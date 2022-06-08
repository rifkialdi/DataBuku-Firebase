package com.example.databuku_firebase

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.databuku_firebase.databinding.ActivityTambahBukuBinding
import com.example.databuku_firebase.model.DataBukuModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import java.util.concurrent.Executor

class TambahBukuActivity : AppCompatActivity() {
    /* View binding */
    private lateinit var binding: ActivityTambahBukuBinding
    /* Buat objek untuk instance Firebase Realtime */
    private lateinit var dbRealtime: DatabaseReference
    /* Buat objek untuk instance Firebase Storage */
    private lateinit var uploadTask: UploadTask
    private lateinit var fStorage: FirebaseStorage
    private lateinit var sRef: StorageReference
    /* deklarasi untuk image uri */
    private lateinit var imageUri: Uri

    /* Untuk menampilkan gambar dari firebase storage
    private lateinit var executor: Executor
    private lateinit var handler: Handler
    private var image: Bitmap? = null
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBukuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Menambah Buku"
        /* Inisialisasi objek */
        dbRealtime = Firebase.database.reference

        binding.idbtnTambah.setOnClickListener {
            checkField()
        }

        binding.idivImage.visibility = View.INVISIBLE
        binding.idbtnUpload.setOnClickListener {
            selectImage()
        }

        /* Inisialisasi objek */
        fStorage = FirebaseStorage.getInstance("gs://databuku-49a78.appspot.com")
        sRef = fStorage.reference
    }

    /* implicit intent (untuk memilih foto di file hp)*/
    private fun selectImage() {
        val intent = Intent()
        intent.type ="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* Jika foto nya sudah di pilih ini akan berjalan */
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            binding.idivImage.visibility = View.VISIBLE

            imageUri = data.data!!
            binding.idivImage.setImageURI(imageUri)
            /* button upload akan tidak terlihat (invisible */
            binding.idbtnUpload.visibility = View.INVISIBLE
        }
    }

    /* Untuk upload foto ke firebase storage dan di dalamnya terdapat fungsi untuk menyimpan ke realtime firebase */
    private fun uploadImage(judul: String, penerbit: String, tahunTerbit: String, penulis: String, imageUri: Uri) {
        val UUID = UUID.randomUUID().toString()
        val image = sRef.child("uploads/images/$UUID.jpg")
        uploadTask = image.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            image.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()

                message("Berhasil di upload")
                clearEditText()

                saveDbRealtime(judul, penerbit, tahunTerbit, penulis, downloadUri)
                binding.idivImage.visibility = View.INVISIBLE
                binding.idbtnUpload.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Image gagal di upload", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /* Untuk menyimpan data ke realtime firebase */
    fun saveDbRealtime(judul: String, penerbit: String, tahunTerbit: String, penulis: String, urlFoto: String) {
        val dataBuku = DataBukuModel(judul, penerbit, penulis, tahunTerbit,urlFoto)
        val uuid = UUID.randomUUID().toString()
        dbRealtime.child("buku").child(uuid).setValue(dataBuku)
    }
    /* Untuk menghapus seluruh edittext */
    fun clearEditText() {
        binding.idedtJudulbuku.text.clear()
        binding.idedtPenerbit.text.clear()
        binding.idedtTahunterbit.text.clear()
        binding.idedtPenulis.text.clear()
    }
    /* untuk mengecek apakah user sudah memenuhi kondisi nya ?*/
    fun checkField() {
        val judulBuku = binding.idedtJudulbuku.text.toString()
        val penerbit = binding.idedtPenerbit.text.toString()
        val tahunTerbit = binding.idedtTahunterbit.text.toString()
        val penulis = binding.idedtPenulis.text.toString()

        var checkField = false
        if (judulBuku.isEmpty()) {
            checkField = true
            binding.idedtJudulbuku.error = "Silahkan Isi"
            binding.idedtJudulbuku.requestFocus()
        }

        if (penerbit.isEmpty()) {
            checkField = true
            binding.idedtPenerbit.error = "Silahkan Isi"
            binding.idedtPenerbit.requestFocus()
        }

        if (tahunTerbit.isEmpty()) {
            checkField = true
            binding.idedtTahunterbit.error = "Silahkan Isi"
            binding.idedtTahunterbit.requestFocus()
        }

        if (penulis.isEmpty()) {
            checkField = true
            binding.idedtPenulis.error = "Silahkan Isi"
            binding.idedtPenulis.requestFocus()
        }

        if (binding.idivImage.isInvisible){
            checkField = true
            Toast.makeText(this@TambahBukuActivity, "Pilih foto terlebih dahulu", Toast.LENGTH_SHORT).show()
        }

        if (!checkField) {
            Log.e("url", binding.idivImage.toString())
            uploadImage(judulBuku, penerbit, tahunTerbit, penulis, imageUri)
        }
    }
    /* untuk Toast */
    fun message(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}