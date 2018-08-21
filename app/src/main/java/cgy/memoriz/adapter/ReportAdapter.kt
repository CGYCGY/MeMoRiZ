package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.ReportData
import kotlinx.android.synthetic.main.base_list_report.view.*

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ViewHolder> {


    private var context: Context
    private var reportList : List<ReportData>
    private var reportView : ReportAdapterInterface
    var count = 1

    constructor(context: Context, dbList: List<ReportData>, reportView : ReportAdapterInterface) {
        this.context = context
        this.reportList = dbList
        this.reportView = reportView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_report, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = reportList[position]
        holder.title.text = data.title
        holder.type.text = data.type
        holder.body.text = data.body
        holder.datetime.text = data.datetime
        holder.status.text = data.status

        holder.itemView?.setOnClickListener(View.OnClickListener {
            reportView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            reportView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var type: TextView
        var body: TextView
        var datetime: TextView
        var status: TextView

        init {
            title = itemView.report_title
            type = itemView.report_type
            body = itemView.report_body
            datetime = itemView.report_datetime
            status = itemView.report_stat
        }
    }
}