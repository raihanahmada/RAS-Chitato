package com.example.ras_chitato.registrasi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.ActivityRegistrasiBinding

class RegistrasiActivity : AppCompatActivity() {

    companion object {
        // Kunci Intent untuk membawa email dari InputEmailActivity
        const val EXTRA_EMAIL = "extra_email"

        // SharedPreferences data registrasi.
        // Dipisah dari "user_pref" karena logout memanggil clear() pada "user_pref",
        // kalau digabung akun yang sudah didaftarkan ikut terhapus.
        const val PREF_AKUN = "akun_pref"
        const val KEY_NAMA = "nama"
        const val KEY_EMAIL = "email"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
    }

    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Email otomatis terisi dari halaman Register With Gmail dan tidak bisa diubah
        val email = intent.getStringExtra(EXTRA_EMAIL).orEmpty()
        binding.inputEmail.setText(email)
        binding.layoutEmail.isEnabled = false

        binding.btnKembali.setOnClickListener { finish() }

        // Hapus pesan error saat user mengetik ulang
        binding.inputNama.doAfterTextChanged { binding.layoutNama.error = null }
        binding.inputUsername.doAfterTextChanged { binding.layoutUsername.error = null }
        binding.inputPassword.doAfterTextChanged { binding.layoutPassword.error = null }

        binding.btnRegistrasi.setOnClickListener {
            val nama = binding.inputNama.text.toString().trim()
            // Username tidak di-trim supaya spasi di awal/akhir juga terdeteksi
            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()

            var valid = true

            // Semua field wajib diisi
            if (nama.isEmpty()) {
                binding.layoutNama.error = getString(R.string.error_nama_kosong)
                valid = false
            }
            if (email.isEmpty()) {
                binding.layoutEmail.error = getString(R.string.error_email_registrasi)
                valid = false
            }

            // Username wajib diisi dan tidak boleh mengandung spasi
            if (username.isEmpty()) {
                binding.layoutUsername.error = getString(R.string.error_username_kosong)
                valid = false
            } else if (username.any { it.isWhitespace() }) {
                binding.layoutUsername.error = getString(R.string.error_username_spasi)
                valid = false
            }

            // Password wajib diisi dan minimal 6 karakter
            if (password.isEmpty()) {
                binding.layoutPassword.error = getString(R.string.error_password_kosong)
                valid = false
            } else if (password.length < 6) {
                binding.layoutPassword.error = getString(R.string.error_password_pendek)
                valid = false
            }

            if (!valid) return@setOnClickListener

            // Lolos validasi: simpan seluruh data registrasi ke SharedPreferences
            val akunPref = getSharedPreferences(PREF_AKUN, MODE_PRIVATE)
            val editor = akunPref.edit()
            editor.putString(KEY_NAMA, nama)
            editor.putString(KEY_EMAIL, email)
            editor.putString(KEY_USERNAME, username)
            editor.putString(KEY_PASSWORD, password)
            editor.apply()

            // Tampilkan halaman informasi "Registrasi Berhasil"
            startActivity(Intent(this, RegistrasiBerhasilActivity::class.java))
            finish()
        }
    }
}
