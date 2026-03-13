package com.example.suggested_food.screens.import_data

import com.google.firebase.firestore.FirebaseFirestore

fun insertSampleData(firestore: FirebaseFirestore) {
    val categoryNames = listOf(
        "Cảm cúm", "Vitamin", "Đau nhức", "Tiêu hóa",
        "Da liễu", "Hô hấp", "Tim mạch", "Tiểu đường",
        "Thận – Gan", "Sức khỏe nam nữ", "Mắt – Tai mũi họng", "Trẻ em"
    )

    val categoryImages = listOf(
        "https://link1.jpg", "https://link2.jpg", "https://link3.jpg", "https://link4.jpg",
        "https://link5.jpg", "https://link6.jpg", "https://link7.jpg", "https://link8.jpg",
        "https://link9.jpg", "https://link10.jpg", "https://link11.jpg", "https://link12.jpg"
    )

    val categoryIdMap = mutableMapOf<String, String>() // map name -> id Firestore

    categoryNames.forEachIndexed { index, name ->
        val cat = mapOf("name" to name, "imageUrl" to categoryImages[index])
        firestore.collection("categories")
            .add(cat)
            .addOnSuccessListener { docRef ->
                categoryIdMap[name] = docRef.id
                println("Category inserted: ${name} -> ${docRef.id}")

                // Khi tất cả categories đã insert xong thì insert products
                if (categoryIdMap.size == categoryNames.size) {
                    insertProducts(firestore, categoryIdMap)
                }
            }
            .addOnFailureListener { e ->
                println("Error inserting category: ${e.message}")
            }
    }
}

// 2. Insert 25-30 Products
fun insertProducts(firestore: FirebaseFirestore, categoryIdMap: Map<String, String>) {
    val productList = mutableListOf<Map<String, Any>>()

    // Tạo 2-3 sản phẩm cho mỗi category → tổng ~25-30 sản phẩm
    categoryIdMap.forEach { (categoryName, categoryId) ->
        for (i in 1..3) {
            val prod = mapOf(
                "categoryId" to categoryId,
                "name" to "$categoryName Thuốc $i",
                "images" to listOf(
                    "https://link-image1.jpg",
                    "https://link-image2.jpg"
                ),
                "description" to "Mô tả chi tiết về $categoryName Thuốc $i",
                "price" to (10000 + i * 5000),
                "onSale" to (i % 2 == 0),
                "stock" to (50 + i * 10),
                "manufacturer" to "Nhà sản xuất ABC",
                "ingredients" to "Thành phần chính",
                "usage" to "Uống 1 viên/lần, 3 lần/ngày",
                "expiryDate" to "2025-12-31",
                "rating" to 4.0 + (i % 2) * 0.5f,
                "reviews" to listOf("Hiệu quả", "An toàn")
            )
            productList.add(prod)
        }
    }

    productList.forEach { prod ->
        firestore.collection("products")
            .add(prod)
            .addOnSuccessListener { docRef ->
                println("Product inserted: ${prod["name"]} -> ${docRef.id}")
            }
            .addOnFailureListener { e ->
                println("Error inserting product: ${e.message}")
            }
    }
}



