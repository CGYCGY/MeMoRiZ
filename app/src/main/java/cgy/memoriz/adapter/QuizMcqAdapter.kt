package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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
//    private var radioList : RecyclerRadioData
    private var quizView : QuizMcqAdapterInterface
//    private var checkIDList : ArrayList<Int?>

    constructor(context : Context, dbList : List<QuizData>, quizView : QuizMcqAdapterInterface) {
        this.context = context
        this.quizMcqList = dbList
        this.quizView = quizView

//        this.checkIDList = ArrayList()
//        for (i in 0 until this.quizMcqList.size) {
//            checkIDList.add(null)
//        }
//        this.radioList = RecyclerRadioData(this.quizMcqList.size)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_quiz_mcq, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        Log.e("position", position.toString())

        val data = quizMcqList[position]
        holder.no.text = (position+1).toString()
        holder.question.text = data.question
        holder.choice[0].text = data.choices[0]
        holder.choice[1].text = data.choices[1]
        holder.choice[2].text = data.choices[2]
        holder.choice[3].text = data.choices[3]
//        if (checkIDList[position] != null) {
//            for (i in 0 until holder.choice.size) {
//                holder.choice[i].isChecked = holder.choice[i].id == checkIDList[position]!!
//                Log.e("checkedID", holder.choice[i].id.toString())
//                Log.e("checkedID", holder.choice[i].isChecked.toString())
//            }

//            holder.choice[checkIDList[position]!!].isChecked = true
//        }

//        for (i in 0 until checkIDList.size) {
//            Log.e("checkIDList", checkIDList[i].toString())
//        }

        holder.choices.setOnCheckedChangeListener { group, checkedID ->
            // Get the specific radio button and get its state
            val checkedRadioButton = group.findViewById<View>(checkedID) as RadioButton
            // set the button checker state on here
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                // if one of the button checked, what will happen
//                Log.e("checkedID", checkedID.toString())
//                for (i in 0 until holder.choice.size) {
//                    if (holder.choice[i].id == checkedID)
//                        checkIDList[position] = i
//                }
//                checkIDList[position] = checkedID
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
        var choice = ArrayList<RadioButton>()

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
}