@file:OptIn(ExperimentalMaterialApi::class)

package com.example.fastest_delivey_app.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.AuthData
import com.example.fastest_delivey_app.api.HistoryOrder
import com.example.fastest_delivey_app.api.createApi
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainGrey

@Composable
fun HistoryScreen(
    navController: NavController
) {
    val api = createApi()

    val orders = remember { mutableStateOf(emptyList<HistoryOrder>()) }

    LaunchedEffect(key1 = Unit, block = {
        try {
            Log.e("UserData", userEmail.toString())
            Log.e("UserData", userPassword.toString())
            orders.value = api.getOrders(
                token = "Bearer " + api.auth(
                    AuthData(userEmail.toString(), userPassword.toString())
                ).access_token
            )
        }catch (e:Exception){}
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainGrey
    ) {
        Column {
            Text(
                text = "History",
                fontWeight = FontWeight.W900,
                fontStyle = FontStyle.Italic,
                fontSize = 35.sp,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp)
            )

            if (orders.value.isNotEmpty()){
                LazyColumn(content = {

                    items(orders.value){ item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().height(70.dp).padding(15.dp),
                            backgroundColor = Grey,
                            shape = AbsoluteRoundedCornerShape(15.dp),
                            onClick = {

                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = item.date,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Cost",
                                        modifier = Modifier.padding(5.dp)
                                    )

                                    Text(
                                        text = item.price.toString(),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }
                    }
                })
            }else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.group_222354),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
        }
    }
}