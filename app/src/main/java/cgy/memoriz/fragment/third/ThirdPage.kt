package cgy.memoriz
        .fragment.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.fragment.StudentMainMenu
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_home_page.view.*

//this is use bac 1st page things and change some information nia
class ThirdPage : MainActivityBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        setTitle("ThirdPage!")
        view.studentProfileBtn.text = "Back to home"
        view.studentProfileBtn.setOnClickListener {
            switchFragment(StudentMainMenu())
        }
        return view
    }

}
