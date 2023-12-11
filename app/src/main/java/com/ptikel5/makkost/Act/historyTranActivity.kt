package com.ptikel5.makkost.Act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.adapt.hisAdapt
import com.ptikel5.makkost.adapt.rumAdapt
import com.ptikel5.makkost.databinding.ActivityHistoryTranBinding
import com.ptikel5.makkost.datacl.historyCL

class historyTranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryTranBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var hisarraylist: ArrayList<historyCL>
    lateinit var recyclerViewhistory: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryTranBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hisarraylist = arrayListOf<historyCL>()
        binding.hislist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.hislist.adapter = hisAdapt(hisarraylist)
        recyclerViewhistory = binding.hislist

        getdatarum()

    }

    private fun getdatarum() {
        val idtran = intent.getStringExtra("idTransaksi").toString()
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        dbref =
            userid?.let {
                FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference(
                        it
                    ).child("history").child(idtran)
            }!!

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    hisarraylist.clear()
                    for (userSnapshot in snapshot.children) {


                        val his = userSnapshot.getValue(historyCL::class.java)

                        hisarraylist.add(his!!)


                    }
                    //                  binding.rumList.adapter = rumAdapt(rumArrayList)
                    binding.hislist.adapter?.notifyDataSetChanged()
                    val mAdapter = hisAdapt(hisarraylist)
                    recyclerViewhistory.adapter = mAdapter

//                    mAdapter.setOnItemClickListener(object : rumAdapt.onItemClickListener {
//                        override fun onItemClick(position: Int) {
//
//                            val intent = Intent(this@ListRumActivity, detailRumahActivity::class.java)
//
//                            intent.putExtra("idRumah", rumArrayList[position].idRumah)
//                            intent.putExtra("namaRumah", rumArrayList[position].namaRumah)
//                            intent.putExtra("alamatRumah", rumArrayList[position].alamatRumah)
//
//
//                            startActivity(intent)
//                        }
//
//
//                    })
//
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}