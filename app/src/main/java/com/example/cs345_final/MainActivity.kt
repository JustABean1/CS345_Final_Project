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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Scaffold(containerColor = Color(0xFF101820)) {
        paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C2B36)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    // Currency
                    Text(
                        text = "Currency: ${viewModel.currency}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White

                    )

                    // Passive income
                    Text(
                        text = "${viewModel.passiveUpgrade} / sec",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }



            // Button
            Button(
                onClick = {viewModel.addCurrency() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                shape = RoundedCornerShape(30.dp)

            ) {
                Text("$ ${viewModel.upgrade}")
            }

            // upgrades
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "UPGRADES",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )


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