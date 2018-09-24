package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.SlideData
import kotlinx.android.synthetic.main.base_list_slide.view.*

class SlideAdapter : RecyclerView.Adapter<SlideAdapter.ViewHolder> {


    private var context: Context
    private var slideList : List<SlideData>
    private var slideView : SlideAdapterInterface
    var count = 1

    constructor(context: Context, dbList: List<SlideData>, slideView : SlideAdapterInterface) {
        this.context = context
        this.slideList = dbList
        this.slideView = slideView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_slide, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = slideList[position]
        holder.content.text = data.content

        holder.itemView?.setOnClickListener {
            slideView.onClick(data)
        }

        holder.itemView?.setOnLongClickListener {
            slideView.onLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int {
        return slideList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView

        init {
            content = itemView.slide_content
        }
    }
}