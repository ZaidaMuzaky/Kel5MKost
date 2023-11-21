package com.ptikel5.makkost.Act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailRumahBinding
import com.ptikel5.makkost.datacl.Rumah

class detailRumahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRumahBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailRumahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("idRumah").toString(),
                intent.getStringExtra("namaRumah").toString(),
                intent.getStringExtra("alamatRumah").toString()
            )
        }
        binding.btnDelete.setOnClickListener {
            deletedata(intent.getStringExtra("idRumah").toString())
        }
    }

    private fun deletedata(idRumah: String) {
        database = FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("rumah")
        database.child(idRumah).removeValue().addOnSuccessListener {
            val intent = Intent(this, ListRumActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "berhasil hapus", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "gagal hapus", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initView() {
        binding.tvDtIdRumah.text =intent.getStringExtra("idRumah")
        binding.tvDtnamaRumah.text =intent.getStringExtra("namaRumah")
        binding.tvDtalamatRumah.text =intent.getStringExtra("alamatRumah")
    }

    private fun openUpdateDialog(
        idRumah: String,
        namaRumah: String,
        alamatRumah: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.rumah_updialog, null)

        mDialog.setView(mDialogView)

        val ednamaRumah = mDialogView.findViewById<EditText>(R.id.upd_rumah)
        val edalamatRumah = mDialogView.findViewById<EditText>(R.id.upd_alarumah)
        val btnupdateRumah = mDialogView.findViewById<Button>(R.id.update_rumah)

        ednamaRumah.setText(intent.getStringExtra("namaRumah").toString())
        edalamatRumah.setText(intent.getStringExtra("alamatRumah").toString())

        mDialog.setTitle("Updating data")

        val alertDialog = mDialog.create()
        alertDialog.show()
        btnupdateRumah.setOnClickListener {
            val namaRumah = ednamaRumah.text.toString()
            val alamatRumah = edalamatRumah.text.toString()
            updateRumahData(
                idRumah,
                namaRumah,
                alamatRumah
            )
            alertDialog.dismiss()
        }
    }
    private fun updateRumahData(
        idRumah: String,
        namaRumah: String,
        alamatRumah: String
    ) {
        database = FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("rumah")
        val rumahin = mapOf<String,String>(
            "namaRumah" to namaRumah,
            "alamatRumah" to alamatRumah
        )
       database.child(idRumah).updateChildren(rumahin).addOnSuccessListener {
           Toast.makeText(this, "berhasil edit", Toast.LENGTH_SHORT).show()
       }
           .addOnFailureListener {
               Toast.makeText(this, "gagal edit", Toast.LENGTH_SHORT).show()
           }

    }


}