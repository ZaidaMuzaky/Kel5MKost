package com.ptikel5.makkost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.ptikel5.makkost.Act.TambahKamarActivity
import com.ptikel5.makkost.Act.tambahPenyewaActivity
import com.ptikel5.makkost.adapt.kamAdapt
import com.ptikel5.makkost.adapt.penyewaAdapt
import com.ptikel5.makkost.databinding.FragmentKamarBinding
import com.ptikel5.makkost.databinding.FragmentPenyewaBinding
import com.ptikel5.makkost.models.kamarViewModel
import com.ptikel5.makkost.models.penyewaViewModel


class PenyewaFragment : Fragment() {

    private lateinit var recyclerViewPenyewa : RecyclerView
    private var _binding: FragmentPenyewaBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var viewModel: penyewaViewModel
    lateinit var adapter: penyewaAdapt
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
        // Inflate the layout for this fragment
        _binding = FragmentPenyewaBinding.inflate(inflater, container, false)

        // float btn
        binding.floatAddPenyewa.setOnClickListener {
            val intent = Intent(activity, tambahPenyewaActivity::class.java)
            startActivity(intent)
        }

        // recyclerview
        recyclerViewPenyewa = binding.listKamar
        recyclerViewPenyewa.layoutManager = LinearLayoutManager(context)
        recyclerViewPenyewa.setHasFixedSize(true)
        adapter = penyewaAdapt()
        recyclerViewPenyewa.adapter = adapter

        viewModel = ViewModelProvider(this).get(penyewaViewModel::class.java)
        viewModel.allPenyewa.observe(viewLifecycleOwner, Observer {

            adapter.updatePenyewalist(it)

        })

        return binding.root
    }

    companion object {


    }
}