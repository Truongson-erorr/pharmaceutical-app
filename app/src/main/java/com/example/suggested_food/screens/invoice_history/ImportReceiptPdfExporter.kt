package com.example.suggested_food.screens.invoice_history

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import com.example.suggested_food.models.ImportReceipt
import com.example.suggested_food.utils.generateQR
import java.text.NumberFormat
import java.util.*

object ImportReceiptImageExporter {

    fun export(context: Context, data: ImportReceipt): Uri {
        val currency = NumberFormat.getInstance(Locale("vi", "VN"))

        val width = 800f
        val padding = 50f
        val contentRight = width - padding

        val lineHeight = 70f
        val sectionSpacing = 30f

        val fields = listOf(
            "Mã phiếu" to data.id,
            "Người nhập" to data.user,
            "Ngày" to data.date,
            "Sản phẩm" to data.productName,
            "Số lượng" to data.quantity.toString(),
            "HSD" to data.expiryDate,
            "Nhà cung cấp" to data.supplier,
            "Giá nhập" to "${currency.format(data.price)} đ",
            "Tổng tiền" to "${currency.format(data.totalPrice)} đ"
        )
        val height = 1700f

        val bitmap = Bitmap.createBitmap(
            width.toInt(),
            height.toInt(),
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
            color = Color.DKGRAY
            textSize = 30f
            isAntiAlias = true
        }

        val valuePaint = Paint().apply {
            color = Color.BLACK
            textSize = 32f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 2f
        }

        var y = 200f

        canvas.drawText("NHÀ THUỐC SYSTEM", padding, y, titlePaint)
        y += 60f

        canvas.drawText("HÓA ĐƠN NHẬP KHO", padding, y, subTitlePaint)
        y += 50f

        val qrBitmap = generateQR("PRODUCT:${data.productName}", 250)
        val qrX = (width - qrBitmap.width) / 2
        canvas.drawBitmap(qrBitmap, qrX, y, null)
        y += qrBitmap.height + 5f

        canvas.drawLine(padding, y, contentRight, y, linePaint)
        y += 60f

        fields.forEachIndexed { index, (label, value) ->

            canvas.drawText(label, padding, y, labelPaint)

            val valueWidth = valuePaint.measureText(value.toString())
            canvas.drawText(value.toString(), contentRight - valueWidth, y, valuePaint)

            y += lineHeight

            if (index % 4 == 3) {
                canvas.drawLine(padding, y - 20f, contentRight, y - 20f, linePaint)
                y += sectionSpacing
            }
        }

        y += 40f
        canvas.drawLine(padding, y, contentRight, y, linePaint)
        y += 60f

        val footerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 34f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val footerText = "Cảm ơn bạn"
        val x = (width - footerPaint.measureText(footerText)) / 2
        canvas.drawText(footerText, x, y, footerPaint)

        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "hoa_don_${data.id}.png")
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