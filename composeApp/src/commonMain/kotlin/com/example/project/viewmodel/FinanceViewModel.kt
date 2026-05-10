package com.example.project.viewmodel

import androidx.lifecycle.ViewModel
import com.example.project.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class FinanceViewModel : ViewModel() {
    private val _result = MutableStateFlow<CalculationResult?>(null)
    val result = _result.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun calculate(initialSum: String, rate: String, years: String, period: CapitalizationPeriod) {
        val p = initialSum.toDoubleOrNull()
        val r = rate.toDoubleOrNull()?.div(100)
        val t = years.toDoubleOrNull()

        if (p == null || r == null || t == null || p < 0 || r < 0 || t <= 0) {
            _error.value = "Ошибка: Введите корректные данные"
            _result.value = null
            println("Calculation error: invalid input")
            return
        }

        _error.value = null
        val n = period.timesPerYear
        val history = mutableListOf<MonthData>()
        val totalMonths = (t * 12).toInt()

        for (m in 0..totalMonths) {
            val amount = p * (1 + r / n).pow(n * (m / 12.0))
            history.add(MonthData(m, amount))
        }

        _result.value = CalculationResult(
            totalAmount = history.last().balance,
            totalProfit = history.last().balance - p,
            history = history
        )
    }
}