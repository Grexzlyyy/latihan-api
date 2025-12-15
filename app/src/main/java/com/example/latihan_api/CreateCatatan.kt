package com.example.latihan_api

import android.content.Intent
import android.os.Bundle
import android.util.Log // Pastikan ada import ini
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.latihan_api.databinding.ActivityCreateCatatanBinding
import com.example.latihan_api.entities.Catatan
import kotlinx.coroutines.launch

class CreateCatatan : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCatatanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateCatatanBinding.inflate(layoutInflater)

        // --- GANTI JADI INI ---
        setContentView(binding.root)
        // ----------------------

        // Perhatikan: Gunakan binding.root juga di sini agar lebih aman (opsional tapi disarankan)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupEvents()
    }

    fun setupEvents() {
        binding.tombolSimpan.setOnClickListener {
            // --- TARUH KODE TES DI SINI (BARIS PERTAMA DALAM KURUNG KURAWAL) ---
            Toast.makeText(this, "TOMBOL DITEKAN!", Toast.LENGTH_SHORT).show()
            Log.d("CobaTes", "Tombol Simpan Ditekan!")
            // -------------------------------------------------------------------

            val judul = binding.inputJudul.text.toString()
            val isi = binding.inputIsi.text.toString()

            // Validasi input
            if (judul.isEmpty() || isi.isEmpty()) {
                displayMessage("Judul dan isi catatan harus diisi")
                return@setOnClickListener
            }

            // ... (kode payload dan coroutine Anda lanjut di bawah sini) ...

            val payload = Catatan(
                id = null,
                judul = judul,
                isi = isi,
                userId = 1
            )

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.catatanRepository.createCatatan(payload)
                    if (response.isSuccessful) {
                        displayMessage("Catatan Berhasil dibuat")
                        val intent = Intent(this@CreateCatatan, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Tambahkan Log error juga di sini untuk cek detail errornya
                        Log.e("CobaTes", "Gagal: ${response.message()}")
                        displayMessage("Gagal menyimpan: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Tambahkan Log error koneksi
                    Log.e("CobaTes", "Error Koneksi: ${e.message}")
                    displayMessage("Error koneksi: ${e.message}")
                }
            }
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}