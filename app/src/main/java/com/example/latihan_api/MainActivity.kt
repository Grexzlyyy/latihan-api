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
        adapter = CatatanAdapter(mutableListOf())
        // Pastikan di activity_main.xml ada RecyclerView dengan ID 'container'
        binding.container.adapter = adapter
        binding.container.layoutManager = LinearLayoutManager(this)

        // Kode navigasi ini dikomentari di gambar asli Anda
//        binding.btnNavigate.setOnClickListener {
//            val intent = Intent(this, CreateCatatan::class.java)
//            startActivity(intent)
//        }

    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        // JEJAK 1: Mulai proses
        Log.d("CekData", "Fungsi loadData() dipanggil! Sedang menghubungi server...")

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.catatanRepository.getCatatan()

                // JEJAK 2: Server merespon
                Log.d("CekData", "Respon diterima. Code: ${response.code()}")

                if (!response.isSuccessful) {
                    Log.e("CekData", "Gagal Server: ${response.message()}")
                    displayMessage("Gagal : ${response.message()}")
                    return@launch
                }

                val data = response.body()

                // JEJAK 3: Cek isi data
                Log.d("CekData", "Isi Data: $data")

                if (data == null) {
                    Log.e("CekData", "Data Kosong (Null)")
                    displayMessage("Tidak ada data")
                    return@launch
                }

                // JEJAK 4: Masukkan ke Adapter
                Log.d("CekData", "Menampilkan ${data.size} catatan ke layar")
                adapter.updateDataset(data)

            } catch (e: Exception) {
                // JEJAK 5: Error Koneksi (Paling sering terjadi)
                Log.e("CekData", "ERROR KONEKSI FATAL: ${e.message}")
                e.printStackTrace()
                displayMessage("Error Koneksi: ${e.message}")
            }
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}