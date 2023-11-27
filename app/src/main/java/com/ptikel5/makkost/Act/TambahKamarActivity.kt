package com.ptikel5.makkost.Act

import android.R
import android.R.layout.simple_spinner_item
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.PengaturanFragment
import com.ptikel5.makkost.databinding.ActivityTambahKamarBinding
import com.ptikel5.makkost.datacl.KamarCl


class TambahKamarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahKamarBinding
    private lateinit var database: DatabaseReference
    private  val namaRumah: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahKamarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("rumah")

        // button form
        binding.smpKamar.setOnClickListener {
            simpandatakamar()
        }

        binding.btl.setOnClickListener {
            finish()
        }

        // spinner nama rumah
        val dataAdapter = ArrayAdapter<String>(this, simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.inpRumah.setAdapter(dataAdapter)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataAdapter.clear()
                for (ds in snapshot.children) {
                    val name = ds.child("namaRumah").value.toString()
                    dataAdapter.add(name)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // spinner status
        val statusinput: ArrayList<String> = InputStatus()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, simple_spinner_item, statusinput)
        binding.inpStatus.setAdapter(adapter)
    }

    private fun simpandatakamar() {
        val idKamar = database.push().key!!
        val idRumah = binding.inpRumah.text.toString()
        val noKamar = binding.inpNokamar.text.toString()
        val biayaKamar = binding.inpBiaya.text.toString()
        val fasilitasKamar = binding.inpFasilitas.text.toString()
        val statusKamar = binding.inpStatus.text.toString()

        val dataKamar = KamarCl(idKamar,idRumah, noKamar, biayaKamar, fasilitasKamar, statusKamar)
        database.child(idRumah).child("kamar").child(noKamar).setValue(dataKamar).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahkan Data Kamar", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, KamarFragment::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data Kamar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun InputStatus(): ArrayList<String> {
        val status = ArrayList<String>()
        status.add("Kosong")
        status.add("Terisi")
        status.add("Perbaikan")

        return status
    }
}