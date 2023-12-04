package com.ptikel5.makkost.adapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptikel5.makkost.R

import com.ptikel5.makkost.datacl.PenyewaCL

class penyewaAdapt(): RecyclerView.Adapter<penyewaAdapt.MyViewHolder>() {
    private val penyewaList = ArrayList<PenyewaCL>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.penyewalist,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun getItemCount(): Int {
        return penyewaList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentitem = penyewaList[position]

        holder.namaPenyewa.text = currentitem.namaPenyewa
        holder.noHPPenyewa.text = currentitem.noHPPenyewa
        holder.statusPenyewa.text = currentitem.statusPenyewa

    }
    fun updatePenyewalist(kamarlist: List<PenyewaCL>) {

        this.penyewaList.clear()
        this.penyewaList.addAll(penyewaList)
        notifyDataSetChanged()

    }
    class MyViewHolder(itemView: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val namaPenyewa: TextView = itemView.findViewById(R.id.tv_namaPenyewa)
        val noHPPenyewa: TextView = itemView.findViewById(R.id.tv_noHPPenyewa)
        val statusPenyewa: TextView = itemView.findViewById(R.id.tv_statusPenyewa)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }


    }

}