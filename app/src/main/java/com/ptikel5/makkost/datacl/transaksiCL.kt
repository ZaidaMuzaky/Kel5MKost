package com.ptikel5.makkost.datacl

data class transaksiCL(
    var idTransaksi : String? = null,
    var idRumah : String? = null,
    var idKamar : String? = null,
    var idPenyewa : String? = null,
    var tanggalMasuk : String? = null,
    var tanggalKeluar : String? = null,
    var jadwalBayar : String? = null,
    var totalBayar : String? = null,
)
