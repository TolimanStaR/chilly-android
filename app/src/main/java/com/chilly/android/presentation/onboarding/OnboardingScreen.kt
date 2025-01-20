package com.chilly.android.presentation.onboarding

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerOnboardingComponent
import com.chilly.android.di.screens.OnboardingComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonColor
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.structure.EffectCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.clearStackAndNavigate
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.theme.Peach10
import com.chilly.android.presentation.theme.Red10
import com.chilly.android.presentation.theme.Red50
import kotlinx.coroutines.flow.FlowCollector


@Composable
private fun OnboardingScreen(
    onboarding: Destination.Onboarding,
    onEvent: (OnboardingEvent) -> Unit
) {
    Scaffold(
        containerColor = Peach10
    ) { innerPadding ->
        val onboardingUi = OnboardingUi.onboardings[onboarding.index]
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.main_padding))
        ) {
            // stepper
            Row {
                repeat(OnboardingUi.count) { item ->
                    val color = if (item == onboarding.index) Red50 else Red10
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                            .size(24.dp, 4.dp)
                    )
                }
            }
            Image(
                painterResource(onboardingUi.imageId),
                contentDescription = null
            )
            Text(
                text = stringResource(onboardingUi.titleId),
                style = MaterialTheme.typography.headlineMedium
            )
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
                            onEvent(OnboardingEvent.Finish)
                        }
                    )
                }
                ChillyButton(
                    textRes = onboardingUi.nextTextId,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(OnboardingEvent.NextStep(onboarding.index, OnboardingUi.count))
                    }
                )
            }
        }
    }
}


fun NavGraphBuilder.onboardingComposable(navController: NavController) {
    composable<Destination.Onboarding> { backStack ->
        ScreenHolder(
            viewModelFactory = {
                buildComponent().viewModelFactory().build()
            }
        ) {
            OnboardingScreen(
                backStack.toRoute(),
                ::dispatch
            )
            EffectCollector(createEffectCollector(navController))
        }
    }
}

private fun Context.buildComponent(): OnboardingComponent = DaggerOnboardingComponent.builder()
    .appComponent(applicationComponent)
    .build()

private fun createEffectCollector(navController: NavController) = FlowCollector<OnboardingEffect> { effect ->
    when(effect) {
        is OnboardingEffect.NavigateOnboardingScreen -> navController.navigate(Destination.Onboarding(effect.index))
        OnboardingEffect.OnboardingFinished -> navController.clearStackAndNavigate(Destination.Main)
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
        OnboardingScreen(Destination.Onboarding(index)) { }
    }
}

private class IndexProvider : PreviewParameterProvider<Int> {
    override val values = (0..2).asSequence()
}
