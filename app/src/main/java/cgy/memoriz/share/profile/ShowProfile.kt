package cgy.memoriz.share.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_profile_show.view.*

class ShowProfile : MainActivityBaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_show, container, false)

        setTitle("Profile")

        view.editProfileBtn.setOnClickListener {
            switchFragment(EditProfile())
        }


        return view
    }
}