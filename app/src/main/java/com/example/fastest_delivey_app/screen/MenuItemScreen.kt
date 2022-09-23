@file:OptIn(ExperimentalPagerApi::class)

package com.example.fastest_delivey_app.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fastest_delivey_app.api.MenuItem
import com.example.fastest_delivey_app.api.createApi
import com.example.fastest_delivey_app.database.Order
import com.example.fastest_delivey_app.database.createOrderDao
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.example.fastest_delivey_app.ui.theme.MainGrey
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MenuItemScreen(
    navController: NavController,
    menuItemId:Int
) {
    val api = createApi()

    val orderDao = createOrderDao()

    val menuItem = remember { mutableStateOf(MenuItem()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, block = {
        menuItem.value = api.getMenuItem(menuItemId)
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainGrey
    ) {
        if(menuItem.value.id != 0 ){
            val item = menuItem.value

            val pagerState = rememberPagerState(pageCount = item.images.lastIndex)

            Column {
                IconButton(modifier = Modifier.padding(10.dp),onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HorizontalPager(state = pagerState) {
                        val image = item.images[it]

                        Image(
                            painter = rememberAsyncImagePainter(model = image.url),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(5.dp)
                        )
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        activeColor = MainColor,
                        modifier = Modifier.padding(10.dp)
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier.padding(5.dp),
                        color = Color.Black,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "N" + item.price.toString(),
                        modifier = Modifier.padding(5.dp),
                        color = MainColor,
                        fontSize = 15.sp
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = "Delivery info",
                        modifier = Modifier.padding(5.dp),
                        color = Color.Black
                    )

                    Text(
                        text = item.description,
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.W200,
                        color = Grey
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.padding(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MainColor
                            ),
                            onClick = {
                                scope.launch {
                                    orderDao.insertOrder(
                                        Order(
                                            id = 0,
                                            name = item.name,
                                            price = item.price,
                                            icon = item.images.last().url,
                                            count = 1,
                                            defultPrice = item.price
                                        )
                                    )
                                }
                            },
                        ) {
                            Text(text = "Add to cart",color = Color.White)
                        }
                    }
                }
            }
        }
    }
}