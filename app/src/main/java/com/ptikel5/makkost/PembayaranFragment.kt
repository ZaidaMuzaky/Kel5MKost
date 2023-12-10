package com.ptikel5.makkost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ptikel5.makkost.Act.TambahKamarActivity
import com.ptikel5.makkost.Act.tambahTransaksiActivity
import com.ptikel5.makkost.databinding.FragmentKamarBinding
import com.ptikel5.makkost.databinding.FragmentPembayaranBinding


class PembayaranFragment : Fragment() {
    private var _binding: FragmentPembayaranBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPembayaranBinding.inflate(inflater, container, false)

        binding.floatAddtrx.setOnClickListener {
            val intent = Intent(activity, tambahTransaksiActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}