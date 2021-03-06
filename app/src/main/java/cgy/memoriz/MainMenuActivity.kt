package cgy.memoriz

import android.os.Bundle
import cgy.memoriz.fragment.LecturerMainMenu
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.others.EventBus
import kotlinx.android.synthetic.main.main_navigation.*
import kotlinx.android.synthetic.main.main_navigation_header.view.*

//extend main menu base to user the drawer
class MainMenuActivity : MainMenuBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharedPref.init(this)
        SharedPref.arrow = false
        nav_view.getHeaderView(0).userName.text = SharedPref.userName
        EventBus().initBus()

        //Init the view of Home fragment.
        if (SharedPref.userType == "Student")
            replaceContainerFragment(StudentMainMenu())
        else if (SharedPref.userType == "Lecturer")
            replaceContainerFragment(LecturerMainMenu())
    }
}

