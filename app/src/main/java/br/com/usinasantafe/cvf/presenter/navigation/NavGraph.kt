package br.com.usinasantafe.cvf.presenter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.usinasantafe.cvf.presenter.navigation.Routes.CONFIG_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.SPLASH_ROUTE
import br.com.usinasantafe.cvf.presenter.view.configuration.config.ConfigScreen
import br.com.usinasantafe.cvf.presenter.view.splash.SplashScreen


@Composable
fun NavigationGraph(
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = SPLASH_ROUTE,
    navActions: NavigationActions = remember(navHostController) {
        NavigationActions(navHostController)
    }
) {

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {

        composable(SPLASH_ROUTE) {
            SplashScreen(
                onNavConfig = navActions::navigateToConfig
            )
        }

        composable(CONFIG_ROUTE) {
            ConfigScreen(
                onNavFront = {},
                onNavNote = {}
            )
        }

    }
}