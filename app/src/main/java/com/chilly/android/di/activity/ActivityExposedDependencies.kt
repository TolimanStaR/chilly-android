package com.chilly.android.di.activity

import androidx.compose.material3.SnackbarHostState
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.TopBarActionsHandler
import com.chilly.android.presentation.navigation.TopBarEventsRepository

interface ActivityExposedDependencies :
        ResourceDependencies,
        TopBarDependencies

interface ResourceDependencies {
    val snackbarShower: SnackbarShower
    val snackbarHostState: SnackbarHostState
}

interface TopBarDependencies {
    val topBarEventsRepository: TopBarEventsRepository
    val topBarEventHandler: TopBarActionsHandler
}