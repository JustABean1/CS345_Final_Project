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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kotlin.math.*
import kotlin.time.toDuration

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

    private var _valueUpgrade = mutableStateOf(1)
    private var _valueLevel = mutableStateOf(1)
    private val _valueCostBase = mutableStateOf(5)
    private val _valueMultiplier = mutableStateOf(3)
    val valueUpgrade: Int get() = _valueUpgrade.value
    val valueLevel: Int get() = _valueLevel.value
    //cost of upgrading value is base*mult^number of upgrades
    val valueCost: Int get() = calcCost(_valueCostBase.value, _valueMultiplier.value, _valueLevel.value)

    // Passive Income upgrade
    private var _passiveupgrade = mutableStateOf(0)
    private var _passiveLevel = mutableStateOf(0)
    private val _passiveCostBase = mutableStateOf(6)
    private val _passiveMultiplier = mutableStateOf(4)
    val passiveUpgrade: Int get() = _passiveupgrade.value
    //cost of upgrading passive is base*mult^number of upgrades
    val passiveCost: Int get() = calcCost(_passiveCostBase.value, _passiveMultiplier.value, _passiveLevel.value)

    //Autoclicker vars
    private var _autoClickerLevel = mutableStateOf(0)
    private val _autoCostBase = mutableStateOf(9)
    private val _autoMultiplier = mutableStateOf(9)
    val autoClickerLevel: Int get() = _autoClickerLevel.value
    //cost of upgrading auto is base*mult^number of upgrades
    val autoCost: Int get() = calcCost(_autoCostBase.value, _autoMultiplier.value, _autoClickerLevel.value)



    init {
        startPassiveIncome()
    }

    fun addCurrency() {
        _currency.value += _valueUpgrade.value
    }

    fun calcCost(base: Int, mult: Int, level: Int): Int = (base * mult.toDouble().pow(level)).toInt()
    fun buyValueUpgrade() {
        //if enough money, subtract the cost and then upgrade
        if (_currency.value >= valueCost) {
            _currency.value -= valueCost
            _valueUpgrade.value += _valueUpgrade.value/10 + 1 //don't love leaving a magic number here but oh well
            _valueLevel.value++
        }

    }
    fun buyPassiveUpgrade() {
        //if enough money, subtract the cost and then upgrade
        if (_currency.value >= passiveCost) {
            _currency.value -= passiveCost
            _passiveupgrade.value += _passiveupgrade.value/10 + 1 //don't love leaving a magic number here but oh well
            _passiveLevel.value++
        }

    }

    fun buyAutoUpgrade() {
        //if enough money, subtract the cost and then upgrade
        if (_currency.value >= autoCost) {
            _currency.value -= autoCost
            _autoClickerLevel.value++
        }
        //not starting a while(true) loop until a delay is valid
        if (autoClickerLevel == 1) startAuto()

    }

    private fun startPassiveIncome() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _currency.value += _passiveupgrade.value
            }
        }
    }

    private fun startAuto() {
        viewModelScope.launch {
            while (true) {
                delay((5000/autoClickerLevel).toLong())
                addCurrency()
            }
        }
    }

    fun calcIncome(): Int {
        if (autoClickerLevel > 0) {
            return ((5000 / autoClickerLevel) / 5 * _valueUpgrade.value) + (passiveUpgrade)
        }
        return passiveUpgrade
    }

}

@Composable
fun ClickerScreen(viewModel: ClickerViewModel) {

    Scaffold(
        containerColor = Color(0xFF101820)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Currency display
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

                    Text(
                        text = "Currency: ${viewModel.currency}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    Text(
                        text = "${viewModel.calcIncome()} / sec",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Green
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // Main click button
            Button(
                onClick = { viewModel.addCurrency() },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),

                shape = RoundedCornerShape(20.dp)
            ) {

                Text(
                    "$ ${viewModel.valueUpgrade}"
                )
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // Upgrade section title
            Text(
                text = "Upgrades",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // Scrollable upgrade list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    ),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Click upgrade
                Button(
                    onClick = { viewModel.buyValueUpgrade() },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Click Upgrade")

                        Text(
                            "+${viewModel.valueUpgrade} per click"
                        )

                        Text(
                            "Cost: ${viewModel.valueCost}"
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(15.dp)
                )


                // Passive income upgrade
                Button(
                    onClick = { viewModel.buyPassiveUpgrade() },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Passive Upgrade")

                        Text(
                            "+${viewModel.passiveUpgrade} per second"
                        )

                        Text(
                            "Cost: ${viewModel.passiveCost}"
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(15.dp)
                )


                // Auto clicker upgrade
                Button(
                    onClick = { viewModel.buyAutoUpgrade() },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Auto Clicker")

                        Text(
                            "Level: ${viewModel.autoClickerLevel}"
                        )

                        Text(
                            "Cost: ${viewModel.autoCost}"
                        )
                    }
                }
            }
        }
    }
}