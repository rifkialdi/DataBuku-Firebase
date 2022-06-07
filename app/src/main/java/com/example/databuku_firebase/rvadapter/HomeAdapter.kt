package com.example.databuku_firebase.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databuku_firebase.databinding.ItemRvHomeBinding
import com.example.databuku_firebase.model.DataBukuModel

class HomeAdapter(val listItem: ArrayList<DataBukuModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRvHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listItem[position]
        holder.binding.idtvJudulBuku.text = "Judul : ${data.judulBuku}"
        holder.binding.idtvPenerbit.text = "Penerbit : ${data.penerbit}"
        holder.binding.idtvTahunTerbit.text = "Tahun Terbit : ${data.tahunTerbit}"
        holder.binding.idtvPenulis.text = "Penulis : ${data.penulis}"
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}