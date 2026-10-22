package com.example.ras_chitato.tbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentTbsBinding
import com.example.ras_chitato.databinding.ItemSetoranBinding

class TbsFragment : Fragment() {

    private var _binding: FragmentTbsBinding? = null
    private val binding get() = _binding!!

    // Data contoh riwayat setoran (nanti diganti data dari API SIKUDA)
    private data class Setoran(
        val tanggal: String,
        val berat: String,
        val nilai: String,
        val kavling: String,
        val sudahDibayar: Boolean
    )

    private val daftarSetoran = listOf(
        Setoran("28 Sep 2026", "TBS 1.240 kg", "Rp 3.534.000", "Kavling A2 · Rp 2.850/kg", false),
        Setoran("14 Sep 2026", "TBS 1.170 kg", "Rp 3.276.000", "Kavling A1 · Rp 2.800/kg", true),
        Setoran("29 Agt 2026", "TBS 1.080 kg", "Rp 2.991.600", "Kavling A2 · Rp 2.770/kg", true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTbsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.tbs_judul)
        }

        tampilkanSetoran()
    }

    // Isi containerSetoran: setiap data -> inflate item_setoran.xml -> addView
    private fun tampilkanSetoran() {
        for (setoran in daftarSetoran) {
            val item = ItemSetoranBinding.inflate(layoutInflater, binding.containerSetoran, false)
            item.txtTanggal.text = setoran.tanggal
            item.txtBerat.text = setoran.berat
            item.txtNilai.text = setoran.nilai
            item.txtKavling.text = setoran.kavling

            // Warna kartu mengikuti status: hijau = dibayar, oranye = diproses
            val warnaStatus = if (setoran.sudahDibayar) R.color.hijau_utama else R.color.oranye_tbs
            val warnaLatar = if (setoran.sudahDibayar) R.color.hijau_muda else R.color.oranye_muda
            item.txtStatus.setText(if (setoran.sudahDibayar) R.string.status_dibayar else R.string.status_diproses)
            item.txtStatus.backgroundTintList = ContextCompat.getColorStateList(requireContext(), warnaStatus)
            item.kartuSetoran.setCardBackgroundColor(ContextCompat.getColor(requireContext(), warnaLatar))
            item.kartuSetoran.strokeColor = ContextCompat.getColor(requireContext(), warnaStatus)

            binding.containerSetoran.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
