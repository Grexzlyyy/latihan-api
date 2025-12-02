package com.example.latihan_api.entities

import com.google.gson.annotations.SerializedName

data class Catatan(
    val id: Int?,
    val judul: String,
    val isi: String,

    // Tambahkan ini. Gunakan SerializedName agar aman jika nama variabel beda dengan JSON
    @SerializedName("user_id")
    val userId: Int?
)