package com.ptikel5.makkost.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptikel5.makkost.R
import com.ptikel5.makkost.datacl.transaksiCL


class tranAdapt() : RecyclerView.Adapter<tranAdapt.MyViewHolder>() {
    private val transaksiList = ArrayList<transaksiCL>()
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.pembayaranlist,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = transaksiList[position]

        holder.noKamartrx.text = currentitem.idKamar
        holder.namaPenyewatrx.text = currentitem.idPenyewa
        holder.tanggalMasukPenyewatrx.text = currentitem.tanggalMasuk

    }

    override fun getItemCount(): Int {
        return transaksiList.size
    }
    fun updateTransaksiList(transaksiList: List<transaksiCL>) {

        this.transaksiList.clear()
        this.transaksiList.addAll(transaksiList)
        notifyDataSetChanged()

    }
    class MyViewHolder(itemView: android.view.View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val noKamartrx: TextView = itemView.findViewById(R.id.tv_Pnokamar)
        val namaPenyewatrx: TextView = itemView.findViewById(R.id.tv_Pnamapenyewa)
        val tanggalMasukPenyewatrx: TextView = itemView.findViewById(R.id.tv_tangggalMasuk)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}