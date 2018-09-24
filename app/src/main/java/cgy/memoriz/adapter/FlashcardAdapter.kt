package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.FlashcardData
import kotlinx.android.synthetic.main.base_list_flashcard.view.*

class FlashcardAdapter : RecyclerView.Adapter<FlashcardAdapter.ViewHolder> {


    private var c: Context
    private var flashcardList : List<FlashcardData>
    private var flashcardView : FlashcardAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<FlashcardData>, flashcardView : FlashcardAdapterInterface) {
        this.c = c
        this.flashcardList = dbList
        this.flashcardView = flashcardView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_flashcard, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = flashcardList[position]
        holder.card1.text = data.card1
        holder.card2.text = data.card2
        holder.image1.visibility = GONE
        holder.image2.visibility = GONE

        //onhold image flashcard
//        val file1 = File(Environment.getExternalStorageDirectory().path + "/" + data.card1)
//        val file2 = File(Environment.getExternalStorageDirectory().path + "/" + data.card2)
//
//        if (file1.exists()) {
//            val uri = Uri.fromFile(file1)
//
//            spDialog.showDialog()
//
//            //load image from phone storage
//            Picasso.get()
//                    .load(uri)
//                    .into(view!!.profile_show_photo)
//
//            spDialog.hideDialog()
//        }
//        else {
//            spDialog.showDialog()
//
//            //get image from server and show it out
//            Picasso.get()
//                    .load(url)
//                    .into(view!!.profile_show_photo)
//
//            spDialog.hideDialog()
//
//            //save to phone storage
//            Picasso.get()
//                    .load(url)
//                    .into(JExtension.getTarget(imageName))
//        }

        holder.itemView?.setOnClickListener(View.OnClickListener {
            flashcardView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            flashcardView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return flashcardList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card1 : TextView
        var card2 : TextView
        var image1 : ImageView
        var image2 : ImageView

        init {
            card1 = itemView.flashcard_card1
            card2 = itemView.flashcard_card2
            image1 = itemView.flashcard_image1
            image2 = itemView.flashcard_image2
        }
    }
}