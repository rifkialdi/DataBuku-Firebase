package com.example.databuku_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databuku_firebase.databinding.ActivityHomeBinding
import com.example.databuku_firebase.model.DataBukuModel
import com.example.databuku_firebase.rvadapter.HomeAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var data: ArrayList<DataBukuModel>
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


        binding.idrvDatabuku.layoutManager = LinearLayoutManager(this)
        binding.idrvDatabuku.setHasFixedSize(true)
        data = arrayListOf<DataBukuModel>()
        getDataBuku()
    }

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
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}