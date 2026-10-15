package com.example.ras_chitato

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ras_chitato.databinding.ActivityProfilDeveloperBinding

class ProfilDeveloperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfilDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tombol kembali ke MainActivity
        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Klik baris sosmed -> buka tautan lewat Intent implisit
        binding.rowGithub.setOnClickListener { bukaTautan(getString(R.string.link_github)) }
        binding.rowInstagram.setOnClickListener { bukaTautan(getString(R.string.link_instagram)) }
        binding.rowEmail.setOnClickListener { bukaTautan(getString(R.string.link_email)) }
    }

    private fun bukaTautan(link: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this,
                R.string.link_gagal, Toast.LENGTH_SHORT).show()
        }
    }
}
