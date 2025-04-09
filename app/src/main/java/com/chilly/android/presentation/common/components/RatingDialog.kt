package com.chilly.android.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chilly.android.R
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.presentation.theme.Yellow50
import com.chilly.android.presentation.theme.Yellow70

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceRatingDialog(
    place: PlaceDto,
    onDismiss: () -> Unit,
    onRatingChange: (Float) -> Unit,
    onCommentChange: (String) -> Unit,
    onConfirm: () -> Unit,
    commentValue: String = "",
    ratingValue: Float = 5f
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.rating_dialog_title, place.name))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var sliderState by remember(ratingValue) { mutableFloatStateOf(ratingValue) }
                Text(
                    text = stringResource(R.string.rating_in_dialog, sliderState)
                )
                val sliderColors = SliderDefaults.colors(
                    thumbColor = Yellow70,
                    activeTrackColor =  Yellow50,
                )
                Slider(
                    value = sliderState,
                    onValueChange = {
                        sliderState = it
                    },
                    onValueChangeFinished = {
                        onRatingChange(sliderState)
                    },
                    valueRange = 0f..5f,
                    thumb = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = sliderColors.thumbColor,
                        )
                    },
                    track = {
                        SliderDefaults.Track(
                            sliderState = it,
                            drawStopIndicator = null,
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 0.dp,
                            colors = sliderColors,
                            modifier = Modifier.height(8.dp)
                        )
                    }
                )
                Text(stringResource(R.string.rating_comment_suggest))
                ChillyTextField(
                    value = commentValue,
                    onValueChange = {
                        onCommentChange(it)
                    },
                    placeholderTextRes = R.string.comment_placeholder
                )
            }
        },
        confirmButton = {
            ChillyButton(
                textRes = R.string.dialog_confirm_button,
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            )
        }
    )
}