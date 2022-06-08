package com.example.databuku_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databuku_firebase.databinding.ActivityHomeBinding
import com.example.databuku_firebase.model.DataBukuModel
import com.example.databuku_firebase.rvadapter.HomeAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    /* View binding  */
    private lateinit var binding: ActivityHomeBinding
    /* Deklarasi variabel untuk meyimpan data */
    private lateinit var data: ArrayList<DataBukuModel>
    /* Buat objek untuk instance Firebase Realtime */
    private lateinit var dbRealtime: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Data Buku"

        binding.idtvLogout.setOnClickListener {
            Firebase.auth.signOut()
            Intent(this, LogInActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        binding.idbtnTambahbuku.setOnClickListener {
            startActivity(Intent(this, TambahBukuActivity::class.java))
        }

        /* inisialisasi untuk recycler view nya */
        binding.idrvDatabuku.layoutManager = LinearLayoutManager(this)
        binding.idrvDatabuku.setHasFixedSize(true)
        data = arrayListOf<DataBukuModel>()

        getDataBuku()
    }

    /* Untuk mengambil data di realtime database dan di tampilkan ke recycler view */
    fun getDataBuku() {
        dbRealtime = FirebaseDatabase.getInstance().getReference("buku")
        dbRealtime.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    data.clear()
                    for (userSnapshoot in snapshot.children) {
                        val user = userSnapshoot.getValue(DataBukuModel::class.java)
                        data.add(user!!)
                    }
                    binding.idrvDatabuku.adapter = HomeAdapter(data)
                    binding.progressBar.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}