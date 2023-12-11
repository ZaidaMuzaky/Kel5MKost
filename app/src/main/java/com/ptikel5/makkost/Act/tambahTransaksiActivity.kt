package com.ptikel5.makkost.Act

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.KamarFragment
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityTambahTransaksiBinding
import com.ptikel5.makkost.datacl.channelID
import com.ptikel5.makkost.datacl.notificationID
import com.ptikel5.makkost.datacl.transaksiCL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class tambahTransaksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahTransaksiBinding
    private lateinit var database: DatabaseReference
    private lateinit var databasehistory: DatabaseReference
    private lateinit var databasespinnerKamar: DatabaseReference
    private lateinit var databasespinnerRumah: DatabaseReference
    private lateinit var databasespinnerPenyewa: DatabaseReference
    private val namaRumah: MutableList<String> = ArrayList()
    private val namaKamar: MutableList<String> = ArrayList()
    private val namaPenyewa: List<String> = ArrayList()

    //    private lateinit var dataAdapter1: ArrayAdapter<String>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahTransaksiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("transaksi")
        }!!
        databasehistory = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("history")
        }!!
        databasespinnerRumah = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("rumah")
        }!!
//        databasespinnerKamar = userid?.let {
//            val selectedRumah = binding.inpRumahtrx.text.toString()
//            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
//                it
//            ).child("kamar").child(selectedRumah)
//        }!!
        databasespinnerPenyewa = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("penyewa")
        }!!
// penggunaan date picker
        binding.btndate.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                binding.inpTanggalmasuk.setText(formattedDate)
            }
        }
        binding.btndate1.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                binding.inpTanggalkeluar.setText(formattedDate)
            }
        }
        binding.btndate2.setOnClickListener {
            val dattePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            dattePicker.show(supportFragmentManager, "datePicker")
            dattePicker.addOnPositiveButtonClickListener { selectedDate ->
                val simpleDataFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val formattedDate = simpleDataFormat.format(Date(selectedDate))
                binding.inpJadwalbayar.setText(formattedDate)
            }
        }
//        createNotificationChannel()
        binding.smpTransaksi.setOnClickListener {
            simpandatatrx()
//            scheduleNotification()
        }

        binding.btl.setOnClickListener {
            finish()
        }

//        spinner rumah
        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.inpRumahtrx.setAdapter(dataAdapter)

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
        binding.inpRumahtrx.setOnItemClickListener { _, _, position, _ ->
            val selectedRumah = namaRumah[position]
            loadKamarData(selectedRumah)
        }
//    spinner penyewa
        val dataAdapter2 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaPenyewa)
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.inpPenyewatrx.setAdapter(dataAdapter2)

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


//        databasespinnerKamar.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                dataAdapter1.clear()
//                for (ds in snapshot.children) {
//                    val name = ds.child("noKamar").value.toString()
//                    dataAdapter1.add(name)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })


    }


    private fun scheduleNotification() {
        val intent = Intent(applicationContext, MyReceiver::class.java)
        intent.action = "MY_NOTIFICATION"

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.DAY_OF_MONTH, 29)
        calendar.set(Calendar.HOUR_OF_DAY, 10) // Ganti dengan jam yang diinginkan
        calendar.set(Calendar.MINUTE, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.MONTH, 1)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 29,
            pendingIntent
        )
        showAlert(calendar.timeInMillis, "Makost", "Jadwal Pembayaran Telah tiba")
    }

    class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "MY_NOTIFICATION") {
                // Tambahkan kode yang ingin dijalankan saat menerima notifikasi di sini
                Toast.makeText(context, "Jadwal Pembayaran Telah tiba", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title" +
                        "\nMessage: $message" +
                        "\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }



    private fun loadKamarData(selectedRumah: String) {
        //        spinner Kamar
        val dataAdapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaKamar)
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.inpKamartrx.setAdapter(dataAdapter1)
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

    private fun simpandatatrx() {
        val idTransaksi = database.push().key!!
        val idRumah = binding.inpRumahtrx.text.toString()
        val idKamar = binding.inpKamartrx.text.toString()
        val idPenyewa = binding.inpPenyewatrx.text.toString()
        val tanggalMasuk = binding.inpTanggalmasuk.text.toString()
        val tanggalKeluar = binding.inpTanggalkeluar.text.toString()
        val jadwalBayar = binding.inpJadwalbayar.text.toString()
        val totalBayar = binding.inpJumlahbayartrx.text.toString()

        val dataTransaksi = transaksiCL(
            idTransaksi,
            idRumah,
            idKamar,
            idPenyewa,
            tanggalMasuk,
            tanggalKeluar,
            jadwalBayar,
            totalBayar
        )
        database.child(idTransaksi).setValue(dataTransaksi).addOnCompleteListener {
            Toast.makeText(this, "Berhasil Menambahkan Data Transaksi", Toast.LENGTH_SHORT)
                .show()
            finish()
            scheduleNotification()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menambahkan data Transaksi", Toast.LENGTH_SHORT).show()
        }
    }
}