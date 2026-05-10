package com.example.project

import com.example.project.viewmodel.FinanceViewModel
import com.example.project.models.CapitalizationPeriod
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class FinanceIntegrationTest {
    @Test
    fun testFullFlow() {
        val viewModel = FinanceViewModel()
        viewModel.calculate("5000", "10", "2", CapitalizationPeriod.QUARTERLY)
        val result = viewModel.result.value
        assertNotNull(result)
        assertTrue(result.history.size > 20)
        assertTrue(result.totalAmount > 5000)
        assertTrue(result.totalProfit > 0)
    }
}