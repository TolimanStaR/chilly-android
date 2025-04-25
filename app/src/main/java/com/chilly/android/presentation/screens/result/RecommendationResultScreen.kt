package com.chilly.android.presentation.screens.result

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerRecommendationResultComponent
import com.chilly.android.di.screens.RecommendationResultComponent
import com.chilly.android.presentation.common.components.ErrorReloadPlaceHolder
import com.chilly.android.presentation.common.components.LoadingPlaceholder
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme


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
            LoadingPlaceholder(stringResource(R.string.recommendation_result_loading)) {
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        items(state.recommendations) { place ->
            PlaceListItem(place) {
                onEvent(UiEvent.PlaceClicked(place.id))
            }
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

fun NavGraphBuilder.installRecommendationResultScreen(padding: PaddingValues) {
    fadingComposable<Destination.RecommendationResult> {
        ScreenHolder<RecommendationResultStore, RecommendationResultComponent>(
            componentFactory = {
                DaggerRecommendationResultComponent.builder()
                    .appComponent(applicationComponent)
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
            state = RecommendationResultState(),
            onEvent = {},
            padding = PaddingValues()
        )
    }
}

