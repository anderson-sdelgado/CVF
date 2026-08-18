package br.com.usinasantafe.cvf.presenter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Routes.CONFIG_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.FRONT_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.RELEASE_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.SPLASH_ROUTE
import br.com.usinasantafe.cvf.presenter.view.configuration.config.ConfigScreen
import br.com.usinasantafe.cvf.presenter.view.manager.front.FrontScreen
import br.com.usinasantafe.cvf.presenter.view.manager.release.ReleaseScreen
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
                onNavFront = navActions::navigateToFront,
                onNavNote = {}
            )
        }

        composable(
            FRONT_ROUTE,
            arguments = listOf(
                navArgument(ID_FRONT_ARG) { type = NavType.IntType }
            )
        ){
            FrontScreen(
                onNavRelease = {
                    navActions.navigateToRelease(it)
                },
                onNavConfig = navActions::navigateToConfig
            )
        }

        composable(
            RELEASE_ROUTE,
            arguments = listOf(
                navArgument(ID_FRONT_ARG) { type = NavType.IntType }
            )
        ){ entry ->
            ReleaseScreen(
                onNavFront = {
                    navActions.navigateToFront(
                        idFront = entry.arguments?.getInt(ID_FRONT_ARG)!!
                    )
                },
                onNavDriver = {}
            )
        }

    }
}