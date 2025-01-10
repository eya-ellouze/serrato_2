package com.serrato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serrato.dto.GreenHouseDto
import com.serrato.model.GreenHouseViewModel
import com.serrato.service.GreenHouseService
import com.serrato.ui.theme.SerratoTheme
import kotlin.math.round

class GreenHouseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val param = intent.getStringExtra(MainActivity.GREENHOUSE_PARAM)
        val viewModel: GreenHouseViewModel by viewModels()
        viewModel.greenhouse = GreenHouseService.findByNameOrId(param)
        val greenhouse = GreenHouseService.findByNameOrId(param)

        val onGreenHouseSave: () -> Unit = {
            if(viewModel.greenhouse != null) {
                val greenhouseDto: GreenHouseDto = viewModel.greenhouse as GreenHouseDto
                GreenHouseService.updateGreenHouse(greenhouseDto.id, greenhouseDto)
                Toast.makeText(baseContext, "GreenHouse ${greenhouseDto.name} was updated", Toast.LENGTH_LONG).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
            }
        }
        val navigateBack: () -> Unit = {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        setContent {
            SerratoTheme {
                Scaffold(
                    topBar = { SerratoTopAppBar("GreenHouse", navigateBack, this) },
                    floatingActionButton = { GreenHouseUpdateButton(onGreenHouseSave) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.greenhouse != null) {
                        GreenHouseDetail(viewModel!! , Modifier.padding(innerPadding))
                    } else {
                        NoGreenHouse(Modifier.padding(innerPadding))
                    }

                }
            }
        }
    }
    @Composable
    fun GreenHouseDetail(greenhouseViewModel: GreenHouseViewModel, modifier: Modifier = Modifier) {
        var greenhouse = greenhouseViewModel.greenhouse!!

        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.act_greenhouse_name),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = greenhouse.name ?: "",
                onValueChange = { greenhouseViewModel.greenhouse = greenhouse.copy(name=it)},
                label = { Text(text = stringResource(R.string.act_greenhouse_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Current Temperature\n",
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = "${greenhouse.currentTemperature}°C",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Target Temperature ",
                style = MaterialTheme.typography.labelSmall
            )

            Slider(
                value = greenhouse.targetTemperature?.toFloat() ?: 18.0f,
                onValueChange = { greenhouse = greenhouse.copy(targetTemperature = it.toDouble()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 0,
                valueRange = 10f..28f
            )
            Text(text = (round((greenhouse.targetTemperature ?: 18.0) * 10) / 10).toString())

        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Current Humidity\n",
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = "${greenhouse.currentHumidity}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Target Humidity ",
            style = MaterialTheme.typography.labelSmall
        )

        Slider(
            value = greenhouse.targetHumidity?.toFloat() ?: 50.0f,
            onValueChange = { greenhouse = greenhouse.copy(targetHumidity = it.toDouble()) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 0,
            valueRange = 0f..100f
        )
        Text(text = (round((greenhouse.targetHumidity ?: 18.0) * 10) / 10).toString())

    }
    }


    @Preview(showBackground = true)
    @Composable
    fun NoGreenHouse(modifier: Modifier = Modifier) {
        Text(
            text = stringResource(R.string.act_greenhouse_none),
            modifier = modifier,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    @Composable
    fun GreenHouseUpdateButton(onClick: () -> Unit) {
        ExtendedFloatingActionButton(
            onClick = { onClick() },
            icon = {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = stringResource(R.string.act_greenhouse_save),
                )
            },
            text = { Text(text = stringResource(R.string.act_greenhouse_save)) }
        )
    }
}