package com.example.ras_chitato.profil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ras_chitato.AuthActivity
import com.example.ras_chitato.BaseActivity
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentProfilBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.profil_fragment_judul)
        }

        // Di Fragment, akses SharedPreferences lewat requireContext()
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "").orEmpty()
        binding.txtUsername.text = username
        binding.txtInisial.text = username.take(1).uppercase()

        // Pindah ke menu Kartu di Bottom Navigation
        binding.btnKartuSaya.setOnClickListener {
            (requireActivity() as BaseActivity).pilihMenu(R.id.nav_kartu)
        }

        // Intent: pindah ke halaman Profil Developer
        binding.btnProfilDeveloper.setOnClickListener {
            val intent = Intent(requireContext(), ProfilDeveloperActivity::class.java)
            startActivity(intent)
        }

        // Logout: konfirmasi dulu lewat AlertDialog
        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.logout_judul)
                .setMessage(R.string.logout_pesan)
                .setPositiveButton(R.string.ya) { dialog, _ ->
                    // Hapus semua data di "user_pref" (isLogin & username)
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    dialog.dismiss()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    // Tutup BaseActivity (bukan fragment-nya)
                    requireActivity().finish()
                }
                .setNegativeButton(R.string.batal) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
