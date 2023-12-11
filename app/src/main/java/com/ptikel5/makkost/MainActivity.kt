package com.ptikel5.makkost

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ptikel5.makkost.databinding.ActivityMainBinding
import com.ptikel5.makkost.datacl.notificationID
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(BerandaFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.beranda -> replaceFragment(BerandaFragment())
                R.id.kamar -> replaceFragment(KamarFragment())
                R.id.penyewa -> replaceFragment(PenyewaFragment())
                R.id.pembayaran -> replaceFragment(PembayaranFragment())
                R.id.pengaturan -> replaceFragment(PengaturanFragment())

                else->{

                }
            }
            true
        }

}




    //    mengganti fragment
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}