package com.tlilektik.oselottest.view.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tlilektik.oselottest.R

class RecyclerViewHolders(itemView: View, val applicationContext: Context,
                          val itemListener : RecyclerViewAdapter.RecyclerViewClickListener
) :
    RecyclerView.ViewHolder(itemView),View.OnClickListener{

    var name : TextView? = null
    var thumbnail : ImageView? = null
    var age : TextView? = null

    init{
        itemView.setOnClickListener(this)
        name = itemView.findViewById(R.id.name)
        thumbnail = itemView.findViewById(R.id.thumbnail)
        age = itemView.findViewById(R.id.age)
    }

    override fun onClick(v: View?) {
        Toast.makeText(v!!.context, "Clicked Country Position = $position",
            Toast.LENGTH_SHORT).show()

        itemListener.recyclerViewListClicked(thumbnail, layoutPosition)

    }

}