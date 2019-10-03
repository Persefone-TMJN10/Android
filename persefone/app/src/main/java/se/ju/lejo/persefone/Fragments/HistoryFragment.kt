package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.history_fragment_layout.view.*
import se.ju.lejo.persefone.Adapter.RecycleViewAdapter
import se.ju.lejo.persefone.Models.HistoryListItem
import se.ju.lejo.persefone.R

class HistoryFragment: Fragment() {

    private var theView: View? = null
    private var dummyData = ArrayList<HistoryListItem>()
    var recycleViewAdapter: RecycleViewAdapter? = null

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
            dummyData.add(HistoryListItem("2019-10-02 22:33:57", "2019-10-02 22:33:57", 30))
            dummyData.add(HistoryListItem("2019-10-03 22:33:57", "2019-10-03 22:33:57", 40))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 90))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 500))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 290))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 290))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 290))
            dummyData.add(HistoryListItem("2019-10-04 22:33:57", "2019-10-04 22:33:57", 290))
            view.history_recycler_view.layoutManager = this.activity?.let { LinearLayoutManager(it) }
            recycleViewAdapter = this.context?.let { RecycleViewAdapter(dummyData, it) }
            view.history_recycler_view.adapter = recycleViewAdapter
            theView = view
        }

        return theView
    }
}