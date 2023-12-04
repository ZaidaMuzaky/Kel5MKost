package com.ptikel5.makkost.Act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailRumahBinding

class detailKamarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRumahBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailRumahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        binding.btnUpdate.setOnClickListener {

        }

    }

    private fun openUpdateDialog(
        idKamar : String,
        idRumah : String,
        noKamar : String,
        biayaKamar : String,
        fasilitasKamar : String,
        statusKamar : String) {

        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.kamar_updialog, null)

        mDialog.setView(mDialogView)

        val edNoKamar = mDialogView.findViewById<EditText>(R.id.upd_NoKamar)
        val edNamaRumah = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_namaRumah)
        val edBiayaKamar = mDialogView.findViewById<EditText>(R.id.upd_BiayaKamar)
        val edFasilitasKamar = mDialogView.findViewById<EditText>(R.id.upd_FasilitasKamar)
        val edStatusKamar = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_statusKamar)
        val btnUpdateKamar = mDialogView.findViewById<Button>(R.id.update_kamar)

        edNoKamar.setText(intent.getStringExtra("noKamar").toString())
        edNamaRumah.setText(intent.getStringExtra("idRumah").toString())
        edBiayaKamar.setText(intent.getStringExtra("biayaKamar").toString())
        edFasilitasKamar.setText(intent.getStringExtra("fasilitasKamar").toString())
        edStatusKamar.setText(intent.getStringExtra("statusKamar").toString())

        mDialog.setTitle("sedang update data")

        val alertDialog = mDialog.create()
        alertDialog.show()
        btnUpdateKamar.setOnClickListener {
            val noKamar = edNoKamar.text.toString()
            val namaRumah = edNamaRumah.text.toString()
            val biayaKamar = edBiayaKamar.text.toString()
            val fasilitasKamar = edFasilitasKamar.text.toString()
            val statusKamar = edStatusKamar.text.toString()

            updateKamarData(
                noKamar,
                namaRumah,
                biayaKamar,
                fasilitasKamar,
                statusKamar
            )
        }
    }

    private fun updateKamarData(
        noKamar: String,
        namaRumah: String,
        biayaKamar: String,
        fasilitasKamar: String,
        statusKamar: String
    ) {
        TODO("Not yet implemented")
    }

    private fun initView() {
        TODO("Not yet implemented")
    }
}