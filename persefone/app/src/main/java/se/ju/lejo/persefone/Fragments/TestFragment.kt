package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import se.ju.lejo.persefone.R

class TestFragment : Fragment(){

    var theView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {

            val view = inflater.inflate(R.layout.test_fragment, container, false)

            theView = view
        }

        return theView
    }
}