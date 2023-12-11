package com.ptikel5.makkost

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ptikel5.makkost.databinding.FragmentBerandaBinding
import com.ptikel5.makkost.databinding.FragmentKamarBinding
import java.text.FieldPosition


class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var ref: DatabaseReference
    private lateinit var Spinner: Spinner
    private  val namaRumah: List<String> = ArrayList()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)

        // filtering
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        database = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    it
                ).child("rumah")
        }!!


        val dataAdapter =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, namaRumah)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.filNamarumah.setAdapter(dataAdapter)

        binding.btnRefresh.setOnClickListener {
            refreshKamarData()
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataAdapter.clear()
                for (ds in snapshot.children) {
                    val idRumah = ds.child("idRumah").value.toString()
                    val name = ds.child("namaRumah").value.toString()
                    dataAdapter.add(name)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        //listener
        binding.filNamarumah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedRumah = parentView?.getItemAtPosition(position).toString()

                fetchPieChartData(selectedRumah)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }

        return binding.root
    }

    private fun refreshKamarData() {
        val selectedRumah = binding.filNamarumah.text.toString()
        fetchPieChartData(selectedRumah)
    }

    private fun fetchPieChartData(selectedRumah: String) {
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = userid?.let {
            FirebaseDatabase.getInstance("https://makkost-65394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(
                it
            ).child("kamar").child(selectedRumah)
        }

            ref?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val kamarMap = dataSnapshot.value as? HashMap<String, Any>

                    if (kamarMap != null) {
                        var totalKosong = 0
                        var totalTerisi = 0
                        var totalPerbaikan = 0

                        for (kamar in kamarMap.values) {
                            val statusKamar =
                                (kamar as? HashMap<String, Any>)?.get("statusKamar") as? String
                            when (statusKamar) {
                                "Kosong" -> totalKosong++
                                "Terisi" -> totalTerisi++
                                "Perbaikan" -> totalPerbaikan++
                            }
                        }

                        val entiers = ArrayList<PieEntry>()
                        entiers.add(PieEntry(totalKosong.toFloat(), "Kosong"))
                        entiers.add(PieEntry(totalPerbaikan.toFloat(), "Perbaikan"))
                        entiers.add(PieEntry(totalTerisi.toFloat(), "Terisi"))

                        val pieDataSet = PieDataSet(entiers, "")
                        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.asList()
                        pieDataSet.valueTextSize = 12f

                        val pieData = PieData(pieDataSet)
                        binding.pchart.data = pieData
                        binding.pchart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        binding.pchart.legend.horizontalAlignment =
                            Legend.LegendHorizontalAlignment.LEFT
                        binding.pchart.legend.orientation = Legend.LegendOrientation.VERTICAL

                        binding.pchart.legend.textSize = 12f
                        binding.pchart.description.isEnabled = false

                        binding.pchart.animateY(1000)
                        binding.pchart.invalidate()

                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("BerandaFragment", "loadPost:onCancelled", databaseError.toException())
                }
            })

        }
    }