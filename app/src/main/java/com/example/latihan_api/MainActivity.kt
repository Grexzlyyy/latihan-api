package com.example.latihan_api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihan_api.adapter.CatatanAdapter
import com.example.latihan_api.databinding.ActivityMainBinding
import com.example.latihan_api.entities.Catatan
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CatatanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupEvents()
    }

    fun setupEvents() {
        // Update adapter dengan menambahkan onDelete
        adapter = CatatanAdapter(mutableListOf(), object : CatatanAdapter.CatatanItemevents {
            override fun onEdit(catatan: Catatan) {
                val intent = Intent(this@MainActivity, EditCatatanActivity::class.java)
                intent.putExtra("id_catatan", catatan.id)
                startActivity(intent)
            }

            // Fitur Delete: Munculkan Dialog saat ditekan lama
            override fun onDelete(catatan: Catatan) {
                showDeleteDialog(catatan)
            }
        })

        binding.container.adapter = adapter
        binding.container.layoutManager = LinearLayoutManager(this)

        binding.btnNavigate.setOnClickListener {
            val intent = Intent(this, CreateCatatan::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk menampilkan Popup Konfirmasi
    private fun showDeleteDialog(catatan: Catatan) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Hapus Catatan")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan '${catatan.judul}'?")

        builder.setPositiveButton("Ya, Hapus") { _, _ ->
            prosesHapusData(catatan.id!!)
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Fungsi untuk memanggil API Delete
    private fun prosesHapusData(id: Int) {
        lifecycleScope.launch {
            try {
                // Pastikan di Repository kamu sudah ada fungsi deleteCatatan(id)
                val response = RetrofitClient.catatanRepository.deleteCatatan(id)

                if (response.isSuccessful) {
                    displayMessage("Catatan berhasil dihapus")
                    loadData() // Refresh list setelah dihapus
                } else {
                    displayMessage("Gagal menghapus: ${response.message()}")
                }
            } catch (e: Exception) {
                displayMessage("Error: ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        Log.d("CekData", "Fungsi loadData() dipanggil!")
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.catatanRepository.getCatatan()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        adapter.updateDataset(data)
                    }
                } else {
                    displayMessage("Gagal : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CekData", "ERROR: ${e.message}")
                displayMessage("Error Koneksi: ${e.message}")
            }
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}