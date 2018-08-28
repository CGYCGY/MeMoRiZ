package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.ChatHistoryData
import kotlinx.android.synthetic.main.recent_chat.view.*

class ChatHistoryAdapter : RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {


    private var c: Context
    private var historyList: List<ChatHistoryData>
    private var historyView: ChatHistoryInterface

    constructor(c: Context, list: List<ChatHistoryData>, historyView: ChatHistoryInterface) {
        this.c = c
        this.historyList = list
        this.historyView = historyView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_chat, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var data = historyList[position]
        holder.tv_name.text = data.receiverName

        holder.itemView?.setOnClickListener {
            historyView.onClick(data)
        }

    }

    fun setData(list : List<ChatHistoryData>) {
        this.historyList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView

        init {
            tv_name = itemView.tv_name
        }
    }
}