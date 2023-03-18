package com.example.firebaseapp.AdapterHolder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseapp.DetailActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.dataClass.DataClass


class CardAdaptapter(private val context: Context, private var dataList: List<DataClass>) : RecyclerView.Adapter<CardAdaptapter.CardViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return CardViewHolder(view)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    fun searchDataList(searchList: ArrayList<DataClass>) {
        dataList = searchList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        Glide.with(context!!).load(dataList?.get(position)!!.dataImage).into(holder.recImage)
        holder.recTitle.text = dataList!![position].dataTitle
        holder.recDesc.text = dataList!![position].dataDesc
        holder.recLang.text = dataList!![position].dataLang
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("Image", dataList!![holder.adapterPosition].dataImage)
                putExtra("Description", dataList!![holder.adapterPosition].dataDesc)
                putExtra("Title", dataList!![holder.adapterPosition].dataTitle)
                putExtra("Key", dataList!![holder.adapterPosition].key)
                putExtra("Language", dataList!![holder.adapterPosition].dataLang)
            }
            context.startActivity(intent)
        }


    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recImage: ImageView = itemView.findViewById(R.id.recImage)
        val recTitle: TextView = itemView.findViewById(R.id.recTitle)
        val recDesc: TextView = itemView.findViewById(R.id.recDesc)
        val recLang: TextView = itemView.findViewById(R.id.recPriority)
        val recCard: CardView = itemView.findViewById(R.id.recCard)
    }
}
