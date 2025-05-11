package com.chilly.android.presentation.screens.result

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnLayout
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerRecommendationResultComponent
import com.chilly.android.di.screens.RecommendationResultComponent
import com.chilly.android.presentation.common.components.ErrorReloadPlaceHolder
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.common.components.animatedAlpha
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import timber.log.Timber


@Composable
private fun RecommendationResultScreen(
    state: RecommendationResultState,
    onEvent: (UiEvent) -> Unit,
    padding: PaddingValues
) {

    LaunchedEffect(Unit) {
        onEvent(UiEvent.CheckRequest)
    }

    when {
        state.recommendations.isEmpty() && !state.errorOccurred -> {
            ShimmeringLoadingScreen(padding) {
                onEvent(UiEvent.ScreenShown)
            }
            return
        }
        state.recommendations.isEmpty() && state.errorOccurred -> {
            ErrorReloadPlaceHolder(null) {
                onEvent(UiEvent.LoadAgainClicked)
            }
            return
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onEvent(UiEvent.OnPermissionRequestResult(isGranted))
        }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onEvent(UiEvent.PermissionGranted)
            }
        }
    } else {
        LaunchedEffect(Unit) {
            onEvent(UiEvent.PermissionGranted)
        }
    }
    // show recommendations
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        state.recommendations.forEach { place ->
            PlaceListItem(place) {
                onEvent(UiEvent.PlaceClicked(place.id))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
        ) {
            ResultMapView(
                markers = state.recommendations.map { place ->
                    MarkerData(
                        title = place.name,
                        latitude = place.latitude,
                        longitude = place.longitude
                    )
                },
                onMarkerClick = {
                    Timber.i("marker clicked: ${it.title}")
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ShimmeringLoadingScreen(
    padding: PaddingValues,
    startAction: () -> Unit
) {
    LaunchedEffect(Unit) {
        startAction()
    }
    val alpha by animatedAlpha(from=0.8f, to = 0.2f, duration = 800)

    Column(
        modifier = Modifier
            .alpha(alpha)
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(3) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ResultMapView(
    markers: List<MarkerData>,
    onMarkerClick: (MarkerData) -> Unit,

) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setMultiTouchControls(true)

                val bounds = calculateBounds(markers)
                controller.setCenter(bounds?.centerWithDateLine)
                controller.setZoom(18.0)

                overlays.clear()
                markers.forEach { markerData ->
                    overlays.add(createMarker(this, markerData, onMarkerClick))
                }

                doOnLayout {
                    zoomToBoundingBox(bounds, true, 100)
                }
            }
        },
        update = { view ->
            view.overlays.clear()
            markers.forEach { markerData ->
                view.overlays.add(createMarker(view, markerData, onMarkerClick))
            }
            view.invalidate()
        },
        modifier = Modifier
    )
}

private fun createMarker(
    mapView: MapView,
    markerData: MarkerData,
    onClick: (MarkerData) -> Unit
): Marker {
    return Marker(mapView).apply {
        position = GeoPoint(
            markerData.latitude,
            markerData.longitude
        )
        Timber.i("marker for ${markerData.title} is positioned at lat = ${position.latitude}, lon = ${position.longitude}")
        title = markerData.title
        icon = mapView.context.getDrawable(R.drawable.marker_icon)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        setOnMarkerClickListener { _, _ ->
            onClick(markerData)
            true
        }
    }
}

private fun calculateBounds(
    markers: List<MarkerData>
): BoundingBox? {
    if (markers.isEmpty()) return null
    Timber.e("markers size: ${markers.size}")

    var minLat = Double.MAX_VALUE
    var maxLat = Double.MIN_VALUE
    var minLon = Double.MAX_VALUE
    var maxLon = Double.MIN_VALUE

    markers.forEach { marker ->
        minLat = minOf(minLat, marker.latitude)
        maxLat = maxOf(maxLat, marker.latitude)
        minLon = minOf(minLon, marker.longitude)
        maxLon = maxOf(maxLon, marker.longitude)
    }

    val markersBox = BoundingBox(
        /* north = */ maxLat,
        /* east = */ maxLon,
        /* south = */ minLat,
        /* west = */ minLon
    )

    return markersBox.increaseByScale(1.1)
}

private fun BoundingBox.increaseByScale(scale: Double): BoundingBox {
    val centerLat = centerLatitude
    val centerLon = centerLongitude

    val latSpan = latitudeSpan * scale / 2
    val lonSpan = longitudeSpanWithDateLine * scale / 2

    return BoundingBox(
        centerLat + latSpan, // north
        centerLon + lonSpan, // east
        centerLat - latSpan, // south
        centerLon - lonSpan  // west
    )
}

private operator fun BoundingBox.contains(other: BoundingBox): Boolean {
    return this.contains(other.latNorth, other.lonEast) &&
            this.contains(other.latSouth, other.lonWest)
}

fun NavGraphBuilder.installRecommendationResultScreen(padding: PaddingValues) {
    fadingComposable<Destination.RecommendationResult> {
        ScreenHolder<RecommendationResultStore, RecommendationResultComponent>(
            componentFactory = {
                DaggerRecommendationResultComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = {
                store()
            }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            RecommendationResultScreen(state.value, store::dispatch, padding)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "RecommendationResultScreen", showSystemUi = true, showBackground = true)
private fun PreviewRecommendationResultScreen() {
    ChillyTheme {
        RecommendationResultScreen(
            state = RecommendationResultState(

            ),
            onEvent = {},
            padding = PaddingValues()
        )
    }
}

