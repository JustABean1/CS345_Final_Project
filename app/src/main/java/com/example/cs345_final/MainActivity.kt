package com.example.cs345_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.cs345_final.ui.theme.CS345_FinalTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ClickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface() {
                    ClickerScreen(viewModel)
                }
            }

                }
            }
        }

class ClickerViewModel : ViewModel() {

    // Private mutable state
    private var _currency = mutableStateOf(0)
    val currency: Int get() = _currency.value

    private var _upgrade = mutableStateOf(1)
    val upgrade: Int get() = _upgrade.value
    fun addCurrency() {
        _currency.value += _upgrade.value
    }
    fun buyUpgrade() {
        _upgrade.value += 1
    }
}

@Composable
fun ClickerScreen(viewModel: ClickerViewModel) {

    // Scaffold for screen structure
    Scaffold() { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Currency
            Text (
                text = "Currency: ${viewModel.currency}",
                style = MaterialTheme.typography.headlineMedium
            )

            // Button
            Button(
                onClick = {viewModel.addCurrency() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text("$ ${viewModel.upgrade}")
            }

            // upgrades
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "upgrade: ${viewModel.upgrade}",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = {viewModel.buyUpgrade()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Buy Upgrade (+1/Click")
                }
            }

        }
    }
}