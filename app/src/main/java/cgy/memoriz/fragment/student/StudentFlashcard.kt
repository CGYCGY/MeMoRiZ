package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.data.FCSetData
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_student_fc_back.view.*
import kotlinx.android.synthetic.main.fragment_student_fc_front.view.*
import kotlinx.android.synthetic.main.fragment_student_flashcard.view.*

class StudentFlashcard : MainActivityBaseFragment() {

    fun newInstance(fcSetInfo: FCSetData): StudentFlashcard{
        val args = Bundle()
        args.putSerializable("flashcard set detail", fcSetInfo)
        val fragment = StudentFlashcard()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_flashcard, container, false)
        /*
         * Get the data from previous fragment
         */
        val bundle = arguments
        if (bundle != null) {
            val fcSet : FCSetData = bundle.getSerializable("flashcard set detail") as FCSetData

            setTitle(fcSet.name.toString())
        }else {
            Log.e("error", "missing flashcard set")
            setTitle("Flashcard")
        }

//        loadFCSetList()

        view.fc_image1.visibility = GONE
        view.fc_image2.visibility = GONE

        view.fc_back.visibility = INVISIBLE

        view.fc_front.setOnClickListener {
            if (view.fc_back.visibility == INVISIBLE)
                view.fc_back.visibility = VISIBLE

            view.flashcard.flipTheView()
        }

        view.fc_back.setOnClickListener {
            view.flashcard.flipTheView()
        }

//        view.flashcard.setOnFlipListener { easyFlipView, newCurrentSide ->
//            Toast.makeText(context, "Flip Completed! New Side is: " + newCurrentSide, Toast.LENGTH_LONG).show()
//        }

        return view
    }
}