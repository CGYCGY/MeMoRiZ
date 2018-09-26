package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.SetData
import kotlinx.android.synthetic.main.base_list_quiz_set.view.*

class QuizSetAdapter : RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {


    private var context: Context
    private var quizSetList : List<SetData>
    private var quizSetView : QuizSetAdapterInterface
    var count = 1

    constructor(context: Context, dbList: List<SetData>, quizSetView : QuizSetAdapterInterface) {
        this.context = context
        this.quizSetList = dbList
        this.quizSetView = quizSetView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_quiz_set, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = quizSetList[position]
        val qsize = "Quiz Set Size: " + data.size.toString()
        holder.name.text = data.name
        holder.size.text = qsize

        holder.itemView?.setOnClickListener {
            quizSetView.onClick(data)
        }

        holder.itemView?.setOnLongClickListener {
            quizSetView.onLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int {
        return quizSetList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var size: TextView

        init {
            name = itemView.quiz_set_name
            size = itemView.quiz_set_size
        }
    }
}