package com.example.latihan_api

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.latihan_api.databinding.ActivityEditCatatanBinding
import com.example.latihan_api.entities.Catatan
import kotlinx.coroutines.launch

class EditCatatanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCatatanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupEvents()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun setupEvents(){
        binding.tombolEdit.setOnClickListener{
            val id = intent.getIntExtra("id_catatan", 0)
            val judul = binding.inputJudul.text.toString()
            val isi = binding.inputIsi.text.toString()

            if (isi.isEmpty() || judul.isEmpty()){
                displayMesagge("Judul isi catatan harus di isi")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val catatan = Catatan(id, judul, isi, userId = 1)
                val data = RetrofitClient.catatanRepository.editCatatan(id, catatan)

                if (data.isSuccessful){
                    displayMesagge("Catatan Berhasil diubah")
                    switchPage(MainActivity::class.java)
                }else {
                    displayMesagge("Error: ${data.message()}")
                }
            }
        }
    }

    fun loadData(){
        val id = intent.getIntExtra("id_catatan", 0)

        if (id == 0) {
            displayMesagge("Error: id catatan tidak terkirim")
            switchPage(MainActivity::class.java)
            return
        }
        lifecycleScope.launch {
            val data = RetrofitClient.catatanRepository.getCatatan(id)
            if (data.isSuccessful){
                val catatan = data.body()
                binding.inputJudul.setText(catatan?.judul)
                binding.inputIsi.setText(catatan?.isi)
            } else {
                displayMesagge("Error: ${data.message()}")
                switchPage(MainActivity::class.java)
            }
        }
    }

    fun displayMesagge(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun switchPage(destination: Class<MainActivity>){
        val intent= Intent(this,destination)
        startActivity(intent)
    }

}