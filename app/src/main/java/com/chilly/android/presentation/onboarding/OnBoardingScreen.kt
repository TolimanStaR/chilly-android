package com.chilly.android.presentation.onboarding

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerOnboardingComponent
import com.chilly.android.di.screens.OnboardingComponent
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.checkClearNavigate
import com.chilly.android.presentation.theme.Peach10
import com.chilly.android.presentation.theme.Red10
import com.chilly.android.presentation.theme.Red50


@Composable
private fun OnBoardingScreen(
    onboarding: Destination.OnBoarding,
    onEvent: (OnBoardingEvent) -> Unit
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
                fontSize = 24.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = stringResource(onboardingUi.mainTextId),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onboarding.index != OnboardingUi.onboardings.lastIndex) {
                    ChillyButton(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black,
                        textRes = R.string.onboarding_skip_text,
                    ) {
                        onEvent(OnBoardingEvent.Finish)
                    }
                }
                ChillyButton(
                    backgroundColor = Red50,
                    textColor = Color.White,
                    textRes = onboardingUi.nextTextId
                ) {
                    onEvent(OnBoardingEvent.NextStep(onboarding.index, OnboardingUi.count))
                }
            }
        }
    }
}

// maybe should make common component

@Composable
private fun RowScope.ChillyButton(
    backgroundColor: Color,
    textColor: Color,
    @StringRes textRes: Int,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corners)),
        onClick = onClick,
        modifier = Modifier.weight(1f)
    ) {
        Text(text = stringResource(textRes))
    }
}

fun NavGraphBuilder.onBoardingComposable(navController: NavController) {
    composable<Destination.OnBoarding> { backStack ->
        ScreenHolder(
            viewModelFactory = {
                buildComponent().viewModelFactory()
                    .build(navController::checkClearNavigate)
            }
        ) {
            OnBoardingScreen(
                backStack.toRoute(),
                ::onEvent
            )
        }
    }
}

private fun Context.buildComponent(): OnboardingComponent = DaggerOnboardingComponent.builder()
    .appComponent(applicationComponent)
    .build()

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
@Preview(name = "onboarding 1", showSystemUi = true, showBackground = true)
private fun PreviewOnboarding1() {
    OnBoardingScreen(Destination.OnBoarding(0)) { }
}

@Composable
@Preview(name = "onboarding 2", showSystemUi = true, showBackground = true)
private fun PreviewOnboarding2() {
    OnBoardingScreen(Destination.OnBoarding(1)) { }
}

@Composable
@Preview(name = "onboarding 3", showSystemUi = true, showBackground = true)
private fun PreviewOnboarding3() {
    OnBoardingScreen(Destination.OnBoarding(2)) { }
}
