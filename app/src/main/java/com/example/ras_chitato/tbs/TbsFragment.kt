package com.example.ras_chitato.tbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.ras_chitato.R
import com.example.ras_chitato.databinding.FragmentTbsBinding
import com.example.ras_chitato.databinding.ItemSetoranBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Locale

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

    // Data contoh harga TBS harian 2 Sep - 1 Okt 2026 (Rp/kg), dipakai grafik
    private val hargaHarian = listOf(
        2670, 2675, 2680, 2690, 2700, 2705, 2715, 2710, 2705, 2720,
        2730, 2740, 2735, 2730, 2745, 2760, 2770, 2775, 2780, 2790,
        2785, 2780, 2795, 2800, 2810, 2805, 2815, 2825, 2840, 2850
    )
    private val tanggalHarian = (2..30).map { "$it Sep" } + "1 Okt"

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

        // Grafik default 30 hari, tombol 7H / 30H mengganti rentang
        tampilkanGrafik(30)
        binding.toggleRentang.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) tampilkanGrafik(if (checkedId == R.id.btn7Hari) 7 else 30)
        }

        tampilkanSetoran()
    }

    // Grafik garis harga TBS memakai MPAndroidChart: data -> Entry -> LineDataSet -> LineData -> LineChart
    private fun tampilkanGrafik(jumlahHari: Int) {
        val harga = hargaHarian.takeLast(jumlahHari)
        val tanggal = tanggalHarian.takeLast(jumlahHari)
        val hijau = ContextCompat.getColor(requireContext(), R.color.hijau_utama)
        val abu = ContextCompat.getColor(requireContext(), R.color.teks_abu)
        val font = ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)
        val rupiah = { rp: Int -> String.format(Locale.forLanguageTag("id-ID"), "%,d", rp) }

        // 1) Setiap harga jadi satu titik (x = urutan hari, y = harga)
        val titik = harga.mapIndexed { i, rp -> Entry(i.toFloat(), rp.toFloat()) }

        // 2) Kumpulan titik + gaya garis
        val dataSet = LineDataSet(titik, getString(R.string.grafik_label)).apply {
            color = hijau
            lineWidth = 2.5f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_grafik_tbs)
            highLightColor = ContextCompat.getColor(requireContext(), R.color.hijau_tua)
            setDrawHorizontalHighlightIndicator(false)
        }

        // 3) Pasang data ke LineChart dan atur tampilan sumbu
        binding.chartHarga.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            setScaleEnabled(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(tanggal)
                granularity = 1f
                labelCount = 4
                setDrawGridLines(false)
                textColor = abu
                typeface = font
            }
            extraBottomOffset = 6f  // ruang untuk label tanggal
            axisLeft.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float) = rupiah(value.toInt())  // 2.840, bukan 2,840
                }
                setDrawAxisLine(false)
                gridColor = ContextCompat.getColor(requireContext(), R.color.garis)
                textColor = abu
                typeface = font
            }
            highlightValue(titik.last().x, 0)  // tandai harga terbaru
            animateX(700)
        }

        binding.txtRingkasGrafik.text = getString(R.string.grafik_ringkas, rupiah(harga.min()), rupiah(harga.max()))
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
