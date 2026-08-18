package br.com.usinasantafe.cvf.presenter.navigation

import androidx.navigation.NavHostController
import br.com.usinasantafe.cvf.presenter.navigation.Args.ID_FRONT_ARG
import br.com.usinasantafe.cvf.presenter.navigation.Screens.CONFIG_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.FRONT_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.PASSWORD_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.RELEASE_SCREEN
import br.com.usinasantafe.cvf.presenter.navigation.Screens.SPLASH_SCREEN

object Screens {
    const val SPLASH_SCREEN = "splashScreen"
    const val PASSWORD_SCREEN = "passwordScreen"
    const val CONFIG_SCREEN = "configScreen"
    const val FRONT_SCREEN = "frontScreen"
    const val RELEASE_SCREEN = "releaseScreen"
}

object Args {
    const val OPTION_ARG = "option"
    const val ID_FRONT_ARG = "idFront"
}

object Routes {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val PASSWORD_ROUTE = PASSWORD_SCREEN
    const val CONFIG_ROUTE = CONFIG_SCREEN
    const val FRONT_ROUTE = "$FRONT_SCREEN/{$ID_FRONT_ARG}"
    const val RELEASE_ROUTE = "$RELEASE_SCREEN/{$ID_FRONT_ARG}"
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

    fun navigateToFront(idFront: Int = 0) {
        navController.navigate("$FRONT_SCREEN/$idFront")
    }

    fun navigateToRelease(idFront: Int = 0) {
        navController.navigate("$RELEASE_SCREEN/$idFront")
    }

    //////////////////////////////////////////////////////////////////////

}