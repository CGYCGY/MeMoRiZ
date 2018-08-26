package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.QuizData
import kotlinx.android.synthetic.main.base_list_lec_quiz.view.*

class QuizAdapter : RecyclerView.Adapter<QuizAdapter.ViewHolder> {


    private var context: Context
    private var quizList : List<QuizData>
    private var quizView : QuizAdapterInterface
    var count = 1

    constructor(context: Context, dbList: List<QuizData>, quizView : QuizAdapterInterface) {
        this.context = context
        this.quizList = dbList
        this.quizView = quizView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_lec_quiz, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = quizList[position]
        holder.question.text = data.question
        holder.answer.text = data.answer

        holder.itemView?.setOnClickListener(View.OnClickListener {
            quizView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            quizView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView
        var answer: TextView

        init {
            question = itemView.quiz_question
            answer = itemView.quiz_answer
        }
    }
}