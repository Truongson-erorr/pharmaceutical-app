package com.example.suggested_food.models

data class PromoCode(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val description: String? = null,
    val discountType: String = "PERCENT",
    val discountValue: Double = 0.0,
    val maxDiscountAmount: Double? = null,
    val minOrderValue: Double = 0.0,
    val applyScope: String = "ALL",
    val productIds: List<String> = emptyList(),
    val startDate: Long = 0,
    val endDate: Long = 0,
    val usageLimit: Int = 0,
    val usedCount: Int = 0,
    val perUserLimit: Int = 1,
    val isActive: Boolean = true,
    val status: String = "ACTIVE",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdBy: String? = null,
    val internalNote: String? = null
)