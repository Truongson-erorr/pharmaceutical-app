package com.example.suggested_food.screens.invoice_history

import android.content.Context
import com.example.suggested_food.models.ImportReceipt
import java.io.File

object ImportReceiptCsvExporter {

    fun export(context: Context, data: ImportReceipt): File {

        val file = File(
            context.getExternalFilesDir(null),
            "hoa_don_${data.id}.csv"
        )

        file.printWriter().use { out ->
            out.println("Trường,Gía trị")
            out.println("Mã số phiếu,${data.id}")
            out.println("Người nhập,${data.user}")
            out.println("Sản phẩm,${data.productName}")
            out.println("Số lượng,${data.quantity}")
            out.println("Tổng tiền,${data.totalPrice}")
        }
        return file
    }
}