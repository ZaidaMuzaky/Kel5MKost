package com.ptikel5.makkost.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ptikel5.makkost.datacl.KamarCl
import com.ptikel5.makkost.datacl.PenyewaCL
import com.ptikel5.makkost.repository.penyewaRepo

class penyewaViewModel : ViewModel() {
    private var repository: penyewaRepo = penyewaRepo()
    private val _allPenyewa = MutableLiveData<List<PenyewaCL>>()
    val allPenyewa: MutableLiveData<List<PenyewaCL>> = _allPenyewa

    init {
        repository = penyewaRepo().getInstance()
        repository.loadPenyewa(_allPenyewa)
    }
}