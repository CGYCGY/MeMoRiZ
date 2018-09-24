package cgy.memoriz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.fragment.chat.Chat
import cgy.memoriz.fragment.mainmenu.second.MainMenu2
import cgy.memoriz.fragment.student.StudentClassList
import cgy.memoriz.fragment.student.StudentFCSetList
import cgy.memoriz.share.profile.ShowProfile
import kotlinx.android.synthetic.main.fragment_student_home.view.*

/*Every fragment extend MainActivityBaseFragment() so that can use the function implemented.
 * Default will extend fragment. Change it.*/
class StudentMainMenu : MainActivityBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_home, container, false)
        SharedPref.init(context!!)

        /*changing toolbar title */
        setTitle("StudentMainMenu")

        /*
         * If dun want transfer data put it switchFragment(SecondPage())
         * If wan transfer data put switchFragment(Secondpage(data that need to transfer)
         */
        view.studentProfileBtn.setOnClickListener {
            //switchFragment(SecondPage().newInstance("Data to transfer"))
            // transfer object use below.
            switchFragment(ShowProfile())
//            switchFragment(SecondPage().newInstance())
        }

        view.studentFlashcardBtn.setOnClickListener {
            switchFragment(StudentFCSetList())
        }

        view.studentQuestionBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.studentQuestionBtn.text.toString()))
        }

        view.studentReportBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.studentReportBtn.text.toString()))
        }

        view.studentClassBtn.setOnClickListener {
            switchFragment(StudentClassList())
        }

        view.studentStudentsHallBtn.setOnClickListener {
            switchFragment(Chat())
        }

        return view
    }

    override fun onResume() {
        /*
         *AppUiState is to handle onBackPress()
         * When you navigate away from your home page ,then the toolbar icon will switch to arrow.
         * When navigate back to homepage, the toolbar icon will switch back to hamburger icon.
         */
        SharedPref.arrow = false
        updateToolbarIconState(SharedPref.arrow)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        SharedPref.arrow = true
        updateToolbarIconState(SharedPref.arrow)
    }
}
