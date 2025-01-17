package com.chilly.android.presentation.common.components

sealed interface SizeParameter {
    data object Small : SizeParameter
    data object Medium : SizeParameter
}