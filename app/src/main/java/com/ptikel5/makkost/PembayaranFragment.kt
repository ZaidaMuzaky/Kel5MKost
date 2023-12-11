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
import com.ptikel5.makkost.Act.detailPenyewaActivity
import com.ptikel5.makkost.Act.detailTransaksiActivity
import com.ptikel5.makkost.Act.tambahTransaksiActivity
import com.ptikel5.makkost.adapt.penyewaAdapt
import com.ptikel5.makkost.adapt.tranAdapt
import com.ptikel5.makkost.databinding.FragmentPembayaranBinding
import com.ptikel5.makkost.models.transaksiViewModel


class PembayaranFragment : Fragment() {
    private lateinit var recyclerViewtransaksi : RecyclerView
    private var _binding: FragmentPembayaranBinding? = null
    private lateinit var viewModel: transaksiViewModel
    lateinit var adapter: tranAdapt
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
        // recyclerview
        recyclerViewtransaksi = binding.transaksilist
        recyclerViewtransaksi.layoutManager = LinearLayoutManager(context)
        recyclerViewtransaksi.setHasFixedSize(true)
        adapter = tranAdapt()
        recyclerViewtransaksi.adapter = adapter

        viewModel = ViewModelProvider(this).get(transaksiViewModel::class.java)
        viewModel.alltransaksi.observe(viewLifecycleOwner, Observer {

          adapter.updateTransaksiList(it)

        })
        adapter.setOnItemClickListener(object : tranAdapt.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(requireActivity(), detailTransaksiActivity::class.java)
                val selectedKamar = viewModel.alltransaksi.value?.get(position)

                selectedKamar?.let {
                    intent.putExtra("idTransaksi", it.idTransaksi)
                    intent.putExtra("idRumah", it.idRumah)
                    intent.putExtra("idKamar", it.idKamar)
                    intent.putExtra("idPenyewa", it.idPenyewa)
                    intent.putExtra("tanggalMasuk", it.tanggalMasuk)
                    intent.putExtra("tanggalKeluar", it.tanggalKeluar)
                    intent.putExtra("jadwalBayar", it.jadwalBayar)
                    intent.putExtra("totalBayar", it.totalBayar)

                    startActivity(intent)
                }
            }

        })
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}