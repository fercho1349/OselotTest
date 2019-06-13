package com.tlilektik.oselottest.view.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import com.tlilektik.oselottest.R
import com.tlilektik.oselottest.utils.CircleTransform
import android.text.Html
import android.widget.Filterable
import android.widget.Filter
import android.widget.ImageView
import com.tlilektik.oselottest.entity.ItemObject


class RecyclerViewAdapter(var itemList: List<ItemObject>, val applicationContext: Context,
                          val mArrayList : List<ItemObject>, val itemListener : RecyclerViewClickListener
) :
    RecyclerView.Adapter<RecyclerViewHolders>(), Filterable {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolders {
        val layoutView = LayoutInflater.from(p0.context).inflate(R.layout.card_view, null)
        return RecyclerViewHolders(layoutView, applicationContext, itemListener)
    }

    override fun getItemCount(): Int {
        return this.itemList.size
    }

    override fun onBindViewHolder(p0: RecyclerViewHolders, p1: Int) {
        val styledText = Html.fromHtml(applicationContext.getString(R.string.name, itemList[p1].name))
        p0.name!!.text = styledText
        Picasso.get().load(itemList[p1].thumbnail).transform(CircleTransform()).into(p0.thumbnail)
        val styledText2 = Html.fromHtml(applicationContext.getString(R.string.age, itemList[p1].age.toString()))
        p0.age!!.text = styledText2
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    itemList = mArrayList
                } else {
                    val filteredList = ArrayList<ItemObject>()
                        for (itemObject in mArrayList) {
                            if (itemObject.name!!.toLowerCase().contains(charString)) {
                                filteredList.add(itemObject)
                            }
                        }
                        itemList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = itemList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                itemList = filterResults.values as ArrayList<ItemObject>
                notifyDataSetChanged()
            }
        }
    }

    interface RecyclerViewClickListener {
        fun recyclerViewListClicked(v: ImageView?, position: Int)
    }

}