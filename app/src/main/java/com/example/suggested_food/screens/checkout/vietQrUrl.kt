package com.example.suggested_food.screens.checkout

fun vietQrUrl(
    bankId: String = "VCB",
    accountNo: String = "0123456789",
    accountName: String = "NGUYEN VAN A",
    amount: Double,
    orderId: String
): String {
    return "https://img.vietqr.io/image/$bankId-$accountNo-compact.png" +
            "?amount=${amount.toInt()}" +
            "&addInfo=$orderId" +
            "&accountName=$accountName"
}
