package se.ju.lejo.persefone.Main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val screens = arrayListOf<MainScreen>()

    fun setItems(screens: List<MainScreen>) {
        this.screens.apply {
            clear()
            addAll(screens)
            notifyDataSetChanged()
        }
    }

    fun getItems(): List<MainScreen> {
        return screens
    }

    override fun getItem(position: Int): Fragment {
        return screens[position].fragment
    }

    override fun getCount(): Int {
        return screens.size
    }
}