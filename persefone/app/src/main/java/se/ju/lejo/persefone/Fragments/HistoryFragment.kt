package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.lejo.persefone.R

class HistoryFragment: Fragment() {

    private var theView: View? = null

    companion object {
        const val TAG = "tag_history_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.history_fragment_layout, container, false)
        }

        return theView
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setUpViews() {

    }




}