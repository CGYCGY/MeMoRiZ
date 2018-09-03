package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.ClassData
import kotlinx.android.synthetic.main.base_list_class.view.*

class ClassListAdapter : RecyclerView.Adapter<ClassListAdapter.ViewHolder> {


    private var c: Context
    private var classList : List<ClassData>
    private var classListView : ClassListAdapterInterface
    var count = 1

    constructor(c: Context, dbList: List<ClassData>, classListView : ClassListAdapterInterface) {
        this.c = c
        this.classList = dbList
        this.classListView = classListView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.base_list_class, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = classList[position]
        holder.owner.text = data.owner
        holder.name.text = data.name
        holder.size.text = data.size.toString()

        holder.itemView?.setOnClickListener(View.OnClickListener {
            classListView.onClick(data)
        })

        holder.itemView?.setOnLongClickListener(View.OnLongClickListener {
            classListView.onLongClick(data)
            true
        })
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var owner: TextView
        var name: TextView
        var size: TextView

        init {
            owner = itemView.class_owner
            name = itemView.class_name
            size = itemView.class_size
        }
    }
}