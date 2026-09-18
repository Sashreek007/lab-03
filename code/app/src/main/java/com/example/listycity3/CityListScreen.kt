package com.example.listycity3

import android.R.attr.label
import android.R.attr.name
import android.R.attr.onClick
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme


@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City)->Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    //Asked claude chat if I could make it recompose on click of if there is a better way to do it.
    LaunchedEffect(selectedCity) {
        val city = selectedCity
        if (city != null) {
            newCityName = city.name
            newProvinceName = city.province
            showAddCityFields = true
        } else {
            newCityName = ""
            newProvinceName = ""
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            FloatingActionButton(modifier = Modifier.padding(16.dp), onClick = {
                showAddCityFields = !showAddCityFields
            }) { Text("+")}
        }
        if (showAddCityFields) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)

                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)

                )
                Spacer(modifier = Modifier.width(8.dp))

                Button(modifier = Modifier.padding(vertical = 12.dp),onClick = {
                    if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                        val current = selectedCity
                        val entered = City(name = newCityName, province = newProvinceName)

                        if (current == null) {
                            onAddCity(entered)
                        } else {
                            onUpdateCity(current, entered)
                        }

                        newCityName = ""
                        newProvinceName = ""
                        showAddCityFields = false
                        selectedCity = null
                    }
                }) { if (selectedCity == null ){ Text("Add City") }
                    else {Text("Update City")}
            }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        if (selectedCity == city) {
                            selectedCity = null
                        } else {
                            selectedCity = city
                        }
                    }
                )

                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(city: City, isSelected: Boolean, onClick: ()-> Unit) {
    //asked claude chat for help on changing the background color
    val bg = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .background(bg)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ), onAddCity = {},
            onUpdateCity = {_,_ ->}
        )
    }
}