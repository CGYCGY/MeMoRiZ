package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.AnswerData
import kotlinx.android.synthetic.main.base_list_answer.view.*

class QSolverAnswerAdapter : RecyclerView.Adapter<QSolverAnswerAdapter.ViewHolder> {


    private var c: Context
    private var questionList : List<AnswerData>
    private var qSolverView : QSolverAnswerAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<AnswerData>, qSolverView : QSolverAnswerAdapterInterface) {
        this.c = c
        this.questionList = dbList
        this.qSolverView = qSolverView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_answer, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = questionList[position]
        holder.body.text = data.body
        holder.owner.text = data.owner
        holder.datetime.text = data.datetime

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
        var body: TextView
        var owner: TextView
        var datetime: TextView

        init {
            body = itemView.ans_body
            owner = itemView.ans_owner
            datetime = itemView.ans_datetime
        }
    }
}