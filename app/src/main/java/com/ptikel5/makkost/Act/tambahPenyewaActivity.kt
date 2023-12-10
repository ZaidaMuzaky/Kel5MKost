package com.ptikel5.makkost.Act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ptikel5.makkost.PengaturanFragment
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailKamarBinding
import com.ptikel5.makkost.databinding.ActivityTambahPenyewaBinding
import com.ptikel5.makkost.datacl.PenyewaCL

class tambahPenyewaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPenyewaBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahPenyewaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("penyewa")
        }!!

        binding.smpPenyewa.setOnClickListener {
            simpandata()
        }
        binding.btl.setOnClickListener {
            finish()
        }

        val statusPenyewaInput: ArrayList<String> = InputStatusPenyewa()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusPenyewaInput)
        binding.inpStatusPenyewa.setAdapter(adapter)

    }
    private fun simpandata(){
        val idPenyewa = database.push().key!!
        val namaPenyewa = binding.inpNamaPenyewa.text.toString()
        val alamatPenyewa = binding.inpAlamatPeneyewa.text.toString()
        val noHPPenyewa = binding.inpNoHpPenyewa.text.toString()
        val pekerjaanPenyewa = binding.inpPekerjaanPenyewa.text.toString()
        val emailPenyewa = binding.inpEmailPenyewa.text.toString()
        val statusPenyewa = binding.inpStatusPenyewa.text.toString()

        val dataPenyewa = PenyewaCL(idPenyewa, namaPenyewa, alamatPenyewa, noHPPenyewa, pekerjaanPenyewa, emailPenyewa, statusPenyewa)
        database.child(idPenyewa).setValue(dataPenyewa).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahakan data Penyewa", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data Penyewa", Toast.LENGTH_SHORT).show()
        }

    }
    private fun InputStatusPenyewa(): ArrayList<String> {
        val status = ArrayList<String>()
        status.add("Aktif")
        status.add("Tidak Aktif")

        return status
    }
}