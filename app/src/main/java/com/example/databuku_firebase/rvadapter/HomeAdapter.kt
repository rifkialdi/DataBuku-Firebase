package com.example.databuku_firebase.rvadapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.example.databuku_firebase.databinding.ItemRvHomeBinding
import com.example.databuku_firebase.model.DataBukuModel
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HomeAdapter(val listItem: ArrayList<DataBukuModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var imageUri: Uri

    /* properties Untuk menampilkan gambar dari data url di realtime firebase */
    /* ini cara yang kedua yaaaa
    private var executor: Executor = Executors.newSingleThreadExecutor()
    private var handler: Handler =Handler(Looper.getMainLooper())
    private var image: Bitmap? = null*/

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

        /* Untuk mengambil gambar dari url realtime database */
        Glide.with(holder.itemView)
            .load(data.urlFoto)
            .placeholder(R.drawable.notify_panel_notification_icon_bg)
            .error(R.drawable.notification_icon_background)
            .centerCrop()
            .into(holder.binding.idivCover)


        /*
        Cara ini sama kok, Untuk mengambil gambar dari url realtime database
         INI CARA YANG DARI PAK ZULKA
        executor.execute {
            val imageURL = data.urlFoto

            try {
                val ins = URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(ins)

                handler.post {
                    holder.binding.idivCover.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

}