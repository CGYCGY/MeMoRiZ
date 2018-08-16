package cgy.memoriz.fragment.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.fragment.third.ThirdPage
import kotlinx.android.synthetic.main.fragment_second_page.view.*

class SecondPage : MainActivityBaseFragment() {

    private var textGet : String ?= null

    /*
     *Put the data u want transfer from the previous fragment into the parameter.
     * Serializable is used to transfer a class of data.
     */
    fun newInstance(text : String) : SecondPage {
        val args = Bundle()
        args.putString("key1", text)
        val fragment = SecondPage()
        fragment.arguments = args
        return fragment
    }
    fun newInstance(): SecondPage{
        val args = Bundle()
        args.putSerializable("transfer data with this", "nothing")
        val fragment = SecondPage()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_second_page, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            textGet = bundle.getSerializable("key2") as String
            //setTitle("$textGet")
            setTitle("$textGet")
        }else {
            setTitle("This is second page.")
        }

        view.secondBtn.setOnClickListener {
            switchFragment(ThirdPage())
        }
        return view
    }
}
