package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.data.UserData
import kotlinx.android.synthetic.main.recent_chat.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private var c: Context
    private var userList: List<UserData>
    private var view: UserAdapterInterface

    constructor(c: Context, list: List<UserData>, view: UserAdapterInterface) {
        this.c = c
        this.userList = list
        this.view = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_chat, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = userList[position]
        holder.tv_name.text = data.name

        holder.itemView?.setOnClickListener {
            view.onClick(data)
        }
    }

    fun setData(list: List<UserData>) {
        this.userList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView

        init {
            tv_name = itemView.tv_name
        }
    }
}