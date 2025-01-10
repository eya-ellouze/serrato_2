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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serrato.R.string.act_greenhouse_save
import com.serrato.model.GreenHouseViewModel
import com.serrato.service.GreenHouseService
import com.serrato.ui.theme.SerratoTheme
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
class GreenHouseDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val greenhouseId = intent.getLongExtra(GreenHouseListActivity.GREENHOUSE_PARAM, -1L)
        val viewModel: GreenHouseViewModel by viewModels()

        setContent {
            SerratoTheme {
                Scaffold(
                    topBar = {
                        SerratoTopAppBar(
                            title = "GreenHouse Details",
                            returnAction = {
                                startActivity(Intent(baseContext, GreenHouseListActivity::class.java))
                            },
                            activity = this
                        )
                    },
                    floatingActionButton = {
                        GreenHouseUpdateButton(onClick = {
                            viewModel.greenhouse?.let {
                                GreenHouseService.updateGreenHouse(it.id, it)
                                Toast.makeText(baseContext, "GreenHouse ${it.name} was updated", Toast.LENGTH_LONG).show()
                                startActivity(Intent(baseContext, GreenHouseListActivity::class.java))
                            }
                        })
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LaunchedEffect(greenhouseId) {
                        if (greenhouseId != -1L) {
                            viewModel.greenhouse = GreenHouseService.findById(greenhouseId)
                        }
                    }

                    if (viewModel.greenhouse != null) {
                        GreenHouseDetail(viewModel, Modifier.padding(innerPadding))
                    } else {
                        NoGreenHouse(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }


    @Composable
    fun GreenHouseUpdateButton(onClick: () -> Unit) {
        ExtendedFloatingActionButton(
            onClick = { onClick() },
            icon = {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = stringResource(act_greenhouse_save),
                )
            },
            text = { Text(text = stringResource(act_greenhouse_save)) }
        )
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
    fun GreenHouseDetail(viewModel: GreenHouseViewModel, modifier: Modifier = Modifier) {
        val greenhouse = viewModel.greenhouse!!

        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.act_greenhouse_name),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = greenhouse.name ?: "",
                onValueChange = { viewModel.greenhouse = greenhouse.copy(name = it) },
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
                onValueChange = { viewModel.greenhouse = greenhouse.copy(targetTemperature = it.toDouble()) },
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
    }
}
