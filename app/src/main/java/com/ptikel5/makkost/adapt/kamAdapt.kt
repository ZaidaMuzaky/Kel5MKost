package com.ptikel5.makkost.adapt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptikel5.makkost.R
import com.ptikel5.makkost.datacl.KamarCl

class kamAdapt : RecyclerView.Adapter<kamAdapt.MyViewHolder>() {

    private val kamarList = ArrayList<KamarCl>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.kamarlist,
            parent,false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = kamarList[position]

        holder.noKamar.text = currentitem.noKamar
        holder.statusKamar.text = currentitem.statusKamar
        holder.biayaKamar.text = currentitem.biayaKamar

    }

    override fun getItemCount(): Int {
        return kamarList.size
    }

    fun updatekamarlist(kamarlist : List<KamarCl>){

        this.kamarList.clear()
        this.kamarList.addAll(kamarlist)
        notifyDataSetChanged()

    }

    class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val noKamar : TextView = itemView.findViewById(R.id.tv_noKamar)
        val statusKamar : TextView = itemView.findViewById(R.id.tv_status)
        val biayaKamar : TextView = itemView.findViewById(R.id.tv_harga)

    }



}