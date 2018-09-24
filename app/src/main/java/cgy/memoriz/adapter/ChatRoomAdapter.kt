package cgy.memoriz.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.data.MessageData
import kotlinx.android.synthetic.main.chat_room.view.*

class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {


    private var c: Context
    private var message: List<MessageData>

    constructor(c: Context, message: List<MessageData>) {
        this.c = c
        this.message = message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_room, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = message[position]
        val chat = "[" + data.name + "]\n" + data.message
        holder.tv_chat_box.text = chat
        holder.tv_time.text=data.time
        /*Use share preference here , to get sender id , or else u can pass sender id th constructor*/
        if(data.senderID == SharedPref.userEmail){
            holder.tv_chat_box.text = data.message
            holder.tv_chat_box.gravity= Gravity.END
            holder.ll_main.gravity=Gravity.END
            holder.ll_chat.setBackgroundResource(R.drawable.chat_box_bg_right)
        }
    }

    override fun getItemCount(): Int {
        return message.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_chat_box: TextView
        var tv_time : TextView
        var ll_main : LinearLayout
        var ll_chat : LinearLayout

        init {
            tv_chat_box = itemView.tv_chat_box
            tv_time = itemView.tv_time
            ll_main = itemView.ll_main
            ll_chat = itemView.ll_chat
        }
    }
}