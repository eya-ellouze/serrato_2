package com.serrato

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.serrato.dto.GreenHouseDto
import com.serrato.model.GreenHouseViewModel
import com.serrato.service.ApiServices
import com.serrato.service.GreenHouseService
import com.serrato.ui.theme.PurpleGrey80
import com.serrato.ui.theme.SerratoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class GreenHouseListActivity : ComponentActivity() {

    companion object {
        const val GREENHOUSE_PARAM = "com.serrato.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GreenHouseViewModel by viewModels()
        val navigateBack: () -> Unit = {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch(context = Dispatchers.IO) {
            viewModel.findAll()

            if (viewModel.greenhousesState.value.greenhouses.isNullOrEmpty()) {
                runCatching {
                    ApiServices.greenhousesApiService.findAll().execute()
                }
                    .onSuccess {
                        val greenhouses = it.body() ?: emptyList()
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) {
                            it.printStackTrace()
                            Toast.makeText(
                                applicationContext,
                                "Error on greenhouses loading $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }

            withContext(context = Dispatchers.Main) {
                setContent {
                    SerratoTheme {
                        Scaffold(
                            topBar = { SerratoTopAppBar("GreenHouses", navigateBack, activity = this@GreenHouseListActivity) }
                        ) { innerPadding ->
                            val greenhousesState = viewModel.greenhousesState.collectAsState().value

                            if (greenhousesState.error != null) {
                                Toast.makeText(
                                    applicationContext,
                                    "Error on greenhouses loading ${greenhousesState.error}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(innerPadding),
                                ) {
                                    val greenhouses = greenhousesState.greenhouses ?: emptyList()
                                    items(greenhouses, key = { it.id }) { greenhouse ->
                                        GreenHouseItem(
                                            greenhouse = greenhouse,
                                            modifier = Modifier.clickable {
                                                openGreenHouse(this@GreenHouseListActivity, greenhouse.id)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun openGreenHouse(context: Context, id: Long) {
    val intent = Intent(context, GreenHouseDetailActivity::class.java).apply {
        putExtra(GreenHouseListActivity.GREENHOUSE_PARAM, id)
    }
    context.startActivity(intent)
}

@Composable
fun GreenHouseListContent(innerPadding: PaddingValues, greenhouses: List<GreenHouseDto>, openGreenHouse: (Long) -> Unit) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(innerPadding),
    ) {
        items(greenhouses, key = { it.id }) { greenhouse ->
            GreenHouseItem(
                greenhouse = greenhouse,
                modifier = Modifier.clickable { openGreenHouse(greenhouse.id) }
            )
        }
    }
}

@Composable
fun GreenHouseItem(greenhouse: GreenHouseDto, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PurpleGrey80)
    ) {
        Row(
            modifier = modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = greenhouse.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target temperature: " + (greenhouse.targetTemperature?.toString() ?: "?") + "°",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Target humidity: " + (greenhouse.targetHumidity?.toString() ?: "?") + "%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = (greenhouse.currentTemperature?.toString() ?: "?") + "°",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Right
                )
                Text(
                    text = (greenhouse.currentHumidity?.toString() ?: "?") + "%",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreenHouseItemPreview() {
    SerratoTheme {
        GreenHouseItem(GreenHouseService.GREENHOUSES[0])
    }
}

