package com.example.antriin.utils

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(number: Int): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    return format.format(number).replace("Rp", "Rp ").replace(",00", "")
}