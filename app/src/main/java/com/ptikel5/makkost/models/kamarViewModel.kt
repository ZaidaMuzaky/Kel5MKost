package com.ptikel5.makkost.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ptikel5.makkost.datacl.KamarCl
import com.ptikel5.makkost.repository.kamarRepo

class kamarViewModel : ViewModel() {
    private val repository : kamarRepo
    private val _allKamar = MutableLiveData<List<KamarCl>>()
    val allKamar : LiveData<List<KamarCl>> = _allKamar


    init {

        repository = kamarRepo().getInstance()
        repository.loadKamar(_allKamar)

    }
}