package com.example.ras_chitato.simpanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentSimpananBinding
import com.example.ras_chitato.databinding.ItemMutasiBinding

class SimpananFragment : Fragment() {

    private var _binding: FragmentSimpananBinding? = null
    private val binding get() = _binding!!

    // Data contoh mutasi simpanan (nanti diganti data dari API SIKUDA)
    private data class Mutasi(
        val jenis: String,
        val keterangan: String,
        val jumlah: String,
        val masuk: Boolean
    )

    private val daftarMutasi = listOf(
        Mutasi("Simpanan Wajib", "30 Sep 2026 · Potong TBS", "+Rp 50.000", true),
        Mutasi("Simpanan Wajib", "31 Agt 2026 · Potong TBS", "+Rp 50.000", true),
        Mutasi("Pembelian Pupuk", "20 Agt 2026 · Potong simpanan", "-Rp 25.000", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.simpanan_judul)
        }

        tampilkanMutasi()
    }

    // Isi containerMutasi: setiap data -> inflate item_mutasi.xml -> addView
    private fun tampilkanMutasi() {
        for (mutasi in daftarMutasi) {
            val item = ItemMutasiBinding.inflate(layoutInflater, binding.containerMutasi, false)
            item.txtJenis.text = mutasi.jenis
            item.txtKeterangan.text = mutasi.keterangan
            item.txtJumlah.text = mutasi.jumlah

            // Dana masuk = hijau + panah masuk, dana keluar = merah + panah keluar
            val warna = if (mutasi.masuk) R.color.hijau_utama else R.color.merah_bahaya
            val latar = if (mutasi.masuk) R.color.hijau_muda else R.color.merah_muda
            item.imgArah.setImageResource(if (mutasi.masuk) R.drawable.ic_masuk else R.drawable.ic_keluar_dana)
            item.imgArah.imageTintList = ContextCompat.getColorStateList(requireContext(), warna)
            item.imgArah.backgroundTintList = ContextCompat.getColorStateList(requireContext(), latar)
            item.txtJumlah.setTextColor(ContextCompat.getColor(requireContext(), warna))

            binding.containerMutasi.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
