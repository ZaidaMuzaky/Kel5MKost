package com.ptikel5.makkost.Act

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.R
import com.ptikel5.makkost.adapt.rumAdapt
import com.ptikel5.makkost.databinding.ActivityListRumBinding
import com.ptikel5.makkost.databinding.ActivityLoginBinding
import com.ptikel5.makkost.datacl.Rumah

class ListRumActivity : AppCompatActivity() {
    lateinit var binding: ActivityListRumBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var rumArrayList: ArrayList<Rumah>
    lateinit var recyclerViewRumah : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListRumBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rumArrayList = arrayListOf<Rumah>()
        binding.rumList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        binding.rumList.adapter = rumAdapt(rumArrayList)
        recyclerViewRumah = binding.rumList

        getdatarum()
    }

    private fun getdatarum() {

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        dbref =
            userid?.let {
                FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                    it
                ).child("rumah")
            }!!

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    rumArrayList.clear()
                    for (userSnapshot in snapshot.children) {


                        val his = userSnapshot.getValue(Rumah::class.java)

                        rumArrayList.add(his!!)


                    }
//                  binding.rumList.adapter = rumAdapt(rumArrayList)
                    binding.rumList.adapter?.notifyDataSetChanged()
                    val mAdapter = rumAdapt(rumArrayList)
                    recyclerViewRumah.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : rumAdapt.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@ListRumActivity, detailRumahActivity::class.java)

                            intent.putExtra("idRumah", rumArrayList[position].idRumah)
                            intent.putExtra("namaRumah", rumArrayList[position].namaRumah)
                            intent.putExtra("alamatRumah", rumArrayList[position].alamatRumah)


                            startActivity(intent)
                        }


                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }



}