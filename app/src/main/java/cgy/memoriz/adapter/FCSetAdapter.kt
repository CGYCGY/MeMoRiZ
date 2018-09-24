package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.FCSetData
import kotlinx.android.synthetic.main.base_list_flashcardset.view.*

class FCSetAdapter : RecyclerView.Adapter<FCSetAdapter.ViewHolder> {


    private var c: Context
    private var fcSetList : List<FCSetData>
    private var fcSetListView : FCSetAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<FCSetData>, fcSetListView : FCSetAdapterInterface) {
        this.c = c
        this.fcSetList = dbList
        this.fcSetListView = fcSetListView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_flashcardset, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = fcSetList[position]
        holder.name.text = data.name
        holder.size.text = "Flashcard Count: " + data.size.toString()

        holder.itemView?.setOnClickListener(View.OnClickListener {
            fcSetListView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            fcSetListView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return fcSetList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var size: TextView

        init {
            name = itemView.fcset_name
            size = itemView.fcset_size
        }
    }
}