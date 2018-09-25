package cgy.memoriz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.fragment.admin.AdminReport
import cgy.memoriz.fragment.admin.AdminUAManager
import kotlinx.android.synthetic.main.fragment_base_mainmenu2.view.*

class AdminMainMenu : MainActivityBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_base_mainmenu2, container, false)
        SharedPref.init(context!!)
        SharedPref.arrow = false

        /*changing toolbar title */
        setTitle("Admin Main Menu")

        view.mainMenuBtn1.text = getString(R.string.UAManager)
        view.mainMenuBtn2.text = getString(R.string.ReportManager)

        view.mainMenuBtn1.setOnClickListener {
            switchFragment(AdminUAManager())
        }

        view.mainMenuBtn2.setOnClickListener {
            switchFragment(AdminReport())
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
