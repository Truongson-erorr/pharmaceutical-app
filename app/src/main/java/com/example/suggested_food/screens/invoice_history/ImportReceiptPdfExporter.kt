package com.example.suggested_food.screens.invoice_history

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.suggested_food.models.ImportReceipt
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object ImportReceiptPdfExporter {
    fun export(context: Context, data: ImportReceipt): File {
        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()

        val currency = NumberFormat.getInstance(Locale("vi", "VN"))
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        var y = 50

        fun draw(text: String, size: Float = 14f, bold: Boolean = false) {
            paint.textSize = size
            paint.isFakeBoldText = bold
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 40
        }

        draw("HÓA ĐƠN NHẬP", 20f, true)
        y += 20

        draw("Mã số phiếu: ${data.id}")
        draw("Người nhập: ${data.user}")
        draw("Ngày: ${formatter.format(Date(data.date))}")

        y += 20

        draw("Sản phẩm: ${data.productName}")
        draw("Đơn vị: ${data.unit}")
        draw("Số lượng: ${data.quantity}")

        draw("Lô: ${data.lot}")
        draw("HSD: ${data.expiryDate}")

        draw("Nhà cung cấp: ${data.supplier}")

        if (data.note.isNotEmpty()) {
            draw("Ghi chú: ${data.note}")
        }

        y += 20

        draw("Giá nhập: ${currency.format(data.price)} đ")
        draw("Tổng tiền: ${currency.format(data.totalPrice)} đ", bold = true)

        pdfDocument.finishPage(page)

        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "hoa_don_${data.id}.pdf"
        )

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }
}