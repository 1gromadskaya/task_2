package com.example.project.models

import kotlinx.serialization.Serializable

@Serializable
data class MonthData(
    val month: Int,
    val balance: Double
)

@Serializable
data class CalculationResult(
    val totalAmount: Double,
    val totalProfit: Double,
    val history: List<MonthData>
)

enum class CapitalizationPeriod(val label: String, val timesPerYear: Int) {
    MONTHLY("Ежемесячно", 12),
    QUARTERLY("Ежеквартально", 4),
    ANNUALLY("Ежегодно", 1)
}