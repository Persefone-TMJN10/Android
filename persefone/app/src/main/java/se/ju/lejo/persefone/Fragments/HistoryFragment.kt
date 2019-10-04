package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.ju.lejo.persefone.Adapter.RecycleViewAdapter
import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.Data.RestHandler
import se.ju.lejo.persefone.Models.HistoryListItem
import se.ju.lejo.persefone.R

class HistoryFragment: Fragment() {

    private var theView: View? = null
    private var recycleViewAdapter: RecycleViewAdapter? = null
    private var listOfHistoryItems: ArrayList<HistoryListItem> = ArrayList()

    companion object {
        const val TAG = "tag_historyFragment_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {

            val view = inflater.inflate(R.layout.history_fragment_layout, container, false)

            view.findViewById<RecyclerView>(R.id.history_recycler_view)

            view.findViewById<RecyclerView>(R.id.history_recycler_view).layoutManager = this.activity?.let { LinearLayoutManager(it) }
            recycleViewAdapter = this.context?.let { RecycleViewAdapter(listOfHistoryItems, it) }
            view.findViewById<RecyclerView>(R.id.history_recycler_view).adapter = recycleViewAdapter

            theView = view
        }

        if (DataHandler.getRfId() != null) {
            updateRecycleViewAdapter()
        }

        return theView
    }

    fun updateRecycleViewAdapter() {

        if (DataHandler.getRfId() != null) {
            RestHandler.getSessionForSpecificRFID(DataHandler.getRfId()) {
                if(it) {
                    activity!!.runOnUiThread {
                        listOfHistoryItems.clear()
                        listOfHistoryItems.addAll(DataHandler.getHistoryListForRFID())

                        recycleViewAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }

    }
}