package com.example.project

import androidx.compose.ui.test.*
import com.example.project.ui.FinanceScreen
import com.example.project.viewmodel.FinanceViewModel
import kotlin.test.Ignore
import kotlin.test.Test

class FinanceUITest {

    @OptIn(ExperimentalTestApi::class)
    @Ignore
    @Test
    fun testElementsPresence() = runComposeUiTest {
        setContent {
            FinanceScreen(FinanceViewModel())
        }
        onNodeWithText("Финансовый калькулятор").assertIsDisplayed()
        onNodeWithText("Начальная сумма").assertIsDisplayed()
        onNodeWithText("Рассчитать прогноз").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Ignore
    @Test
    fun testErrorDisplay() = runComposeUiTest {
        setContent {
            FinanceScreen(FinanceViewModel())
        }

        onNodeWithText("10000").performTextReplacement("")
        onNodeWithText("Рассчитать прогноз").performClick()
        onNodeWithText("Ошибка: Введите корректные данные").assertIsDisplayed()
    }
}