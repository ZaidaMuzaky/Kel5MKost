package com.ptikel5.makkost.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ptikel5.makkost.datacl.KamarCl
import com.ptikel5.makkost.datacl.transaksiCL
import com.ptikel5.makkost.repository.kamarRepo
import com.ptikel5.makkost.repository.transaksiRepo

class transaksiViewModel : ViewModel() {
    private val repository : transaksiRepo
    private val _allTransaksi = MutableLiveData<List<transaksiCL>>()
    val alltransaksi : MutableLiveData<List<transaksiCL>> = _allTransaksi


    init {

        repository = transaksiRepo().getInstance()
        repository.loadTransaksi(_allTransaksi)

    }
}