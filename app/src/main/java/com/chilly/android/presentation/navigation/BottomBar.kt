package com.chilly.android.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.chilly.android.R

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
        inactiveIcon = Icons.Outlined.FavoriteBorder
    )
)

@Composable
fun ChillyBottomBar(
    backStackEntry: NavBackStackEntry?,
    onNavigation: (Destination) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        bottomNavigationRoutes.forEach { item ->
            val selected = backStackEntry?.destination?.hasRoute(item.route::class) ?: false
            NavigationBarItem(
                colors = with(MaterialTheme.colorScheme) {
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = primary,
                        unselectedIconColor = onSurface,
                        indicatorColor = Color.Transparent
                    )
                },
                selected = selected,
                onClick = {
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