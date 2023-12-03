package com.ptikel5.makkost.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.datacl.KamarCl


public class kamarRepo( val passfilter: String = "Rumah ABC") {


    val databaseReference : DatabaseReference = FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("rumah").child(passfilter).child("kamar")


    @Volatile private var INSTANCE : kamarRepo ?= null
    fun getInstance() : kamarRepo{
        return INSTANCE ?: synchronized(this){

            val instance = kamarRepo(passfilter)
            INSTANCE = instance
            instance
        }


    }


//    fun loadKamar(kamarlist: LiveData<List<KamarCl>>){
//
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                try {
//
//                    val _kamarlist : List<KamarCl> = snapshot.children.map { dataSnapshot ->
//
//                        dataSnapshot.getValue(KamarCl::class.java)!!
//
//                    }
//
//                    kamarlist.postValue(_kamarlist)
//
//                }catch (e : Exception){
//
//
//                }
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//        })
//
//
//    }

    fun loadKamar(kamarlist: MutableLiveData<List<KamarCl>>) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _kamarlist: List<KamarCl> = snapshot.children.mapNotNull { dataSnapshot ->
                        dataSnapshot.getValue(KamarCl::class.java)
                    }

                    // Gunakan setValue langsung
                    kamarlist.value = _kamarlist
                } catch (e: Exception) {
                    // Handle exception, misalnya, log pesan kesalahan
                    Log.e("kamarRepo", "Error converting data: ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}