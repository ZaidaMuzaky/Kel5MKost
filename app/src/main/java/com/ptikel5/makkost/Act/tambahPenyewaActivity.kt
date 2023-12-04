package com.ptikel5.makkost.Act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailKamarBinding
import com.ptikel5.makkost.databinding.ActivityTambahPenyewaBinding

class tambahPenyewaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPenyewaBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahPenyewaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Penyewa")


    }
    private fun simpandata(){
        val idPenyewa = database.push().key!!

    }
}