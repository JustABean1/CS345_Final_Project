package com.example.cs345_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.lifecycle.viewModelScope
import com.example.cs345_final.ui.theme.CS345_FinalTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    // Currency Amount
    private var _currency = mutableStateOf(0)
    val currency: Int get() = _currency.value

    // Click upgrade
    private var _upgrade = mutableStateOf(1)
    val upgrade: Int get() = _upgrade.value

    // Passive Income upgrade
    private var _passiveupgrade = mutableStateOf(0)
    val passiveUpgrade: Int get() = _passiveupgrade.value

    init {
        startPassiveIncome()
    }

    fun addCurrency() {
        _currency.value += _upgrade.value
    }
    fun buyUpgrade() {
        _upgrade.value += 1
    }
    fun butPassiveUpgrade() {
        _passiveupgrade.value += 1
    }

    private fun startPassiveIncome() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _currency.value += _passiveupgrade.value
            }
        }
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

            Text(
                text = "${viewModel.passiveUpgrade} / sec",
                style = MaterialTheme.typography.titleLarge
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


                Button(
                    onClick = {viewModel.buyUpgrade()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Buy Upgrade Click (+${viewModel.upgrade}/Click)")

                }

                Button(
                    onClick = {viewModel.butPassiveUpgrade()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Buy Upgrade Passive (+${viewModel.passiveUpgrade}/Sec)")
                }
            }

        }
    }
}