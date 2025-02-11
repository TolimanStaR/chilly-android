package com.chilly.android.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.chilly.android.R
import timber.log.Timber

data class TopBarState(
    @StringRes val titleRes: Int,
    val showBackButton: Boolean = false,
    val showProfileAction: Boolean = true
)

sealed interface TopBarEvent {
    data object BackClicked : TopBarEvent
    data object ProfileClicked : TopBarEvent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChillyTopBar(
    state: TopBarState,
    onEvent: (TopBarEvent) -> Unit
) {
   TopAppBar(
        title = {
            Text(text = stringResource(state.titleRes))
        },
        navigationIcon = {
            if (state.showBackButton) {
                IconButton(
                    onClick = { onEvent(TopBarEvent.BackClicked) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (state.showProfileAction) {
                IconButton(
                    onClick = { onEvent(TopBarEvent.ProfileClicked) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

data class BottomNavigationRoute<T : Destination>(
    val titleRes: Int,
    val route: T,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
)

val bottomNavigationRoutes = listOf(
    BottomNavigationRoute(
        titleRes = R.string.history_screen_title,
        route = Destination.History,
        activeIcon = Icons.Default.Menu,
        inactiveIcon = Icons.Outlined.Menu
    ),
    BottomNavigationRoute(
        titleRes = R.string.main_screen_title,
        route = Destination.Main,
        activeIcon = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home
    ),
    BottomNavigationRoute(
        titleRes = R.string.favorites_screen_title,
        route = Destination.Favorites,
        activeIcon = Icons.Filled.Favorite,
        inactiveIcon = Icons.Outlined.Favorite
    )
)

@Composable
fun ChillyBottomBar(
    backStackEntry: NavBackStackEntry?,
    onNavigation: (Destination) -> Unit,
) {
    NavigationBar {
        bottomNavigationRoutes.forEach { item ->
            val selected = backStackEntry?.destination?.hasRoute(item.route::class) ?: false
            NavigationBarItem(
                selected = selected,
                onClick = {
                    Timber.i("clicked on item with route: ${item.route}")
                    onNavigation(item.route)
                },
                icon = {
                    val imageVector = if (selected) item.activeIcon else item.inactiveIcon
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null
                    )
                }
            )
        }
    }
}