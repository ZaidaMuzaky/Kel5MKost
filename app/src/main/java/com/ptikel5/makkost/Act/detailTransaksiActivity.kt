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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityDetailTransaksiBinding
import com.ptikel5.makkost.datacl.Rumah
import com.ptikel5.makkost.datacl.historyCL
import com.ptikel5.makkost.datacl.transaksiCL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class detailTransaksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransaksiBinding
    private lateinit var database: DatabaseReference
    private lateinit var databasehis: DatabaseReference
    private lateinit var databasespinnerRumah : DatabaseReference
    private lateinit var databasespinnerKamar : DatabaseReference
    private lateinit var databasespinnerPenyewa : DatabaseReference
    private val namaRumah: MutableList<String> = ArrayList()
    private val namaKamar: MutableList<String> = ArrayList()
    private  val namaPenyewa: List<String> = ArrayList()
    private lateinit var hisarraylist: ArrayList<historyCL>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var idTransaksi = intent.getStringExtra("idTransaksi").toString()
        initView()
        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("idTransaksi").toString(),
                intent.getStringExtra("idRumah").toString(),
                intent.getStringExtra("idKamar").toString(),
                intent.getStringExtra("idPenyewa").toString(),
                intent.getStringExtra("tanggalMasuk").toString(),
                intent.getStringExtra("tanggalKeluar").toString(),
                intent.getStringExtra("jadwalBayar").toString(),
                intent.getStringExtra("totalBayar").toString()
            )
        }
        binding.btnDelete.setOnClickListener {
            deletedata(idTransaksi)
        }
        binding.btnhistory.setOnClickListener {
            val intent = Intent(this, historyTranActivity::class.java)
            intent.putExtra("idTransaksi", idTransaksi)
            startActivity(intent)
        }
    }

    private fun deletedata(idTransaksi: String) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("transaksi")
        }!!
        database.child(idTransaksi).removeValue().addOnSuccessListener {
            Toast.makeText(this, "berhasil hapus", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "gagal hapus", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openUpdateDialog(
        idTransaksi : String,
        idRumah : String,
        idKamar : String,
        idPenyewa : String,
        tanggalMasuk : String,
        tanggalKeluar : String,
        jadwalBayar : String,
        totalBayar : String,
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.transaksi_updatedialog, null)

        mDialog.setView(mDialogView)


        val edNamaRumah = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_idrumahtrx)
        val edNoKamar = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_nokamartrx)
        val edNamaPenyewa = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_namapenyewatrx)
        val edtanggalmasuk = mDialogView.findViewById<TextInputEditText>(R.id.inp_tanggalmasuk)
        val edtanggalkeluar = mDialogView.findViewById<TextInputEditText>(R.id.inp_tanggalkeluar)
        val edjadwalbayar = mDialogView.findViewById<TextInputEditText>(R.id.inp_jadwalbayar)
        val edtotalbayar = mDialogView.findViewById<EditText>(R.id.upd_totalBayar)
        val btnupdatetrx = mDialogView.findViewById<Button>(R.id.update_transaksi)
        val btndate = mDialogView.findViewById<Button>(R.id.btndate)
        val btndate1 = mDialogView.findViewById<Button>(R.id.btndate1)
        val btndate2 = mDialogView.findViewById<Button>(R.id.btndate2)


// penggunaan date picker
        btndate.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                edtanggalmasuk.setText(formattedDate)
            }
        }
        btndate1.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                edtanggalkeluar.setText(formattedDate)
            }
        }
        btndate2.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                edjadwalbayar.setText(formattedDate)
            }
        }
//        inisiasi database untuk spinner
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        databasespinnerRumah = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("rumah")
        }!!
        databasespinnerPenyewa = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("penyewa")
        }!!
        //        spinner rumah
        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edNamaRumah.setAdapter(dataAdapter)

        databasespinnerRumah.addValueEventListener(object : ValueEventListener {
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
// inisialisasi spinner kamar
        edNamaRumah.setOnItemClickListener { _, _, position, _ ->
            val selectedRumah = namaRumah[position]
            loadKamarData(selectedRumah)
        }
//    spinner penyewa
        val dataAdapter2 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaPenyewa)
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edNamaPenyewa.setAdapter(dataAdapter2)

        databasespinnerPenyewa.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataAdapter2.clear()
                for (ds in snapshot.children) {
                    val name = ds.child("namaPenyewa").value.toString()
                    dataAdapter2.add(name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
       
        edNamaRumah.setText(intent.getStringExtra("idRumah").toString())
        edNoKamar.setText(intent.getStringExtra("idKamar").toString())
        edNamaPenyewa.setText(intent.getStringExtra("idPenyewa").toString())
        edtanggalmasuk.setText(intent.getStringExtra("tanggalMasuk").toString())
        edtanggalkeluar.setText(intent.getStringExtra("tanggalKeluar").toString())
        edjadwalbayar.setText(intent.getStringExtra("jadwalBayar").toString())
        edtotalbayar.setText(intent.getStringExtra("totalBayar").toString())

        val histidtran = intent.getStringExtra("idTransaksi").toString()
        val histnamrumah = intent.getStringExtra("idRumah").toString()
        val histnokamar = intent.getStringExtra("idKamar").toString()
        val histnampenyewa = intent.getStringExtra("idPenyewa").toString()
        val histtanggalmasuk = intent.getStringExtra("tanggalMasuk").toString()
        val histtanggalkeluar = intent.getStringExtra("tanggalKeluar").toString()
        val histjadwalbayar = intent.getStringExtra("jadwalBayar").toString()
        val histtotalbayar = intent.getStringExtra("totalBayar").toString()

        mDialog.setTitle("sedang update data")
        val alertDialog = mDialog.create()
        alertDialog.show()
        btnupdatetrx.setOnClickListener {
            val idRumah = edNamaRumah.text.toString()
            val idKamar = edNoKamar.text.toString()
            val idPenyewa = edNamaPenyewa.text.toString()
            val tanggalMasuk = edtanggalmasuk.text.toString()
            val tanggalKeluar = edtanggalkeluar.text.toString()
            val jadwalBayar = edjadwalbayar.text.toString()
            val totalBayar = edtotalbayar.text.toString()

            updateTransaksi(
                idTransaksi,
                idRumah,
                idKamar,
                idPenyewa,
                tanggalMasuk,
                tanggalKeluar,
                jadwalBayar,
                totalBayar
            )
            masukhistory(
                histidtran,
                histnamrumah,
                histnokamar,
                histnampenyewa,
                histtanggalmasuk,
                histtanggalkeluar,
                histjadwalbayar,
                histtotalbayar
            )
            alertDialog.dismiss()
        }
    }

    private fun masukhistory(  idTransaksi: String,
                               idRumah: String,
                               idKamar: String,
                               idPenyewa: String,
                               tanggalMasuk: String,
                               tanggalKeluar: String,
                               jadwalBayar: String,
                               totalBayar: String) {
        val idhistory = database.push().key!!
        val dataTransaksi = historyCL(
            idhistory,
            idTransaksi,
            idRumah,
            idKamar,
            idPenyewa,
            tanggalMasuk,
            tanggalKeluar,
            jadwalBayar,
            totalBayar
        )
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        databasehis  = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("history")
        }!!
        databasehis.child(idTransaksi).child(idhistory).setValue(dataTransaksi).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahkan Data Transaksi", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, KamarFragment::class.java)

            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data Transaksi", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadKamarData(selectedRumah: String) {
//        spinner Kamar
        val dataAdapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaKamar)
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.transaksi_updatedialog, null)
        val ednokamar = mDialogView.findViewById<AutoCompleteTextView>(R.id.upd_nokamartrx)
        ednokamar.setAdapter(dataAdapter1)
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        databasespinnerKamar = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(it)
                .child("kamar")
                .child(selectedRumah)
        }!!

        databasespinnerKamar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                namaKamar.clear()
                for (ds in snapshot.children) {
                    val name = ds.child("noKamar").value.toString()
                    namaKamar.add(name)
                }
                dataAdapter1.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    private fun updateTransaksi(
        idTransaksi: String,
        idRumah: String,
        idKamar: String,
        idPenyewa: String,
        tanggalMasuk: String,
        tanggalKeluar: String,
        jadwalBayar: String,
        totalBayar: String
    ) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("transaksi")
        }!!
        val tranksaksiin = mapOf<String, String>(
            "idTransaksi" to idTransaksi,
            "idRumah" to idRumah,
            "idKamar" to idKamar,
            "idPenyewa" to idPenyewa,
            "tanggalMasuk" to tanggalMasuk,
            "tanggalKeluar" to tanggalKeluar,
            "jadwalBayar" to jadwalBayar,
            "totalBayar" to totalBayar,
            )
        database.child(idTransaksi).updateChildren(tranksaksiin).addOnCompleteListener {
            Toast.makeText(this, "berhasil edit", Toast.LENGTH_SHORT).show()
            database = userid?.let {
                FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                    it
                ).child("transaksi")
            }!!
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "gagal edit", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initView() {
        binding.tvDtIdRumahtrx.text = intent.getStringExtra("idRumah")
        binding.tvDtnokamartrx.text = intent.getStringExtra("idKamar")
        binding.tvDtnamapenyewatrx.text = intent.getStringExtra("idPenyewa")
        binding.tvDttanggalmasuk.text = intent.getStringExtra("tanggalMasuk")
        binding.tvDttanggalkeluartrx.text = intent.getStringExtra("tanggalKeluar")
        binding.tvDtjadwalbayar.text = intent.getStringExtra("jadwalBayar")
        binding.tvDttotalbayar.text = intent.getStringExtra("totalBayar")
    }
}