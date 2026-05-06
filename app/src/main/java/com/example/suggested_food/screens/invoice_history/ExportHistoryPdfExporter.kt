package com.example.suggested_food.screens.invoice_history

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.suggested_food.models.ExportReceipt
import java.io.File
import java.io.FileOutputStream

object ExportHistoryPdfExporter {

    fun export(context: Context, data: ExportReceipt): File {

        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()

        var y = 60f

        fun draw(text: String, size: Float = 14f, bold: Boolean = false) {
            paint.textSize = size
            paint.isFakeBoldText = bold
            canvas.drawText(text, 40f, y, paint)
            y += 40f
        }

        draw("PHIẾU XUẤT KHO", 22f, true)
        y += 20f

        draw("Mã phiếu: ${data.id}")
        draw("Người xuất: ${data.user}")
        draw("Ngày: ${data.date}")

        y += 20f

        draw("Sản phẩm: ${data.productName}")
        draw("Số lượng: ${data.quantity}")
        draw("Khách hàng: ${data.customer}")

        y += 20f
        draw("Tổng tiền: ${data.totalPrice}", 16f, true)

        pdfDocument.finishPage(page)

        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "phieu_xuat_${data.id}.pdf"
        )

        FileOutputStream(file).use { output ->
            pdfDocument.writeTo(output)
        }

        pdfDocument.close()
        return file
    }
}