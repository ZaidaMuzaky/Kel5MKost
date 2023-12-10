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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailKamarBinding
import com.ptikel5.makkost.databinding.ActivityDetailRumahBinding
import com.ptikel5.makkost.databinding.ActivityTambahKamarBinding

class detailKamarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKamarBinding
    private lateinit var database: DatabaseReference
    private lateinit var databaseSpinner : DatabaseReference
    private  val namaRumah: List<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailKamarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("idKamar").toString(),
                intent.getStringExtra("idRumah").toString(),
                intent.getStringExtra("noKamar").toString(),
                intent.getStringExtra("biayaKamar").toString(),
                intent.getStringExtra("fasilitasKamar").toString(),
                intent.getStringExtra("statusKamar").toString(),
            )
        }
        binding.btnDelete.setOnClickListener {
            (deletedata(intent.getStringExtra("idRumah").toString(), intent.getStringExtra("idKamar").toString()))
        }

    }
    private fun deletedata(idRumah: String, idKamar: String) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("kamar")
        }!!
        database.child(idRumah).child(idKamar).removeValue().addOnSuccessListener {
            Toast.makeText(this, "berhasil hapus", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "gagal hapus", Toast.LENGTH_SHORT).show()
        }

    }
    private fun openUpdateDialog(
        idKamar : String,
        idRumah : String,
        noKamar : String,
        biayaKamar : String,
        fasilitasKamar : String,
        statusKamar : String) {

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        databaseSpinner = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("rumah")
        }!!


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


        // spinner nama rumah
        val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edNamaRumah.setAdapter(dataAdapter)

        databaseSpinner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataAdapter.clear()
                for (ds in snapshot.children) {
//                    val idRumah = ds.child("idRumah").value.toString()
                    val name = ds.child("namaRumah").value.toString()
                    dataAdapter.add(name)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // spinner status
        val statusinput: ArrayList<String> = InputStatus()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusinput)
        edStatusKamar.setAdapter(adapter)

    mDialog.setTitle("sedang update data")

        val alertDialog = mDialog.create()
        alertDialog.show()
        btnUpdateKamar.setOnClickListener {
            val idRumah = edNamaRumah.text.toString()
            val noKamar = edNoKamar.text.toString()
            val biayaKamar = edBiayaKamar.text.toString()
            val fasilitasKamar = edFasilitasKamar.text.toString()
            val statusKamar = edStatusKamar.text.toString()

            updateKamarData(
                idKamar,
                idRumah,
                noKamar,
                biayaKamar,
                fasilitasKamar,
                statusKamar,

            )
            alertDialog.dismiss()
        }
    }
    private fun InputStatus(): ArrayList<String> {
        val status = ArrayList<String>()
        status.add("Kosong")
        status.add("Terisi")
        status.add("Perbaikan")

        return status
    }

    private fun updateKamarData(
        idKamar: String,
        idRumah: String,
        noKamar: String,
        biayaKamar: String,
        fasilitasKamar: String,
        statusKamar: String
    ) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("kamar").child(idRumah)
        }!!
        val kamarin = mapOf<String, String>(
            "noKamar" to noKamar,
            "idRumah" to idRumah,
            "biayaKamar" to biayaKamar,
            "fasilitasKamar" to fasilitasKamar,
            "statusKamar" to statusKamar,

        )
        database.child(idKamar).updateChildren(kamarin).addOnSuccessListener {
            Toast.makeText(this, "berhasil edit", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "gagal edit", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initView() {
        binding.tvDtIdKamar.text = intent.getStringExtra("idKamar")
        binding.tvDtNoKamar.text = intent.getStringExtra("noKamar")
        binding.tvDtNamaRumah.text = intent.getStringExtra("idRumah")
        binding.tvDtBiayaKamar.text = intent.getStringExtra("biayaKamar")
        binding.tvDtFasilitasKamar.text = intent.getStringExtra("fasilitasKamar")
        binding.tvDtStatusKamar.text = intent.getStringExtra("statusKamar")
    }
}