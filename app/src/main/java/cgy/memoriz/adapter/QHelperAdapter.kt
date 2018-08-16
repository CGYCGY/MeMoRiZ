package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.QHelperData
import kotlinx.android.synthetic.main.base_list_qhelper.view.*

class QHelperAdapter : RecyclerView.Adapter<QHelperAdapter.ViewHolder> {


    private var c: Context
    private var questionList : List<QHelperData>
    private var qHelperView : QHelperAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<QHelperData>, qHelperView : QHelperAdapterInterface) {
        this.c = c
        this.questionList = dbList
        this.qHelperView = qHelperView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_qhelper, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = questionList[position]
        holder.title.text = data.title
        holder.body.text = data.body
        holder.datetime.text = data.datetime
        holder.condition.text = data.condition

        holder.itemView?.setOnClickListener(View.OnClickListener {
              qHelperView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
               qHelperView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var body: TextView
        var datetime: TextView
        var condition: TextView

        init {
            title = itemView.qhelper_title
            body = itemView.qhelper_body
            datetime = itemView.qhelper_datetime
            condition = itemView.qhelper_condition
        }
    }
}