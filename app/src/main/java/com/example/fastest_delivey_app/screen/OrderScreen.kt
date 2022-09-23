@file:OptIn(ExperimentalMaterialApi::class)

package com.example.fastest_delivey_app.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.*
import com.example.fastest_delivey_app.database.Order
import com.example.fastest_delivey_app.database.createOrderDao
import com.example.fastest_delivey_app.getUserLocalDate
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.example.fastest_delivey_app.ui.theme.MainGrey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderScreen(
    navController: NavController
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val orderDao = createOrderDao()

    val scope = rememberCoroutineScope()

    val order = remember { mutableStateOf(emptyList<Order>()) }

    val api = createApi()

    val sauce = remember { mutableStateOf(emptyList<MenuItem>()) }
    val foods = remember { mutableStateOf(emptyList<MenuItem>()) }

    val orderSumPrice = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit, block = {
        lifecycleScope.launchWhenStarted {
            orderDao.getOrders().collect {
                order.value = it
            }
        }

        foods.value = api.getMenuItems().filter {
            it.type == MenuType.FOOD
        }
        sauce.value = api.getMenuItems().filter {
            it.type == MenuType.SAUCE
        }

        delay(1000L)
        order.value.forEach { item ->
            orderSumPrice.value = orderSumPrice.value+ item.price
        }
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainGrey
    ) {
        Column {
            Row {
                Text(
                    text = "Order",
                    fontWeight = FontWeight.W900,
                    fontStyle = FontStyle.Italic,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                )

                Spacer(modifier = Modifier.width(40.dp))

                Text(
                    text = getUserLocalDate(SimpleDateFormat("dd MM yyyy", Locale.getDefault())),
                    fontSize = 22.sp,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    color = Grey
                )

                Spacer(modifier = Modifier.width(40.dp))

                Icon(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(35.dp),
                    tint = Grey
                )
            }

            if (order.value.isNotEmpty()){
                LazyColumn(content = {
                    items(order.value){ item ->

                        val menuCount = remember { mutableStateOf(item.count) }
                        val menuPrice = remember { mutableStateOf(menuCount.value * item.defultPrice) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = item.icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(
                                        width = 110.dp, height = 80.dp
                                    )
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.name,
                                    modifier = Modifier.padding(5.dp),
                                    fontWeight = FontWeight.W900
                                )

                                Text(
                                    text = "N" + menuPrice.value.toString(),
                                    modifier = Modifier.padding(5.dp),
                                    color = MainColor
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        backgroundColor = Grey,
                                        shape = AbsoluteRoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(30.dp),
                                        onClick = {
                                            if (menuCount.value != 0) {
                                                menuCount.value--
                                                menuPrice.value = item.defultPrice * menuCount.value
                                            }
                                        }
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(text = "-", fontSize = 25.sp, color = Color.Black)
                                        }
                                    }

                                    Text(
                                        text = menuCount.value.toString(),
                                        color = Grey
                                    )


                                    Card(
                                        backgroundColor = Grey,
                                        shape = AbsoluteRoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(30.dp),
                                        onClick = {
                                            menuCount.value++
                                            menuPrice.value = item.defultPrice * menuCount.value
                                        }
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(text = "+", fontSize = 25.sp, color = Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Дополнительно",
                            modifier = Modifier.padding(
                                start = 15.dp,
                                top = 5.dp,
                                bottom = 5.dp
                            )
                        )

                        LazyRow(content = {
                            items(foods.value){ item ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = item.images.last().url),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(
                                                width = 110.dp, height = 80.dp
                                            )
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = item.name,
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Card(
                                            shape = AbsoluteRoundedCornerShape(10.dp),
                                            backgroundColor = Grey
                                        ) {
                                            Text(
                                                text = item.price.toString(),
                                                modifier = Modifier.padding(5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        })
                    }

                    item {
                        Text(
                            text = "Соусы",
                            modifier = Modifier.padding(
                                start = 15.dp,
                                top = 5.dp,
                                bottom = 5.dp
                            )
                        )

                        LazyRow(content = {
                            items(sauce.value){ item ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = item.images.last().url),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(
                                                width = 110.dp, height = 80.dp
                                            )
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = item.name,
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Card(
                                            shape = AbsoluteRoundedCornerShape(10.dp),
                                            backgroundColor = Grey
                                        ) {
                                            Text(
                                                text = item.price.toString(),
                                                modifier = Modifier.padding(5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        })
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                            shape = AbsoluteRoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            backgroundColor = Color(0xFFEB8866)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Order Price",
                                    color = Color.White,
                                    modifier = Modifier.padding(5.dp)
                                )

                                Text(
                                    text = orderSumPrice.value.toString() + "$$",
                                    color = Color.White,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth(),
                            shape = AbsoluteRoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MainColor),
                            onClick = {
                                scope.launch {
                                    try {
                                        api.postOrder(
                                            HistoryOrder(
                                                date = getUserLocalDate(
                                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                                ),
                                                items = order.value.map {
                                                    HistoryOrderItem(
                                                        number = it.count,
                                                        menuItem = api.getMenuItem(it.id)
                                                    )
                                                }
                                            ), token = "Bearer " + token.toString()
                                        )
                                        delay(1000L)
                                        orderDao.clear()
                                    }catch (e:Exception){
                                        Log.e("ORDER",e.message.toString())
                                    }
                                }
                            }) {
                            Text(
                                text = "Order Now",
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                })
            }else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.group_12354),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
        }
    }
}