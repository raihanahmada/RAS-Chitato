package com.example.ras_chitato.kartu

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** Membuat gambar QR Code dari teks memakai library ZXing (core) */
object QrCodeGenerator {

    fun buatQr(isi: String, ukuranPx: Int): Bitmap {
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M, // tetap terbaca walau sebagian tertutup
            EncodeHintType.MARGIN to 1,                                // bingkai putih tipis
            EncodeHintType.CHARACTER_SET to "UTF-8"
        )

        // 1) ZXing mengubah teks menjadi matriks kotak hitam/putih
        val matriks = QRCodeWriter().encode(isi, BarcodeFormat.QR_CODE, ukuranPx, ukuranPx, hints)

        // 2) Setiap titik matriks diubah jadi warna piksel
        val piksel = IntArray(ukuranPx * ukuranPx)
        for (y in 0 until ukuranPx) {
            for (x in 0 until ukuranPx) {
                piksel[y * ukuranPx + x] = if (matriks[x, y]) WARNA_QR else Color.WHITE
            }
        }

        // 3) Piksel disusun menjadi Bitmap yang bisa ditampilkan di ImageView
        return Bitmap.createBitmap(piksel, ukuranPx, ukuranPx, Bitmap.Config.ARGB_8888)
    }

    private const val WARNA_QR = 0xFF0F172A.toInt() // teks_gelap di palet SIKUDA
}
