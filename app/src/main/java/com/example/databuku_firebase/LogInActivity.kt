package com.example.databuku_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databuku_firebase.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    /* View binding */
    private lateinit var binding: ActivityLogInBinding

    /* buat objek untuk instance Firebase Authentication */
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Please Login Your Account"

        /* inisialisasi objek */
        auth = Firebase.auth

        binding.idtvTextintent.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.idbtnLogin.setOnClickListener {
            val email = binding.idedtEmail.text.toString()
            val password = binding.idedtPassword.text.toString()

            var check = false
            if (email.isEmpty()) {
                check = true
                binding.idedtEmail.error = "Isi dulu"
                binding.idedtEmail.requestFocus()
            }
            if (password.isEmpty()) {
                check = true
                binding.idedtPassword.error = "Isi dulu"
                binding.idedtPassword.requestFocus()
            }
            if (!check) {
                loginUser(email, password)
            }
        }
    }

    /* Mengecek inputan user untuk di cek di authentication firebase */
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login succes", Toast.LENGTH_SHORT).show()
                Intent(this, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* Mengecek apakah sudah login?
    *  Jika sudah akan langsung masuk ke home tanpa login lagi*/
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this, HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

}