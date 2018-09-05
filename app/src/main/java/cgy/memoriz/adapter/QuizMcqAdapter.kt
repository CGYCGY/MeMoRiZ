package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.QuizData
import kotlinx.android.synthetic.main.base_list_quiz_mcq.view.*



class QuizMcqAdapter : RecyclerView.Adapter<QuizMcqAdapter.ViewHolder> {


    private var context: Context
    private var quizMcqList : List<QuizData>
    private var quizView : QuizMcqAdapterInterface

    constructor(context : Context, dbList : List<QuizData>, quizView : QuizMcqAdapterInterface) {
        this.context = context
        this.quizMcqList = dbList
        this.quizView = quizView

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_quiz_mcq, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        val data = quizMcqList[position]
        holder.no.text = (position+1).toString()
        holder.question.text = data.question
        holder.choice1.text = data.choices[0]
        holder.choice2.text = data.choices[1]
        holder.choice3.text = data.choices[2]
        holder.choice4.text = data.choices[3]

        holder.choices.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // Changes the textview's text to "Checked: example radiobutton text"
                quizView.onCheck(data, checkedRadioButton.text.toString())
            }
        }
    }

    override fun getItemCount() : Int {
        return quizMcqList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var no : TextView
        var question: TextView
        var choices: RadioGroup
        var choice1: TextView
        var choice2: TextView
        var choice3: TextView
        var choice4: TextView

        init {
            no = itemView.qm_question_no
            question = itemView.qm_question
            choices = itemView.qm_choices
            choice1 = itemView.qm_choice1
            choice2 = itemView.qm_choice2
            choice3 = itemView.qm_choice3
            choice4 = itemView.qm_choice4
        }
    }
}