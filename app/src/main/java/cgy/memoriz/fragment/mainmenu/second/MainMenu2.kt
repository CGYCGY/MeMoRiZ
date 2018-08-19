package cgy.memoriz.fragment.mainmenu.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.fragment.student.StudentQHelper
import cgy.memoriz.fragment.student.StudentQSolver
import kotlinx.android.synthetic.main.fragment_student_mainmenu2.view.*

class MainMenu2 : MainActivityBaseFragment() {

    private var textGet : String ?= null

    fun newInstance(text : String) : MainMenu2 {
        val args = Bundle()
        args.putString("value1", text)
        val fragment = MainMenu2()
        fragment.arguments = args
        return fragment
    }
    fun newInstance(): MainMenu2{
        val args = Bundle()
        args.putSerializable("transfer data with this", "nothing")
        val fragment = MainMenu2()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_mainmenu2, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
//            textGet = bundle.getSerializable("key2") as String
            textGet = bundle.getString("value1")
            //setTitle("$textGet")
            setTitle("$textGet")
        }else {
            setTitle("This is second page.")
        }

        if (textGet == "Question") {
            view.studentQHelperBtn.text = getString(R.string.QHelper)
            view.studentQSolverBtn.text = getString(R.string.QSolver)

            view.studentQHelperBtn.setOnClickListener {
                switchFragment(StudentQHelper())
            }
            /**
             * TO-DO
             * create question helper page
             * create new class question helper
             * create new fragment xml
             * show list of question owned
             * let user create new question
             */

            view.studentQSolverBtn.setOnClickListener {
                switchFragment(StudentQSolver())
            }
        }
        else if (textGet == "Classroom") {
            view.studentQHelperBtn.text = getString(R.string.Quiz)
            view.studentQSolverBtn.text = getString(R.string.Slides)

            view.studentQHelperBtn.setOnClickListener {
                switchFragment(StudentMainMenu())
            }

            view.studentQSolverBtn.setOnClickListener {
                switchFragment(StudentMainMenu())
            }
        }

        return view
    }
}
