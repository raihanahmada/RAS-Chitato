package com.example.ras_chitato

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.ras_chitato.databinding.ActivityAuthBinding
import com.example.ras_chitato.registrasi.InputEmailActivity
import com.example.ras_chitato.registrasi.RegistrasiActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Kode ini harus selalu dipanggil saat butuh akses "user_pref"
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Data akun hasil Registrasi (file terpisah agar tidak ikut terhapus saat logout)
        val akunPref = getSharedPreferences(RegistrasiActivity.PREF_AKUN, MODE_PRIVATE)

        // Hapus pesan error saat user mulai mengetik ulang
        binding.inputUsername.doAfterTextChanged { binding.layoutUsername.error = null }
        binding.inputPassword.doAfterTextChanged { binding.layoutPassword.error = null }

        // Soal 1: tombol Register With Gmail -> halaman input email
        binding.btnRegisterGmail.setOnClickListener {
            startActivity(Intent(this, InputEmailActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString()

            // 1) Validasi: username & password tidak boleh kosong
            if (username.isEmpty()) {
                binding.layoutUsername.error = getString(R.string.error_username_kosong)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.layoutPassword.error = getString(R.string.error_password_kosong)
                return@setOnClickListener
            }

            // 2) Autentikasi, boleh masuk jika salah satu kondisi terpenuhi:
            //    a. username == password (dari laporan sebelumnya)
            //    b. username & password sama dengan data registrasi di SharedPreferences
            val usernameTersimpan = akunPref.getString(RegistrasiActivity.KEY_USERNAME, null)
            val passwordTersimpan = akunPref.getString(RegistrasiActivity.KEY_PASSWORD, null)
            val cocokDataRegistrasi = usernameTersimpan != null &&
                    username == usernameTersimpan &&
                    password == passwordTersimpan

            if (username == password || cocokDataRegistrasi) {
                // Simpan status login & username ke SharedPreferences
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.apply()

                val intent = Intent(this, BaseActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // 3) Gagal: tampilkan MaterialAlertDialog
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.login_gagal_judul)
                    .setMessage(R.string.login_gagal_pesan)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}
