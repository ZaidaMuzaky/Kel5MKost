package com.ptikel5.makkost

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.Act.TambahKamarActivity
import com.ptikel5.makkost.Act.detailKamarActivity
import com.ptikel5.makkost.Act.detailRumahActivity
import com.ptikel5.makkost.adapt.kamAdapt
import com.ptikel5.makkost.databinding.FragmentKamarBinding
import com.ptikel5.makkost.datacl.KamarCl
import com.ptikel5.makkost.models.kamarViewModel
import com.ptikel5.makkost.repository.kamarRepo

private lateinit var recyclerViewKamar : RecyclerView
class KamarFragment : Fragment() {

    private var _binding: FragmentKamarBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var kamarArrayList: ArrayList<KamarCl>
    private  val namaRumah: List<String> = ArrayList()
    private lateinit var viewModel : kamarViewModel
    lateinit var adapter: kamAdapt
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

        // float button
        binding.floatAddKamar.setOnClickListener {
            val intent = Intent(activity, TambahKamarActivity::class.java)
            startActivity(intent)
        }

        binding.btnRefresh.setOnClickListener {
            refreshKamarData()
        }




        // filtering
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("rumah")
        }!!

        val dataAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.filNamarumah.setAdapter(dataAdapter)

//        // Tambahkan listener spinner di sini
//        binding.filNamarumah.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
//                // Panggil metode refresh data kamar di sini
//                val selectedRumah = parentView?.getItemAtPosition(position).toString()
//                refreshKamarData(selectedRumah)
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // Tidak ada tindakan yang perlu diambil jika tidak ada yang dipilih
//            }
//        })

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataAdapter.clear()
                for (ds in snapshot.children) {
                    val idRumah = ds.child("idRumah").value.toString()
                    val name = ds.child("namaRumah").value.toString()
//                    refreshKamarData(idRumah)
                    dataAdapter.add(name)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


        // rcycview
        recyclerViewKamar = binding.listKamar
        recyclerViewKamar.layoutManager = LinearLayoutManager(context)
        recyclerViewKamar.setHasFixedSize(true)
        adapter = kamAdapt()
        recyclerViewKamar.adapter = adapter

        viewModel = ViewModelProvider(this).get(kamarViewModel::class.java)

        viewModel.allKamar.observe(viewLifecycleOwner, Observer {

            adapter.updatekamarlist(it)

        })
        adapter.setOnItemClickListener(object : kamAdapt.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(requireActivity(), detailKamarActivity::class.java)

//                intent.putExtra("idKamar", kamarArrayList[position].idKamar)
//                intent.putExtra("idRumah", kamarArrayList[position].idRumah)
//                intent.putExtra("noKamar", kamarArrayList[position].noKamar)
//                intent.putExtra("fasilitasKamar", kamarArrayList[position].fasilitasKamar)
//                intent.putExtra("statusKamar", kamarArrayList[position].statusKamar)
//
//                startActivity(intent)
                val selectedKamar = viewModel.allKamar.value?.get(position)

                selectedKamar?.let {
                    intent.putExtra("idKamar", it.idKamar)
                    intent.putExtra("idRumah", it.idRumah)
                    intent.putExtra("noKamar", it.noKamar)
                    intent.putExtra("biayaKamar", it.biayaKamar)
                    intent.putExtra("fasilitasKamar", it.fasilitasKamar)
                    intent.putExtra("statusKamar", it.statusKamar)
                    startActivity(intent)
                }
            }

        })


        return binding.root
    }


    private fun refreshKamarData() {
        val selectedRumah = binding.filNamarumah.text.toString()
        val kamarRepo = kamarRepo(selectedRumah)
        kamarRepo.loadKamar(viewModel.allKamar as MutableLiveData<List<KamarCl>>)
    }

    companion object {


    }
}