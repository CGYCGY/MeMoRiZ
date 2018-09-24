package cgy.memoriz.fragment.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cgy.memoriz.R
import cgy.memoriz.fragment.MainActivityBaseFragment
import kotlinx.android.synthetic.main.fragment_create_flashcard_mode.view.*

class CreateFlashcardMode : MainActivityBaseFragment() {
    fun newInstance(text : String) : CreateFlashcardMode {
        val args = Bundle()
        args.putString("id", text)
        val fragment = CreateFlashcardMode()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_flashcard_mode, container, false)
        /*
         * Get the data from previous fragment
         */
        var fcsID = ""
        val bundle = arguments
        if (bundle != null) {
            fcsID = bundle.getString("id")

            setTitle("Choose a flashcard mode")

            view.flashcard_mode1.setOnClickListener {
                switchFragment(CreateFlashcard().newInstance(fcsID, "1010"))
            }

            view.flashcard_mode2.setOnClickListener {
                switchFragment(CreateFlashcard().newInstance(fcsID, "0101"))
            }

            view.flashcard_mode3.setOnClickListener {
                switchFragment(CreateFlashcard().newInstance(fcsID, "0110"))
            }

            view.flashcard_mode4.setOnClickListener {
                switchFragment(CreateFlashcard().newInstance(fcsID, "1001"))
            }

        }else {
            Log.e("error", "bundle data missing")
        }

        return view
    }
}