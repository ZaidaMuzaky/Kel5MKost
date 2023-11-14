package com.ptikel5.makkost.adapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptikel5.makkost.R
import com.ptikel5.makkost.datacl.Rumah

class rumAdapt(private val rumList : ArrayList<Rumah>) : RecyclerView.Adapter<rumAdapt.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rumahlist,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = rumList[position]

        holder.namrum.text = currentitem.namaRumah
        holder.alrum.text = currentitem.alamatRumah

    }

    override fun getItemCount(): Int {

        return rumList.size
    }


    class MyViewHolder(itemView: View,clickListener: onItemClickListener ) : RecyclerView.ViewHolder(itemView) {

        val namrum: TextView = itemView.findViewById(R.id.tvnamrum)
        val alrum: TextView = itemView.findViewById(R.id.tvrumal)

        init {
            itemView.setOnClickListener {
               clickListener.onItemClick(adapterPosition)
            }
        }
    }


}
