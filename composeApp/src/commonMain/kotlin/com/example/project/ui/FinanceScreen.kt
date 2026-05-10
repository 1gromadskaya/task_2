package com.example.project.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import com.example.project.viewmodel.FinanceViewModel
import com.example.project.models.CapitalizationPeriod
import kotlin.math.roundToInt

@Composable
fun FinanceScreen(viewModel: FinanceViewModel) {
    var sum by remember { mutableStateOf("10000") }
    var rate by remember { mutableStateOf("12.0") }
    var years by remember { mutableStateOf("5") }
    var selectedPeriod by remember { mutableStateOf(CapitalizationPeriod.MONTHLY) }

    val result by viewModel.result.collectAsState()
    val error by viewModel.error.collectAsState()
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Финансовый калькулятор", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = sum,
            onValueChange = { sum = it },
            label = { Text("Начальная сумма") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Процентная ставка: ${rate}%", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = rate.toFloatOrNull() ?: 0f,
            onValueChange = { rate = ((it * 10).roundToInt() / 10.0).toString() },
            valueRange = 0f..30f,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = years,
            onValueChange = { years = it },
            label = { Text("Срок (лет)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center) {
            CapitalizationPeriod.entries.forEach { p ->
                FilterChip(
                    selected = selectedPeriod == p,
                    onClick = { selectedPeriod = p },
                    label = { Text(p.label) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Button(
            onClick = { viewModel.calculate(sum, rate, years, selectedPeriod) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Рассчитать прогноз")
        }

        error?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }

        result?.let { res ->
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val totalDisplay = (res.totalAmount * 100).roundToInt() / 100.0
                    val profitDisplay = (res.totalProfit * 100).roundToInt() / 100.0

                    Text(text = "Итог: $totalDisplay", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Прибыль: $profitDisplay", color = Color(0xFF2E7D32), style = MaterialTheme.typography.titleMedium)
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(start = 60.dp, end = 20.dp, top = 20.dp, bottom = 40.dp)
            ) {
                val maxVal = res.history.maxOf { it.balance }.toFloat()
                val minVal = res.history.minOf { it.balance }.toFloat()
                val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal
                val xStep = size.width / (res.history.size - 1)

                drawLine(color = Color.Gray, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = 2f)
                drawLine(color = Color.Gray, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 2f)

                val gridLines = 5
                for (i in 0..gridLines) {
                    val y = size.height - (i * (size.height / gridLines))
                    val value = (minVal + (i * (range / gridLines))).roundToInt()

                    drawLine(color = Color.LightGray, start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = 1f)
                    drawText(textMeasurer, value.toString(), Offset(-55.dp.toPx(), y - 10.dp.toPx()))
                }

                val path = Path()
                res.history.forEachIndexed { i, data ->
                    val x = i * xStep
                    val y = size.height - ((data.balance.toFloat() - minVal) / range * size.height)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(listOf(Color.Blue, Color.Cyan)),
                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                )
            }
        }
    }
}