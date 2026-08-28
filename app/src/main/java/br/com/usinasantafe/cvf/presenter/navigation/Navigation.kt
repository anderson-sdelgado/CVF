package br.com.usinasantafe.cvf.presenter.navigation

import androidx.navigation.NavHostController
import br.com.usinasantafe.cvf.lib.Option
import br.com.usinasantafe.cvf.lib.OptionMenu
import br.com.usinasantafe.cvf.lib.OptionReturn
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_MENU_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Args.OPTION_RETURN_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Screens.CART_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.CONFIG_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.DRIVER_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.FRONT_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.MSG_CART_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.PASSWORD_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.RELEASE_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.REVIEW_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.SPLASH_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.TRUCK_SCREEN

object Screens {
    const val SPLASH_SCREEN = "splashScreen"
    const val PASSWORD_SCREEN = "passwordScreen"
    const val CONFIG_SCREEN = "configScreen"
    const val FRONT_SCREEN = "frontScreen"
    const val RELEASE_SCREEN = "releaseScreen"
    const val DRIVER_SCREEN = "driverScreen"
    const val TRUCK_SCREEN = "truckScreen"
    const val CART_SCREEN = "cartScreen"
    const val MSG_CART_SCREEN = "msgCartScreen"
    const val REVIEW_SCREEN = "reviewScreen"
}

object Args {
    const val OPTION_ARG = "option"
    const val OPTION_MENU_ARG = "optionMenu"
    const val OPTION_RETURN_ARG = "optionReturn"
}

object Routes {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val PASSWORD_ROUTE = "$PASSWORD_SCREEN/{$OPTION_MENU_ARG}/{$OPTION_RETURN_ARG}"
    const val CONFIG_ROUTE = "$CONFIG_SCREEN/{$OPTION_ARG}/{$OPTION_RETURN_ARG}"
    const val FRONT_ROUTE = "$FRONT_SCREEN/{$OPTION_ARG}/{$OPTION_RETURN_ARG}/{$OPTION_MENU_ARG}"
    const val RELEASE_ROUTE = "$RELEASE_SCREEN/{$OPTION_ARG}/{$OPTION_RETURN_ARG}/{$OPTION_MENU_ARG}"
    const val DRIVER_ROUTE = DRIVER_SCREEN
    const val TRUCK_ROUTE = TRUCK_SCREEN
    const val CART_ROUTE = CART_SCREEN
    const val MSG_CART_ROUTE = MSG_CART_SCREEN
    const val REVIEW_ROUTE = REVIEW_SCREEN
}

class NavigationActions(private val navController: NavHostController) {

    ///////////////////////// Splash //////////////////////////////////

    fun navigateToSplash() {
        navController.navigate(SPLASH_SCREEN)
    }

    ////////////////////////////////////////////////////////////////////

    ///////////////////////// Config //////////////////////////////////

    fun navigateToConfig(
        option: Int = Option.INSERT.ordinal,
        optionReturn: Int = OptionReturn.DRIVER.ordinal
    ) {
        navController.navigate("$CONFIG_SCREEN/$option/$optionReturn")
    }

    fun navigateToPassword(
        optionMenu: Int,
        optionReturn: Int
    ) {
        navController.navigate("$PASSWORD_SCREEN/$optionMenu/$optionReturn")
    }

    fun navigateToFront(
        option: Int = Option.INSERT.ordinal,
        optionReturn: Int = OptionReturn.DRIVER.ordinal,
        optionMenu: Int = OptionMenu.CONFIG.ordinal
    ) {
        navController.navigate("$FRONT_SCREEN/$option/$optionReturn/$optionMenu")
    }

    fun navigateToRelease(
        option: Int = Option.INSERT.ordinal,
        optionReturn: Int = OptionReturn.DRIVER.ordinal,
        optionMenu: Int = OptionMenu.CONFIG.ordinal
    ) {
        navController.navigate("$RELEASE_SCREEN/$option/$optionReturn/$optionMenu")
    }

    fun navigateToDriver() {
        navController.navigate(DRIVER_SCREEN)
    }

    fun navigateToTruck() {
        navController.navigate(TRUCK_SCREEN)
    }

    fun navigateToCart() {
        navController.navigate(CART_SCREEN)
    }

    fun navigateToMsgCart() {
        navController.navigate(MSG_CART_SCREEN)
    }

    fun navigateToReview() {
        navController.navigate(REVIEW_SCREEN)
    }

    //////////////////////////////////////////////////////////////////////

}