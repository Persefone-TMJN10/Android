package se.ju.lejo.persefone.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.ju.lejo.persefone.Models.HistoryList
import se.ju.lejo.persefone.R


class RecycleViewAdapter(val historyList: ArrayList<HistoryList>):
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.adapter_item_layout, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.inTime?.text = historyList[p1].inTime
        p0.outTime?.text = historyList[p1].outTime
        p0.accumulatedRad?.text = historyList[p1].totalRadAccumulated.toString()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val inTime = itemView.findViewById<TextView>(R.id.tw_inTime_history)
        val outTime = itemView.findViewById<TextView>(R.id.tw_outTime_history)
        val accumulatedRad = itemView.findViewById<TextView>(R.id.tw_radiationAccumulated_history)
    }

}