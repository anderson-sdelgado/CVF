package br.com.usinasantafe.cvf.presenter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.usinasantafe.cvf.lib.Option
import br.com.usinasantafe.cvf.lib.OptionReturn
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_MENU_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_RETURN_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Routes.CONFIG_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.DRIVER_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.FRONT_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.PASSWORD_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.RELEASE_ROUTE
import br.com.usinasantafe.cvf.presenter.navigation.Routes.SPLASH_ROUTE
import br.com.usinasantafe.cvf.presenter.view.configuration.config.ConfigScreen
import br.com.usinasantafe.cvf.presenter.view.configuration.password.PasswordScreen
import br.com.usinasantafe.cvf.presenter.view.manager.front.FrontScreen
import br.com.usinasantafe.cvf.presenter.view.manager.release.ReleaseScreen
import br.com.usinasantafe.cvf.presenter.view.note.driver.DriverScreen
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

        composable(
            CONFIG_ROUTE,
            arguments = listOf(
                navArgument(OPTION_MENU_ARG) { type = NavType.IntType },
                navArgument(OPTION_RETURN_ARG) { type = NavType.IntType },
            )
        ) { entry ->
            val optionReturn = OptionReturn.entries[entry.arguments?.getInt(OPTION_RETURN_ARG)!!]
            ConfigScreen(
                onNavFront = navActions::navigateToFront,
                onNavNote = {
                    when(optionReturn) {
                        OptionReturn.DRIVER -> navActions.navigateToDriver()
                        OptionReturn.TRUCK -> navActions.navigateToTruck()
                        OptionReturn.CART -> navActions.navigateToCart()
                        OptionReturn.MSG_CART -> navActions.navigateToMsgCart()
                        OptionReturn.REVIEW -> navActions.navigateToReview()
                    }
                }
            )
        }

        composable(
            PASSWORD_ROUTE,
            arguments = listOf(
                navArgument(OPTION_MENU_ARG) { type = NavType.IntType },
                navArgument(OPTION_RETURN_ARG) { type = NavType.IntType },
            )
        ){ entry ->
            val optionReturn = OptionReturn.entries[entry.arguments?.getInt(OPTION_RETURN_ARG)!!]
            PasswordScreen(
                onNavConfig = {
                    navActions.navigateToConfig(option = Option.EDIT.ordinal, optionReturn.ordinal)
                },
                onNavFront = {
                    navActions.navigateToFront(option = Option.EDIT.ordinal, optionReturn.ordinal)
                },
                onNavRelease = {
                    navActions.navigateToRelease(option = Option.EDIT.ordinal, optionReturn.ordinal)
                },
                onNavNote = {
                    when(optionReturn) {
                        OptionReturn.DRIVER -> navActions.navigateToDriver()
                        OptionReturn.TRUCK -> navActions.navigateToTruck()
                        OptionReturn.CART -> navActions.navigateToCart()
                        OptionReturn.MSG_CART -> navActions.navigateToMsgCart()
                        OptionReturn.REVIEW -> navActions.navigateToReview()
                    }
                }
            )
        }

        composable(
            FRONT_ROUTE,
            arguments = listOf(
                navArgument(OPTION_MENU_ARG) { type = NavType.IntType },
                navArgument(OPTION_RETURN_ARG) { type = NavType.IntType },
            )
        ){ entry ->
            val optionReturn = OptionReturn.entries[entry.arguments?.getInt(OPTION_RETURN_ARG)!!]
            FrontScreen(
                onNavRelease = {
                    navActions.navigateToRelease()
                },
                onNavConfig = navActions::navigateToConfig,
                onNavNote = {
                    when(optionReturn) {
                        OptionReturn.DRIVER -> navActions.navigateToDriver()
                        OptionReturn.TRUCK -> navActions.navigateToTruck()
                        OptionReturn.CART -> navActions.navigateToCart()
                        OptionReturn.MSG_CART -> navActions.navigateToMsgCart()
                        OptionReturn.REVIEW -> navActions.navigateToReview()
                    }
                }
            )
        }

        composable(
            RELEASE_ROUTE,
            arguments = listOf(
                navArgument(OPTION_MENU_ARG) { type = NavType.IntType },
                navArgument(OPTION_RETURN_ARG) { type = NavType.IntType },
            )
        ){ entry ->
            val optionReturn = OptionReturn.entries[entry.arguments?.getInt(OPTION_RETURN_ARG)!!]
            ReleaseScreen(
                onNavFront = {
                    navActions.navigateToFront()
                },
                onNavDriver = navActions::navigateToDriver,
                onNavNote = {
                    when(optionReturn) {
                        OptionReturn.DRIVER -> navActions.navigateToDriver()
                        OptionReturn.TRUCK -> navActions.navigateToTruck()
                        OptionReturn.CART -> navActions.navigateToCart()
                        OptionReturn.MSG_CART -> navActions.navigateToMsgCart()
                        OptionReturn.REVIEW -> navActions.navigateToReview()
                    }
                }
            )
        }

        composable(DRIVER_ROUTE) {
            DriverScreen(
                onNavPassword = {
                    navActions.navigateToPassword(it.ordinal, OptionReturn.DRIVER.ordinal)
                },
                onNavTruck = {}
            )
        }

    }
}