package com.example.ras_chitato.beranda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ras_chitato.BaseActivity
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentBerandaBinding

class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.nav_beranda)
        }

        // Ambil username yang disimpan saat login berhasil
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "").orEmpty()
        binding.txtUsername.text = username
        binding.txtInisial.text = username.take(1).uppercase()

        // Menu cepat: minta BaseActivity memilih menu di Bottom Navigation
        binding.btnCepatTbs.setOnClickListener {
            (requireActivity() as BaseActivity).pilihMenu(R.id.nav_tbs)
        }
        binding.btnCepatKartu.setOnClickListener {
            (requireActivity() as BaseActivity).pilihMenu(R.id.nav_kartu)
        }
        binding.btnCepatSimpanan.setOnClickListener {
            (requireActivity() as BaseActivity).pilihMenu(R.id.nav_simpanan)
        }

        // Intent: pindah ke halaman detail snack saat menu "My Project" diklik
        binding.btnMyProject.setOnClickListener {
            val intent = Intent(requireContext(), SnackActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
