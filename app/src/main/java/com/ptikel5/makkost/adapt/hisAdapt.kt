package com.ptikel5.makkost.adapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptikel5.makkost.R
import com.ptikel5.makkost.datacl.historyCL

class hisAdapt(private val hisList : ArrayList<historyCL>) : RecyclerView.Adapter<hisAdapt.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.historylist,
            parent, false
        )
        return MyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = hisList[position]

        holder.nokamar.text = currentitem.idKamar
        holder.namaPenyewa.text = currentitem.idPenyewa
        holder.tanggalMasuk.text = currentitem.tanggalMasuk
        holder.tanggalKeluar.text = currentitem.tanggalKeluar

    }
    override fun getItemCount(): Int {

        return hisList.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nokamar: TextView = itemView.findViewById(R.id.tv_Pnokamar)
        val namaPenyewa: TextView = itemView.findViewById(R.id.tv_Pnamapenyewa)
        val tanggalMasuk: TextView = itemView.findViewById(R.id.tv_tangggalMasukhis)
        val tanggalKeluar: TextView = itemView.findViewById(R.id.tv_tangggalkeluarhis)


    }
}