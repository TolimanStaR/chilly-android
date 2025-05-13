package com.chilly.android.presentation.screens.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerMainComponent
import com.chilly.android.di.screens.MainComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.PlaceImagesPager
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.components.animatedAlpha
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.main.MainEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    state: MainState,
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current as? Activity
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        onEvent(UiEvent.GotPermissionRequestResult(permissions))
    }
    var permissionRationaleDialogIsShown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { // checkPermissions
        when {
            context != null && MainState.PERMISSIONS.any {
                ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            } -> {
                onEvent(UiEvent.PermissionsGranted)
            }
            context != null && MainState.PERMISSIONS.any {
                ActivityCompat.shouldShowRequestPermissionRationale(context,it)
            } -> {
                permissionRationaleDialogIsShown = true
            }
            else -> {
                permissionLauncher.launch(MainState.PERMISSIONS)
            }
        }
    }

    // Permission rationale
    if (permissionRationaleDialogIsShown) {
        AlertDialog(
            onDismissRequest = {
                permissionRationaleDialogIsShown = false
            },
            confirmButton = {
                ChillyButton(
                    text = "grant permission",
                    onClick = {
                        permissionLauncher.launch(MainState.PERMISSIONS)
                        permissionRationaleDialogIsShown = false
                    }
                )
            },
            title = {
                Text(stringResource(R.string.location_rationale_title))
            },
            text = {
                Text(stringResource(R.string.location_rationale_text))
            }
        )
    }

    if (!state.permissionsChecked) {
        return
    }

    LaunchedEffect(Unit) {
        onEvent(UiEvent.ScreenIsShown)
    }

    val permissionGranted = context != null && MainState.PERMISSIONS.any { ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            onEvent(UiEvent.PermissionsGranted)
        }
    }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(scaffoldPadding)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                onEvent(UiEvent.PulledToRefresh)
            },
            modifier = Modifier.weight(1f)
        ) {
            if (state.feed.isEmpty()) {
                FeedShimmer()
                return@PullToRefreshBox
            }

            val lazyColumnState = rememberLazyListState()
            val layoutInfo by remember {
                derivedStateOf { lazyColumnState.layoutInfo }
            }
            val lastItemVisible = layoutInfo.visibleItemsInfo
                .lastOrNull()?.index == layoutInfo.totalItemsCount - 1

            LaunchedEffect(lastItemVisible) {
                if (lastItemVisible && !state.isLoading) {
                    onEvent(UiEvent.LastFeedElementIsVisible)
                }
            }
            LazyColumn(
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    if (state.locationAccessGranted) {
                        Text(stringResource(R.string.nearby_places_title))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.location_not_enabled_suggestion))
                            Spacer(Modifier.weight(1f))
                            ChillyButton(
                                text = stringResource(R.string.grant_permission_button),
                                onClick = {
                                    if (context == null) return@ChillyButton
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    ).also(context::startActivity)
                                }
                            )
                        }
                    }
                }
                items(state.feed) { place ->
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceBright
                        ),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(UiEvent.PlaceClicked(place))
                        }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            PlaceImagesPager(place)
                            Text(
                                text = place.address
                            )
                        }
                    }
                }
                if (state.isLoading) {
                    item {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator()
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        Text(
            text = stringResource(R.string.chilly_button_caption),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        ChillyButton(
            text = stringResource(R.string.main_chily_button),
            onClick = { onEvent(UiEvent.GetRecommendationClicked) },
            size = SizeParameter.Medium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun NavGraphBuilder.installMainScreen(padding: PaddingValues) {
    fadingComposable<Destination.Main> {
        ScreenHolder<MainStore, MainComponent>(
            componentFactory = {
                DaggerMainComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            MainScreen(state.value, padding, store::dispatch)
        }
    }
}


@Composable
private fun FeedShimmer() {
    val alpha by animatedAlpha(from=0.8f, to = 0.2f, duration = 800)

    Column(
        modifier = Modifier
            .alpha(alpha)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(5) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)

            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "MainScreen", showSystemUi = true, showBackground = true)
private fun PreviewMainScreen() {
    ChillyTheme {
        MainScreen(
            state = MainState(),
            PaddingValues(),
            onEvent = {}
        )
    }
}

