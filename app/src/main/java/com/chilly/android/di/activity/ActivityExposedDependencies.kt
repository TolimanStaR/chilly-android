package com.chilly.android.di.activity

import androidx.compose.material3.SnackbarHostState
import com.chilly.android.presentation.common.structure.SnackbarShower

interface ActivityExposedDependencies :
        ResourceDependencies

interface ResourceDependencies {
    val snackbarShower: SnackbarShower
    val snackbarHostState: SnackbarHostState
}