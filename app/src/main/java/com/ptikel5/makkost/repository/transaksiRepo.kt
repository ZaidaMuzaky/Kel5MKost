package com.ptikel5.makkost.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.datacl.PenyewaCL
import com.ptikel5.makkost.datacl.transaksiCL

public class transaksiRepo {
    val userid = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference: DatabaseReference =
        userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("transaksi")
        }!!

    @Volatile private var INSTANCE : transaksiRepo ?= null
    fun getInstance() : transaksiRepo{
        return INSTANCE ?: synchronized(this){

            val instance = transaksiRepo()
            INSTANCE = instance
            instance
        }


    }
    fun loadTransaksi(transaksilist : MutableLiveData<List<transaksiCL>>){

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _transaksilist : List<transaksiCL> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(transaksiCL::class.java)!!

                    }

                    transaksilist.postValue(_transaksilist)

                }catch (e : Exception){


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }
}