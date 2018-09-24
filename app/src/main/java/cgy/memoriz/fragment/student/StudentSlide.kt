package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.data.SetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.base_list_slide.view.*
import kotlinx.android.synthetic.main.fragment_student_slide.view.*

class StudentSlide : MainActivityBaseFragment() {
    private var slideSet = SetData()
    private var currentPosition = 0

    fun newInstance(fcSetInfo: SetData): StudentSlide{
        val args = Bundle()
        args.putSerializable("slide set detail", fcSetInfo)
        val fragment = StudentSlide()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_slide, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            slideSet = bundle.getSerializable("slide set detail") as SetData

            setTitle(slideSet.name.toString())
        }else {
            Log.e("error", "missing slide set")
            setTitle("Slide")
        }

        view.previousSlideBtn.visibility = INVISIBLE
        view.slide_content.text = slideSet.slideList[0].content

        view.previousSlideBtn.setOnClickListener {
            setupSlide(false)
        }

        view.nextSlideBtn.setOnClickListener {
            setupSlide(true)
        }

        return view
    }

    private fun setupSlide(side : Boolean) {
        if (side) currentPosition++
        else currentPosition--

        if (currentPosition == 0) {
            view!!.previousSlideBtn.visibility = INVISIBLE
            view!!.nextSlideBtn.visibility = VISIBLE
        } else if (currentPosition == slideSet.slideList.size-1) {
            view!!.previousSlideBtn.visibility = VISIBLE
            view!!.nextSlideBtn.visibility = INVISIBLE
        } else {
            view!!.previousSlideBtn.visibility = VISIBLE
            view!!.nextSlideBtn.visibility = VISIBLE
        }

        view!!.slide_content.text = slideSet.slideList[currentPosition].content
    }
}