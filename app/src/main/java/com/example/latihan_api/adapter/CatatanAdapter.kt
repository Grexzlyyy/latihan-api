package com.example.latihan_api.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihan_api.databinding.ItemCatatanBinding
import com.example.latihan_api.entities.Catatan

class CatatanAdapter(
    private val dataset: MutableList<Catatan>,
    private val events: CatatanItemevents
) : RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder>() {

    interface CatatanItemevents {
        fun onEdit(catatan: Catatan)
    }

    inner class CatatanViewHolder(
        val view: ItemCatatanBinding
    ) : RecyclerView.ViewHolder(view.root) {
        // Fungsi untuk mengikat data ke tampilan (TextView)
        fun setDataKeUI(data: Catatan) {
            view.judul.text = data.judul
            view.isi.text = data.isi

            view.root.setOnClickListener{
                events.onEdit(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder {
        val binding = ItemCatatanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CatatanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        val dataSekarang = dataset[position]
        holder.setDataKeUI(dataSekarang)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(dataBaru: List<Catatan>) {
        dataset.clear()
        dataset.addAll(dataBaru)
        notifyDataSetChanged()
    }
}