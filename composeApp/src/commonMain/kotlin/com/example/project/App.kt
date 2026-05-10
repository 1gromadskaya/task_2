package com.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.project.ui.FinanceScreen
import com.example.project.viewmodel.FinanceViewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel = FinanceViewModel()
        FinanceScreen(viewModel)
    }
}