package com.ptikel5.makkost.Act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ptikel5.makkost.PengaturanFragment
import com.ptikel5.makkost.databinding.ActivityRumahBinding
import com.ptikel5.makkost.datacl.Rumah

class RumahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRumahBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRumahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser?.uid
        database = user?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            )
        }!!


        binding.smpRumah.setOnClickListener {
            simpandata()
        }
        
        binding.btl.setOnClickListener {
            finish()
        }

    }
    private fun simpandata() {
        val idRumah = database.push().key!!
        val namaRumah = binding.inpNamar.text.toString()
        val alamatRumah = binding.inpAlamat.text.toString()

        val dataRumah = Rumah(idRumah, namaRumah, alamatRumah)
        database.child("rumah").child(idRumah).setValue(dataRumah).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahakan data rumah", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data rumah", Toast.LENGTH_SHORT).show()
        }
    }
}