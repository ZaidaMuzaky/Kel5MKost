package com.ptikel5.makkost.Act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailRumahBinding
import com.ptikel5.makkost.datacl.Rumah

class detailRumahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRumahBinding
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

        val ednamaRumah = mDialogView.findViewById<TextInputEditText>(R.id.update_namaR)
        val edalamatRumah = mDialogView.findViewById<TextInputEditText>(R.id.update_alamatR)
        val btnupdateRumah = mDialogView.findViewById<Button>(R.id.update_rumah)

        ednamaRumah.setText(intent.getStringExtra("namaRumah").toString())
        edalamatRumah.setText(intent.getStringExtra("alamatRumah").toString())

        mDialog.setTitle("Updating data")

        val alertDialog = mDialog.create()
        alertDialog.show()
        btnupdateRumah.setOnClickListener {
            updateRumahData(
                idRumah,
                ednamaRumah.text.toString(),
                edalamatRumah.text.toString()
            )

            Toast.makeText(applicationContext, "Data Rumah ter update", Toast.LENGTH_LONG).show()

            binding.tvDtnamaRumah.text = ednamaRumah.toString()
            binding.tvDtalamatRumah.text = edalamatRumah.toString()

            alertDialog.dismiss()
        }
    }
    private fun updateRumahData(
        idRumah: String,
        namaRumah: String,
        alamatRumah: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("rumah").child(namaRumah)
        val rumahin = Rumah(idRumah, namaRumah, alamatRumah)
        dbRef.setValue(rumahin)
    }


}