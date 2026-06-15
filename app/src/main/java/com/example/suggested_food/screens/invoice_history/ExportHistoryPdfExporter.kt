package com.example.suggested_food.screens.invoice_history

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import com.example.suggested_food.models.ExportReceipt
import com.example.suggested_food.utils.generateQR
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object ExportHistoryImageExporter {

    fun export(context: Context, data: ExportReceipt, userName: String): Uri {

        val currency = NumberFormat.getInstance(Locale("vi", "VN"))
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val width = 800f
        val padding = 50f
        val contentRight = width - padding

        val lineHeight = 70f

        val bitmap = Bitmap.createBitmap(
            width.toInt(),
            1600,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 46f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val subTitlePaint = Paint().apply {
            color = Color.GRAY
            textSize = 28f
            isAntiAlias = true
        }

        val labelPaint = Paint().apply {
            color = Color.BLACK
            textSize = 30f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val valuePaint = Paint().apply {
            color = Color.BLACK
            textSize = 30f
            isFakeBoldText = false
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 2f
        }

        var y = 200f

        fun divider() {
            y += 15f
            canvas.drawLine(padding, y, contentRight, y, linePaint)
            y += 50f
        }

        fun drawCenter(text: String, paint: Paint) {
            val x = (width - paint.measureText(text)) / 2
            canvas.drawText(text, x, y, paint)
            y += 80f
        }

        fun drawRow(label: String, value: String) {

            canvas.drawText(label, padding, y, labelPaint)

            val valueWidth = valuePaint.measureText(value)
            canvas.drawText(value, contentRight - valueWidth, y, valuePaint)

            y += lineHeight
        }

        canvas.drawText("NHÀ THUỐC SYSTEM", padding, y, titlePaint)
        y += 60f

        canvas.drawText("PHIẾU XUẤT KHO", padding, y, subTitlePaint)
        y += 50f
        val qrBitmap =
            generateQR("PRODUCT:${data.productName}", 250)

        val qrX = (width - qrBitmap.width) / 2
        canvas.drawBitmap(qrBitmap, qrX, y, null)

        y += qrBitmap.height + 5f
        divider()
        drawRow("Mã phiếu: ", data.id)
        drawRow("Người xuất: ", userName)
        drawRow(
            "Ngày xuất: ",
            try {
                dateFormat.format(Date(data.date))
            } catch (e: Exception) {
                data.date.toString()
            }
        )
        divider()

        drawRow("Sản phẩm: ", data.productName)
        drawRow("Số lượng: ", data.quantity.toString())
        drawRow("Hạn sử dụng: ", data.expiryDate ?: "-")
        divider()

        drawRow("Khách hàng: ", data.customer)
        drawRow("Số điện thoại: ", data.customerPhone)
        divider()

        drawRow(
            "Giá xuất: ",
            "${currency.format(data.price)} đ"
        )

        drawRow(
            "Tổng tiền: ",
            "${currency.format(data.totalPrice)} đ"
        )

        val footerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 34f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val footerText = "Cảm ơn bạn!"
        val x = (width - footerPaint.measureText(footerText)) / 2
        canvas.drawText(footerText, x, y, footerPaint)

        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "phieu_xuat_${data.id}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Suggested_Food")
        }

        val uri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!

        resolver.openOutputStream(uri)?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return uri
    }
}