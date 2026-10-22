package com.example.ras_chitato

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.ras_chitato.beranda.BerandaFragment
import com.example.ras_chitato.databinding.ActivityBaseBinding
import com.example.ras_chitato.kartu.KartuFragment
import com.example.ras_chitato.profil.ProfilFragment
import com.example.ras_chitato.simpanan.SimpananFragment
import com.example.ras_chitato.tbs.TbsFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // Insets dipakai di sini saja, supaya BottomNavigationView tidak menambah jarak bawah lagi
            WindowInsetsCompat.CONSUMED
        }

        /** BerandaFragment sebagai fragment default (tidak diulang saat layar diputar) */
        if (savedInstanceState == null) {
            replaceFragment(BerandaFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_beranda -> {
                    replaceFragment(BerandaFragment())
                    true
                }
                R.id.nav_tbs -> {
                    replaceFragment(TbsFragment())
                    true
                }
                R.id.nav_kartu -> {
                    replaceFragment(KartuFragment())
                    true
                }
                R.id.nav_simpanan -> {
                    replaceFragment(SimpananFragment())
                    true
                }
                R.id.nav_profil -> {
                    replaceFragment(ProfilFragment())
                    true
                }
                else -> false
            }
        }

        // Klik menu yang sedang aktif tidak memuat ulang fragment
        binding.bottomNavView.setOnItemReselectedListener { }
    }

    /** Dipanggil dari fragment (misalnya menu cepat di Beranda) untuk pindah menu */
    fun pilihMenu(itemId: Int) {
        binding.bottomNavView.selectedItemId = itemId
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            //.addToBackStack(null) -> dinonaktifkan agar saat back langsung keluar aplikasi
            .commit()
    }
}
