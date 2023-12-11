package com.ptikel5.makkost.Act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailPenyewaBinding

class detailPenyewaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPenyewaBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailPenyewaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("idPenyewa").toString(),
                intent.getStringExtra("namaPenyewa").toString(),
                intent.getStringExtra("alamatPenyewa").toString(),
                intent.getStringExtra("noHPPenyewa").toString(),
                intent.getStringExtra("pekerjaanPenyewa").toString(),
                intent.getStringExtra("emailPenyewa").toString(),
                intent.getStringExtra("statusPenyewa").toString(),
            )
        }
        binding.btnDelete.setOnClickListener {
            deletedata(intent.getStringExtra("idPenyewa").toString())
        }
    }
    private fun deletedata(idPenyewa: String,) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("penyewa")
        }!!
        database.child(idPenyewa).removeValue().addOnSuccessListener {
            Toast.makeText(this, "berhasil hapus", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "gagal hapus", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openUpdateDialog(
        idPenyewa : String,
        namaPenyewa : String,
        alamatPenyewa : String,
        noHPPenyewa : String,
        pekerjaanPenyewa : String,
        emailPenyewa : String,
        statusPenyewa : String) {

        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.penyewa_updialog, null)

        mDialog.setView(mDialogView)

        val ednamaPenyewa = mDialogView.findViewById<EditText>(R.id.upd_namaPenyewa)
        val edalamatPenyewa = mDialogView.findViewById<EditText>(R.id.upd_alamatPenyewa)
        val ednoHpPenyewa = mDialogView.findViewById<EditText>(R.id.upd_noHPPenyewa)
        val edPekerjaanPenyewa = mDialogView.findViewById<EditText>(R.id.upd_pekerjaanPenyewa)
        val edemailPenyewa = mDialogView.findViewById<EditText>(R.id.upd_emailPenyewa)
        val edstatusPenyewa = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_statusPenyewa)
        val btnupdatePenyewa = mDialogView.findViewById<Button>(R.id.update_Penyewa)

        val statusPenyewaInput: ArrayList<String> = InputStatusPenyewa()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusPenyewaInput)
        edstatusPenyewa.setAdapter(adapter)

        ednamaPenyewa.setText(intent.getStringExtra("namaPenyewa").toString())
        edalamatPenyewa.setText(intent.getStringExtra("alamatPenyewa").toString())
        ednoHpPenyewa.setText(intent.getStringExtra("noHPPenyewa").toString())
        edPekerjaanPenyewa.setText(intent.getStringExtra("pekerjaanPenyewa").toString())
        edemailPenyewa.setText(intent.getStringExtra("emailPenyewa").toString())
        edstatusPenyewa.setText(intent.getStringExtra("statusPenyewa").toString())

        mDialog.setTitle("sedang update data")

        val alertDialog = mDialog.create()
        alertDialog.show()
        btnupdatePenyewa.setOnClickListener {
            val namaPenyewa = ednamaPenyewa.text.toString()
            val alamatPenyewa = edalamatPenyewa.text.toString()
            val noHPPenyewa = ednoHpPenyewa.text.toString()
            val pekerjaanPenyewa = edPekerjaanPenyewa.text.toString()
            val emailPenyewa = edemailPenyewa.text.toString()
            val statusPenyewa = edstatusPenyewa.text.toString()

            updatePenyewaData(
                idPenyewa,
                namaPenyewa,
                alamatPenyewa,
                noHPPenyewa,
                pekerjaanPenyewa,
                emailPenyewa,
                statusPenyewa

                )
            alertDialog.dismiss()
        }
    }
    private fun updatePenyewaData(
        idPenyewa: String,
        namaPenyewa: String,
        alamatPenyewa: String,
        noHPPenyewa: String,
        pekerjaanPenyewa: String,
        emailPenyewa: String,
        statusPenyewa: String
    ){
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("Penyewa")
        }!!
        val penyewain = mapOf<String, String>(
            "namaPenyewa" to namaPenyewa,
            "alamatPenyewa" to alamatPenyewa,
            "noHPPenyewa" to noHPPenyewa,
            "pekerjaanPenyewa" to pekerjaanPenyewa,
            "emailPenyewa" to emailPenyewa,
            "statusPenyewa" to statusPenyewa,

        )
        database.child(idPenyewa).updateChildren(penyewain).addOnCompleteListener {
            Toast.makeText(this, "berhasil edit", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "gagal edit", Toast.LENGTH_SHORT).show()
            }
    }
    private fun InputStatusPenyewa(): ArrayList<String> {
        val status = ArrayList<String>()
        status.add("Aktif")
        status.add("Tidak Aktif")

        return status
    }
    private fun initView() {
        binding.tvDtIdPenyewa.text = intent.getStringExtra("idPenyewa")
        binding.tvDtnamaPenyewa.text = intent.getStringExtra("namaPenyewa")
        binding.tvDtalamatPenyewa.text = intent.getStringExtra("alamatPenyewa")
        binding.tvDtnoHPPenyewa.text = intent.getStringExtra("noHPPenyewa")
        binding.tvDtPekerjaanPenyewa.text = intent.getStringExtra("pekerjaanPenyewa")
        binding.tvDtemailPenyewa.text = intent.getStringExtra("emailPenyewea")
        binding.tvDtstatusPenyewa.text = intent.getStringExtra("statusPenyewa")
    }
}