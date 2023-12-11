package com.ptikel5.makkost.Act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ptikel5.makkost.R
import com.ptikel5.makkost.databinding.ActivityHistoryTranBinding

class historyTranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryTranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryTranBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}