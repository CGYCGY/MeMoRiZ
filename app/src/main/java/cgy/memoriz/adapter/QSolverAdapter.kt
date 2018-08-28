package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.QuestionData
import kotlinx.android.synthetic.main.base_list_qsolver.view.*

class QSolverAdapter : RecyclerView.Adapter<QSolverAdapter.ViewHolder> {


    private var c: Context
    private var questionList : List<QuestionData>
    private var qSolverView : QHelperAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<QuestionData>, qSolverView : QHelperAdapterInterface) {
        this.c = c
        this.questionList = dbList
        this.qSolverView = qSolverView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_qsolver, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = questionList[position]
        holder.title.text = data.title
        holder.body.text = data.body
        holder.owner.text = data.owner
        holder.datetime.text = data.datetime
        holder.condition.text = data.condition

        holder.itemView?.setOnClickListener(View.OnClickListener {
            qSolverView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            qSolverView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var body: TextView
        var owner: TextView
        var datetime: TextView
        var condition: TextView

        init {
            title = itemView.qsolver_title
            body = itemView.qsolver_body
            owner = itemView.qsolver_owner
            datetime = itemView.qsolver_datetime
            condition = itemView.qsolver_condition
        }
    }
}