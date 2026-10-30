package com.example.ras_chitato.kartu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentKartuBinding
import com.google.zxing.WriterException

class KartuFragment : Fragment() {

    private var _binding: FragmentKartuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKartuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.kartu_judul)
        }

        // Nama di kartu diambil dari username yang disimpan saat login
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "").orEmpty()
        binding.txtNamaAnggota.text = username

        tampilkanQr(getString(R.string.nomor_anggota), username)
    }

    // QR berisi nomor anggota + username, dipindai petugas timbangan saat setor TBS
    private fun tampilkanQr(nomorAnggota: String, username: String) {
        val isiQr = "SIKUDA;$nomorAnggota;$username"
        val ukuranPx = resources.getDimensionPixelSize(R.dimen.ukuran_qr)
        try {
            binding.imgQr.setImageBitmap(QrCodeGenerator.buatQr(isiQr, ukuranPx))
            binding.txtIsiQr.text = getString(R.string.qr_isi, nomorAnggota, username)
        } catch (e: WriterException) {
            binding.txtIsiQr.setText(R.string.qr_gagal)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
