package cgy.memoriz.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.QuizData
import kotlinx.android.synthetic.main.base_list_quiz_mcq.view.*



class QuizMcqResultAdapter : RecyclerView.Adapter<QuizMcqResultAdapter.ViewHolder> {


    private var context: Context
    private var quizMcqList : List<QuizData>

    constructor(context : Context, dbList : List<QuizData>) {
        this.context = context
        this.quizMcqList = dbList

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_quiz_mcq, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val data = quizMcqList[position]
        val number = " " + (position+1).toString()
        holder.no.text = number
        holder.question.text = data.question
        holder.choice[0].text = data.choices[0]
        holder.choice[1].text = data.choices[1]
        holder.choice[2].text = data.choices[2]
        holder.choice[3].text = data.choices[3]

        labelAnswer(holder, data)
    }

    override fun getItemCount() : Int {
        return quizMcqList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var no : TextView
        var question: TextView
        var choices: RadioGroup
        var choice = ArrayList<TextView>()

        init {
            no = itemView.qm_question_no
            question = itemView.qm_question
            choices = itemView.qm_choices
            choice.add(itemView.qm_choice1)
            choice.add(itemView.qm_choice2)
            choice.add(itemView.qm_choice3)
            choice.add(itemView.qm_choice4)
        }
    }

    private fun labelAnswer(holder : ViewHolder, data : QuizData) {
        if (data.answer == data.choices[data.studentSelection]) {
            holder.choice[data.studentSelection].setBackgroundColor(Color.parseColor("#2ad338"))
        }
        else {
            holder.choice[data.studentSelection].setBackgroundColor(Color.parseColor("#fa2e43"))

            for (i in 0 until data.choices.size) {
                if (data.answer == data.choices[i]) {
                    holder.choice[i].setBackgroundColor(Color.parseColor("#2ad338"))
                    break
                }
            }
        }
    }
}