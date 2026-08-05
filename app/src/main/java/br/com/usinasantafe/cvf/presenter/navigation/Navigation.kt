package br.com.usinasantafe.cvf.presenter.navigation

import androidx.navigation.NavHostController
import br.com.usinasantafe.cvf.presenter.navigation.Screens.CONFIG_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.PASSWORD_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.SPLASH_SCREEN

object Screens {
    const val SPLASH_SCREEN = "splashScreen"
    const val PASSWORD_SCREEN = "passwordScreen"
    const val CONFIG_SCREEN = "configScreen"
}

object Args {
    const val OPTION_ARG = "option"
}

object Routes {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val PASSWORD_ROUTE = PASSWORD_SCREEN
    const val CONFIG_ROUTE = CONFIG_SCREEN

}

class NavigationActions(private val navController: NavHostController) {

    ///////////////////////// Splash //////////////////////////////////

    fun navigateToSplash() {
        navController.navigate(SPLASH_SCREEN)
    }

    ////////////////////////////////////////////////////////////////////

    ///////////////////////// Config //////////////////////////////////

    fun navigateToPassword() {
        navController.navigate(PASSWORD_SCREEN)
    }

    fun navigateToConfig() {
        navController.navigate(CONFIG_SCREEN)
    }

    //////////////////////////////////////////////////////////////////////

}