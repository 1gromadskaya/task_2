package com.example.project

import com.example.project.viewmodel.FinanceViewModel
import com.example.project.models.CapitalizationPeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FinanceLogicTest {
    private val viewModel = FinanceViewModel()

    @Test
    fun testAnnualCalculation() {
        viewModel.calculate("1000", "10", "1", CapitalizationPeriod.ANNUALLY)
        val result = viewModel.result.value
        assertNotNull(result)
        assertEquals(1100.0, (result.totalAmount * 100).toInt() / 100.0, 0.1)
    }

    @Test
    fun testInvalidInput() {
        viewModel.calculate("error", "-5", "0", CapitalizationPeriod.MONTHLY)
        assertNull(viewModel.result.value)
        assertNotNull(viewModel.error.value)
    }

    @Test
    fun testMonthlyCompound() {
        viewModel.calculate("1000", "12", "1", CapitalizationPeriod.MONTHLY)
        val result = viewModel.result.value
        assertNotNull(result)
        assertEquals(1126.82, (result.totalAmount * 100).toInt() / 100.0, 0.1)
    }
}