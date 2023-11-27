package com.ptikel5.makkost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ptikel5.makkost.Act.ListRumActivity
import com.ptikel5.makkost.Act.TambahKamarActivity
import com.ptikel5.makkost.databinding.FragmentKamarBinding
import com.ptikel5.makkost.databinding.FragmentPengaturanBinding


class KamarFragment : Fragment() {

    private var _binding: FragmentKamarBinding? = null
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
        _binding = FragmentKamarBinding.inflate(inflater, container, false)

        binding.floatAddKamar.setOnClickListener {
            val intent = Intent(activity, TambahKamarActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {


    }
}