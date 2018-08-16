package cgy.memoriz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.fragment.mainmenu.second.MainMenu2
import cgy.memoriz.fragment.second.SecondPage
import kotlinx.android.synthetic.main.fragment_home_page.view.*

/*Every fragment exted MainActivityBaseFragment() so that can use the function implemented.
 * Default will extend fragment. Change it.*/
class StudentMainMenu : MainActivityBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        SharedPref.init(context!!)
        SharedPref.arrow = false

        /*changing toolbar title */
        setTitle("StudentMainMenu")

        /*
         * If dun want transfer data put it switchFragment(SecondPage())
         * If wan transfer data put switchFragment(Secondpage(data that need to transfer)
         */
        view.studentProfileBtn.setOnClickListener {
            //switchFragment(SecondPage().newInstance("Data to transfer"))
            // transfer object use below.
            switchFragment(SecondPage())
//            switchFragment(SecondPage().newInstance())
        }

        view.studentFlashcardBtn.setOnClickListener {
            switchFragment(SecondPage())
        }

        view.studentQuestionBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.studentQuestionBtn.text.toString()))
        }

        view.studentReportBtn.setOnClickListener {
            switchFragment(SecondPage())
        }

        view.studentClassBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.studentClassBtn.text.toString()))
        }

        view.studentStudentsHallBtn.setOnClickListener {
            switchFragment(SecondPage())
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        /*
         *AppUiState is to handle onBackPress()
         * When you navigate away from your home page ,then the toolbar icon will switch to arrow.
         * When navigate back to homepage, the toolbar icon will switch back to hamburger icon.
         */
        SharedPref.arrow = false
        updateToolbarIconState(SharedPref.arrow)
    }

    override fun onPause() {
        super.onPause()
        SharedPref.arrow = true
        updateToolbarIconState(SharedPref.arrow)
    }
}
