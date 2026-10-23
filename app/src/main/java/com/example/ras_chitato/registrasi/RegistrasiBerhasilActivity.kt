package com.example.ras_chitato.registrasi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.ActivityRegistrasiBerhasilBinding

class RegistrasiBerhasilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrasiBerhasilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrasiBerhasilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Baca kembali data dari SharedPreferences untuk membuktikan data tersimpan
        val akunPref = getSharedPreferences(RegistrasiActivity.PREF_AKUN, MODE_PRIVATE)
        binding.txtDataAkun.text = getString(
            R.string.berhasil_data,
            akunPref.getString(RegistrasiActivity.KEY_NAMA, "-"),
            akunPref.getString(RegistrasiActivity.KEY_EMAIL, "-"),
            akunPref.getString(RegistrasiActivity.KEY_USERNAME, "-")
        )

        // InputEmail & Registrasi sudah di-finish(), jadi finish() di sini
        // langsung kembali ke halaman Login (AuthActivity)
        binding.btnKeLogin.setOnClickListener { finish() }
    }
}
