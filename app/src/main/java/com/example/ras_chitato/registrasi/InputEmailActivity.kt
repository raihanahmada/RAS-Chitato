package com.example.ras_chitato.registrasi

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.ActivityInputEmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.jvm.java

class InputEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInputEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnKembali.setOnClickListener { finish() }

        binding.btnLanjut.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim().lowercase()

            when {
                // Validasi 1: email tidak boleh kosong
                email.isEmpty() ->
                    tampilkanError(getString(R.string.error_email_kosong))

                // Validasi 2: format email benar dan domain harus @gmail.com
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith("@gmail.com") ->
                    tampilkanError(getString(R.string.error_email_gmail))

                // Valid: buka halaman Registrasi dan bawa email lewat Intent
                else -> {
                    val intent = Intent(this, RegistrasiActivity::class.java)
                    intent.putExtra(RegistrasiActivity.EXTRA_EMAIL, email)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    // Error ditampilkan dengan MaterialAlertDialog
    private fun tampilkanError(pesan: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.email_gagal_judul)
            .setMessage(pesan)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}