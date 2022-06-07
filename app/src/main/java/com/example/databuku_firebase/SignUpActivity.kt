package com.example.databuku_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databuku_firebase.databinding.ActivitySignUpBinding
import com.example.databuku_firebase.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SignUpActivity : AppCompatActivity() {
    /* View Binding */
    private lateinit var binding: ActivitySignUpBinding

    /* Realtime & Authentication */
    private lateinit var dbRealtime: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Let's Register"

        /* Inisialisasi Db Realtime & Authentication */
        dbRealtime = Firebase.database.reference
        auth = Firebase.auth

        binding.idbtnSignup.setOnClickListener {
            val name = binding.idedtName.text.toString()
            val email = binding.idedtEmail.text.toString()
            val password = binding.idedtPassword.text.toString()

            var check = false
            if (name.isEmpty()) {
                check = true
                binding.idedtName.error = "Fill in name"
                binding.idedtName.requestFocus()
            }
            if (email.isEmpty()) {
                check = true
                binding.idedtEmail.error = "Fill in email"
                binding.idedtEmail.requestFocus()
            }
            if (password.isEmpty()) {
                check = true
                binding.idedtPassword.error = "Fill in password"
                binding.idedtPassword.requestFocus()
            }
            if (!check) {
                newAuth(email, name, password)
            }
        }


    }

    fun newAcc(name: String, email: String, password: String) {
        val userUid = auth.uid
        val user = UserModel(name, email, password, userUid)
        val uuid = UUID.randomUUID().toString()
        dbRealtime.child("Users").child(uuid).setValue(user)
    }

    fun newAuth(email: String, name: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                newAcc(name, email, password)
                Toast.makeText(this, "successfully registered", Toast.LENGTH_SHORT).show()

                Intent(this, LogInActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            } else {
                Toast.makeText(this, "Create account Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}