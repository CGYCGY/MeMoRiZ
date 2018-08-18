package cgy.memoriz.fragment.student

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.MainMenuActivity
import cgy.memoriz.R
import cgy.memoriz.adapter.QHelperAdapterInterface
import cgy.memoriz.data.QHelperData
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_qhelper_detail.view.*

class StudentQHelperDetail : MainActivityBaseFragment() {

    fun newInstance(question: QHelperData): StudentQHelperDetail{
        val args = Bundle()
        args.putSerializable("question detail", question)
        val fragment = StudentQHelperDetail()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_qhelper_detail, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val question : QHelperData = bundle.getSerializable("question detail") as QHelperData

            view.qhelper_detail_title.text = Editable.Factory.getInstance().newEditable(question.title)
            view.qhelper_detail_body.text = Editable.Factory.getInstance().newEditable(question.body)
            view.qhelper_detail_datetime.text = Editable.Factory.getInstance().newEditable(question.datetime)
            view.qhelper_detail_condition.text = Editable.Factory.getInstance().newEditable(question.condition)

            setTitle("Question Editor")

        }else {
            Log.e("missing QHelperData", "StudentQHelperDetail got error!")
        }

        view.updateQuestionBtn.setOnClickListener {

            val intent = Intent(context, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        return view
    }
}