package cgy.memoriz.fragment.student

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.adapter.QuizMcqResultAdapter
import cgy.memoriz.data.QuizData
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_student_quiz_mcq.view.*



class StudentQuizResult : MainActivityBaseFragment() {
    private lateinit var recycleAdapter: QuizMcqResultAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var quizList : ArrayList<QuizData>

    fun newInstance(text : String) : StudentQuizResult {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = StudentQuizResult()
        fragment.arguments = args
        return fragment
    }

//    fun newInstance(quiz : ArrayList<QuizData>, setID : Int, quizResultID : Int): StudentQuizResult{
    fun newInstance(quiz : ArrayList<QuizData>): StudentQuizResult{
        val args = Bundle()
        args.putSerializable("quiz list", quiz)
//        args.putInt("set id", setID)
//        args.putInt("quiz result id", quizResultID)
        val fragment = StudentQuizResult()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_quiz_mcq, container, false)
        recycleView = view.quiz_mcq_list

        val bundle = arguments
        if (bundle != null) {
            @Suppress("UNCHECKED_CAST")
            quizList = bundle.getSerializable("quiz list") as ArrayList<QuizData>
//            val setID = bundle.getInt("set id")
//            val quizResultID = bundle.getInt("quiz result id")

            val mark = getMark()
            val markPercentage = mark.toFloat()/quizList.size.toFloat()*100
            val showResult = "Result: " + mark + "/" + quizList.size + " (" + markPercentage + "%)"
            view.quiz_timer.textSize = 20.toFloat()
            view.quiz_timer.text = showResult
        }

        setTitle("Quiz Result")
        setRecycleView(quizList)

        view.quizSubmitBtn.text = getString(R.string.QuizResultBtn)
        view.quizSubmitBtn.setOnClickListener {
            cleanAndSwitchFragment(StudentClassList())
        }

        return view
    }

    private fun setRecycleView(quizList: ArrayList<QuizData>) {
        try {
            recycleAdapter = QuizMcqResultAdapter(context!!, quizList)
            val recycleLayout = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            recycleView.layoutManager = recycleLayout
            recycleView.adapter = recycleAdapter
        } catch (e: NullPointerException) {
            Log.d("Quiz MCQ Adapter error:", e.toString())
        }
    }

    //rmb setup
//    private fun jsonToArrayList(obj : JSONArray) {
//        val answerList = ArrayList<String>()
//        val tempList = ArrayList<String>()
//
//        for (i in 0 until obj.length()) {
//            list.add(QuizData(
//                    obj.getJSONObject(i).getInt("qz_id"),
//                    obj.getJSONObject(i).getString("qz_qstn"),
//                    obj.getJSONObject(i).getString("qz_ans"),
//                    obj.getJSONObject(i).getString("qz_ans")))
//
//            answerList.add(obj.getJSONObject(i).getString("qz_ans"))
//        }
//
//        for (i in 0 until list.size) {
//            if (list[i].choices.size < 3) {
//                tempList.clear()
//                tempList.addAll(answerList)
//                tempList.filterString(list[i].answer!!)
//                tempList.shuffleString()
//
//                Log.d("answer", list[i].answer)
//                for (j in 0 until tempList.size) {
//                    Log.d("tempList got", tempList[j])
//                }
//
//                Log.d("templist de size", tempList.size.toString())
//
//                list[i].choices.add(tempList[0])
//                list[i].choices.add(tempList[1])
//                list[i].choices.add(tempList[2])
//
//                Log.d("templist de size", tempList.size.toString())
//
//                list[i].choices.shuffleString()
//            }
//        }
//
//        setRecycleView(list)
//    }

    private fun getMark() : Int {
        var mark = 0

        for (i in 0 until quizList.size) {
            if (quizList[i].answer == quizList[i].choices[quizList[i].studentSelection])
                mark++
        }
        return mark
    }
}