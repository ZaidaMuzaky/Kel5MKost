package com.ptikel5.makkost.Act

import android.R
import android.R.layout.simple_spinner_item
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.databinding.ActivityTambahKamarBinding
import com.ptikel5.makkost.datacl.KamarCl


class TambahKamarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahKamarBinding
    private lateinit var database: DatabaseReference
    private lateinit var databaseSpinner: DatabaseReference
    private  val namaRumah: List<String> = ArrayList()
    private var selectedRumahId: String = ""
    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahKamarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userid = FirebaseAuth.getInstance().currentUser?.uid

        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("kamar")
        }!!
        databaseSpinner = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("rumah")
        }!!

        // button form
        binding.smpKamar.setOnClickListener {
            simpandatakamar()
        }

        binding.btl.setOnClickListener {
            finish()
        }

//        binding.inpFotokamar.setOnClickListener {
//            val intent = Intent()
//            intent.type = "image/*"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(
//                Intent.createChooser(intent, "Choose Image to Upload"), 0
//            )
//        }


        // spinner nama rumah
        val dataAdapter = ArrayAdapter<String>(this, simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.inpRumah.setAdapter(dataAdapter)

        databaseSpinner.addValueEventListener(object : ValueEventListener{
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

//        uploadImage(idKamar, idRumah)

        val dataKamar = KamarCl(idKamar,idRumah, noKamar, biayaKamar, fasilitasKamar, statusKamar)
        database.child(idRumah).child(idKamar).setValue(dataKamar).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahkan Data Kamar", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, KamarFragment::class.java)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data Kamar", Toast.LENGTH_SHORT).show()
        }
    }

//    fun uploadImage(idrumah: String, idkamar: String) {
//        if (fileUri != null) {
//            val userid = FirebaseAuth.getInstance().currentUser?.uid
//            val ref: StorageReference = userid?.let {
//                FirebaseStorage.getInstance().getReference(it)
//                    .child("kamar").child(idrumah).child(idkamar)
//            }!!
//            ref.putFile(fileUri!!).addOnSuccessListener {
//                Toast.makeText(applicationContext, "File Uploaded Successfully", Toast.LENGTH_LONG)
//                    .show()
//            }.addOnFailureListener {
//                Toast.makeText(applicationContext, "File Upload Failed...", Toast.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }


    private fun InputStatus(): ArrayList<String> {
        val status = ArrayList<String>()
        status.add("Kosong")
        status.add("Terisi")
        status.add("Perbaikan")

        return status
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
//            fileUri = data.data
//            try {
//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
//                binding.uploadGambar.setImageBitmap(bitmap)
//
//            } catch (e: Exception) {
//                Log.e("Exception", "Error: " + e)
//            }
//        }
//    }
}
