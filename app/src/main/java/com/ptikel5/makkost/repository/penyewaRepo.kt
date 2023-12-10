package com.ptikel5.makkost.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.ptikel5.makkost.datacl.KamarCl
import com.ptikel5.makkost.datacl.PenyewaCL

public class penyewaRepo {
    val userid = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference: DatabaseReference =
        userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("penyewa")
        }!!

    @Volatile private var INSTANCE : penyewaRepo ?= null
    fun getInstance() : penyewaRepo{
        return INSTANCE ?: synchronized(this){

            val instance = penyewaRepo()
            INSTANCE = instance
            instance
        }


    }

    fun loadPenyewa(penyewalist : MutableLiveData<List<PenyewaCL>>){

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _penyewalist : List<PenyewaCL> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(PenyewaCL::class.java)!!

                    }

                    penyewalist.postValue(_penyewalist)

                }catch (e : Exception){


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

//    fun loadPenyewa(penyewalist: MutableLiveData<List<KamarCl>>) {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                try {
//                    val _penyewalist: List<PenyewaCL> =
//                        snapshot.children.mapNotNull { dataSnapshot ->
//                            dataSnapshot.getValue(PenyewaCL::class.java)
//                        }
//
//                    penyewalist.value = _penyewalist
//                } catch (e: Exception) {
//                    Log.e("kamarRepo", "Error converting data: ${e.message}")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error if needed
//            }
//        })
//    }

//    fun loadPenyewa(penyewaList: MutableLiveData<List<PenyewaCL>>) {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                try {
//                    val _penyewaList: List<PenyewaCL> = snapshot.children.mapNotNull { dataSnapshot ->
//                        dataSnapshot.getValue(PenyewaCL::class.java)
//                    }
//
//                    penyewaList.value = _penyewaList
//                } catch (e: Exception) {
//                    Log.e("penyewaRepo", "Error converting data: ${e.message}")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("penyewaRepo", "Database error: ${error.message}")
//                // Handle onCancelled if needed
//            }
//        })
//    }
}

