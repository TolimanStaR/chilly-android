package com.chilly.android.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chilly.android.R

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_medium, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_italic, FontWeight.Normal, FontStyle.Italic)
)

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = montserratFontFamily,
        fontSize = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = montserratFontFamily,
        fontSize = 14.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = montserratFontFamily,
        fontSize = 12.sp,
    )
)