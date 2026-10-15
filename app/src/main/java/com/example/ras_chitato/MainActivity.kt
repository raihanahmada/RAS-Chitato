package com.example.ras_chitato

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ras_chitato.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            // Header hijau diperpanjang sampai di belakang status bar
            binding.header.setPadding(
                binding.header.paddingLeft,
                systemBars.top + resources.getDimensionPixelSize(R.dimen.jarak_header),
                binding.header.paddingRight,
                binding.header.paddingBottom
            )
            insets
        }

        // Ambil username yang disimpan saat login berhasil
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "").orEmpty()
        binding.txtUsername.text = username
        binding.txtInisial.text = username.take(1).uppercase()

        // Intent: pindah ke halaman detail snack saat menu "My Project" diklik
        binding.btnMyProject.setOnClickListener {
            val intent = Intent(this, SnackActivity::class.java)
            startActivity(intent)
        }

        // Intent: pindah ke halaman Profil Developer
        binding.btnProfil.setOnClickListener {
            val intent = Intent(this, ProfilDeveloperActivity::class.java)
            startActivity(intent)
        }

        // Logout: konfirmasi dulu lewat AlertDialog
        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.logout_judul)
                .setMessage(R.string.logout_pesan)
                .setPositiveButton(R.string.ya) { dialog, _ ->
                    // Hapus semua data di "user_pref" (isLogin & username)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    dialog.dismiss()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
                .setNegativeButton(R.string.batal) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
