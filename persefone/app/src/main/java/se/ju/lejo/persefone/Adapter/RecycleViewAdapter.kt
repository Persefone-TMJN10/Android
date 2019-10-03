package se.ju.lejo.persefone.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.ju.lejo.persefone.Models.HistoryListItem
import se.ju.lejo.persefone.R


class RecycleViewAdapter(val historyListItem: ArrayList<HistoryListItem>, val context: Context):
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_layout, p0, false))
    }

    override fun getItemCount(): Int {
        return historyListItem.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.inTime?.text = historyListItem[p1].inTime
        p0.outTime?.text = historyListItem[p1].outTime
        p0.accumulatedRad?.text = historyListItem[p1].totalRadAccumulated
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val inTime = itemView.findViewById<TextView>(R.id.tw_inTime_history)
        val outTime = itemView.findViewById<TextView>(R.id.tw_outTime_history)
        val accumulatedRad = itemView.findViewById<TextView>(R.id.tw_radiationAccumulated_history)
    }

}