package cgy.memoriz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.fragment.mainmenu.second.MainMenu2
import cgy.memoriz.fragment.student.StudentQSolver
import cgy.memoriz.share.profile.ShowProfile
import kotlinx.android.synthetic.main.fragment_lecturer_home.view.*

class LecturerMainMenu : MainActivityBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lecturer_home, container, false)
        SharedPref.init(context!!)
        SharedPref.arrow = false

        /*changing toolbar title */
        setTitle("LecturerMainMenu")

        view.lecturerProfileBtn.setOnClickListener {
            switchFragment(ShowProfile())
        }

        view.lecturerCManagerBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.lecturerCManagerBtn.text.toString()))
        }

        view.lecturerReportBtn.setOnClickListener {
            switchFragment(MainMenu2().newInstance(view.lecturerReportBtn.text.toString()))
        }

        view.lecturerQSolverBtn.setOnClickListener {
            switchFragment(StudentQSolver())
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
