package cgy.memoriz.fragment.mainmenu.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.data.ClassData
import cgy.memoriz.fragment.LecturerMainMenu
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.fragment.chat.Chat
import cgy.memoriz.fragment.lecturer.LecturerClassList
import cgy.memoriz.fragment.lecturer.LecturerQuizSet
import cgy.memoriz.fragment.report.CreateReport
import cgy.memoriz.fragment.report.ViewReport
import cgy.memoriz.fragment.student.StudentQHelper
import cgy.memoriz.fragment.student.StudentQSolver
import kotlinx.android.synthetic.main.fragment_base_mainmenu2.view.*

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

    fun newInstance(classInfo : ClassData, text : String): MainMenu2{
        val args = Bundle()
        args.putSerializable("class info", classInfo)
        args.putString("value1", text)
        val fragment = MainMenu2()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base_mainmenu2, container, false)
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
            view.mainMenuBtn1.text = getString(R.string.QHelper)
            view.mainMenuBtn2.text = getString(R.string.QSolver)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(StudentQHelper())
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(StudentQSolver())
            }
        }
        else if (textGet == "Classroom") {
            view.mainMenuBtn1.text = getString(R.string.Quiz)
            view.mainMenuBtn2.text = getString(R.string.Slides)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(StudentMainMenu())
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(StudentMainMenu())
            }
        }
        else if (textGet == "Report") {
            view.mainMenuBtn1.text = getString(R.string.CReport)
            view.mainMenuBtn2.text = getString(R.string.VReport)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(CreateReport())
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(ViewReport())
            }
        }
        else if (textGet == "Class Manager") {
            view.mainMenuBtn1.text = getString(R.string.SMManager)
            view.mainMenuBtn2.text = getString(R.string.SManger)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(LecturerClassList())
//                switchFragment(MainMenu2().newInstance(getString(R.string.SMManager)))
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(MainMenu2().newInstance(getString(R.string.SManger)))
            }
        }
        else if (textGet == "Study Material Manager") {
            val classInfo : ClassData = bundle!!.getSerializable("class") as ClassData
            //ltr pass guo qu

            view.mainMenuBtn1.text = getString(R.string.MQuiz)
            view.mainMenuBtn2.text = getString(R.string.MSlide)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(LecturerQuizSet())
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(StudentMainMenu())
            }
        }
        else if (textGet == getString(R.string.SManger)) {
            view.mainMenuBtn1.text = getString(R.string.SSProgress)
            view.mainMenuBtn2.text = getString(R.string.Chat)

            view.mainMenuBtn1.setOnClickListener {
                switchFragment(LecturerMainMenu())
            }

            view.mainMenuBtn2.setOnClickListener {
                switchFragment(Chat())
            }
        }

        return view
    }
}
