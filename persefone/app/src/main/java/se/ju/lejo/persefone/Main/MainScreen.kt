package se.ju.lejo.persefone.Main

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import se.ju.lejo.persefone.Fragments.ConnectToBTFragment
import se.ju.lejo.persefone.Fragments.TestFragment
import se.ju.lejo.persefone.Fragments.TimerFragment
import se.ju.lejo.persefone.R


enum class MainScreen(@IdRes val menuItemId: Int,
                      @DrawableRes val menuuIconsId: Int,
                      @StringRes val titleStringId: Int,
                      val fragment: Fragment
) {
    CONNECT(R.id.bottom_navigation_connect, R.drawable.bluetooth , R.string.connect, ConnectToBTFragment()),
    TIMER(R.id.bottom_navigation_timer, R.drawable.timer , R.string.timer, TimerFragment()),
    HISTORY(R.id.bottom_navigation_history, R.drawable.history , R.string.history, TestFragment())
}

fun getMainScreenForMenuItem(menuItemId: Int): MainScreen? {
    for (mainScreen in MainScreen.values()) {
        if (mainScreen.menuItemId == menuItemId) {
            return mainScreen
        }
    }
    return null
}