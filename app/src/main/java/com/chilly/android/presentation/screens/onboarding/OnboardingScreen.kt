package com.chilly.android.presentation.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerOnboardingComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonColor
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.structure.EffectCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.slidingComposable
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.theme.Gray90
import com.chilly.android.presentation.theme.Peach10


@Composable
private fun OnboardingScreen(
    onboarding: Destination.Onboarding,
    onEvent: (OnboardingEvent) -> Unit,
    scaffoldPadding: PaddingValues
) {
    Surface(
        contentColor = Gray90,
        color = Peach10
    ) {
        val onboardingUi = OnboardingUi.onboardings[onboarding.index]
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(dimensionResource(R.dimen.main_padding))
        ) {
            Stepper(onboarding.index)
            Image(
                painterResource(onboardingUi.imageId),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(onboardingUi.titleId),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(onboardingUi.mainTextId),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onboarding.index != OnboardingUi.onboardings.lastIndex) {
                    ChillyButton(
                        textRes = R.string.onboarding_skip_text,
                        color = ChillyButtonColor.Gray,
                        type = ChillyButtonType.Tertiary,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onEvent(OnboardingEvent.Finish(onboarding.loggedIn))
                        }
                    )
                }
                ChillyButton(
                    textRes = onboardingUi.nextTextId,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(OnboardingEvent.NextStep(onboarding.index, onboarding.loggedIn, OnboardingUi.count))
                    }
                )
            }
        }
    }
}

@Composable
private fun Stepper(index: Int) {
    Row {
        repeat(OnboardingUi.count) { item ->
            val color = with(MaterialTheme.colorScheme) {
                if (item == index) primary else primaryContainer
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
                    .size(24.dp, 4.dp)
            )
        }
    }
}


fun NavGraphBuilder.installOnboardingComposable(padding: PaddingValues) {
    slidingComposable<Destination.Onboarding>{ backStack ->
        ScreenHolder(
            componentFactory = {
                DaggerOnboardingComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            viewModelFactory = { viewModel() }
        ) {
            OnboardingScreen(
                backStack.toRoute(),
                viewModel::dispatch,
                padding
            )
            EffectCollector(component.effectCollector)
        }
    }
}


private class OnboardingUi(
    @DrawableRes val imageId: Int,
    @StringRes val titleId: Int,
    @StringRes val mainTextId: Int,
    @StringRes val nextTextId: Int
) {
    companion object {
        val onboardings = listOf(
            OnboardingUi(
                R.drawable.chilly_onboarding1,
                R.string.onboarding1_title,
                R.string.onboarding1_text,
                R.string.onboarding1_next
            ),
            OnboardingUi(
                R.drawable.chilly_onboarding2,
                R.string.onboarding2_title,
                R.string.onboarding2_text,
                R.string.onboarding2_next
            ),
            OnboardingUi(
                R.drawable.chilly_onboarding3,
                R.string.onboarding3_title,
                R.string.onboarding3_text,
                R.string.onboarding3_next
            )
        )
        val count = onboardings.size
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun PreviewOnboarding1(
    @PreviewParameter(IndexProvider::class) index: Int
) {
    ChillyTheme {
        OnboardingScreen(Destination.Onboarding(index), { }, PaddingValues())
    }
}

private class IndexProvider : PreviewParameterProvider<Int> {
    override val values = (0..2).asSequence()
}
