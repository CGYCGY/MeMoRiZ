package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.SetData
import kotlinx.android.synthetic.main.base_list_slide_set.view.*

class SlideSetAdapter : RecyclerView.Adapter<SlideSetAdapter.ViewHolder> {


    private var context: Context
    private var slideSetList : List<SetData>
    private var slideSetView : QuizSetAdapterInterface
    var count = 1

    constructor(context: Context, dbList: List<SetData>, slideSetView : QuizSetAdapterInterface) {
        this.context = context
        this.slideSetList = dbList
        this.slideSetView = slideSetView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_slide_set, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = slideSetList[position]
        val slideSetSize = "Slide Set Size: " + data.size.toString()
        Log.e("ssssssss", slideSetSize)
        holder.name.text = data.name
        holder.size.text = slideSetSize

        holder.itemView?.setOnClickListener {
            slideSetView.onClick(data)
        }

        holder.itemView?.setOnLongClickListener {
            slideSetView.onLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int {
        return slideSetList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var size: TextView

        init {
            name = itemView.slide_set_name
            size = itemView.slide_set_size
        }
    }
}