@file:OptIn(ExperimentalMaterialApi::class)

package com.example.fastest_delivey_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.MenuItem
import com.example.fastest_delivey_app.database.Order
import com.example.fastest_delivey_app.database.createOrderDao
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.example.fastest_delivey_app.ui.theme.MainGrey
import kotlinx.coroutines.launch

sealed class CardState {
    class CLOS:CardState()
    class OPEN(val menuItem: MenuItem):CardState()
    class OPEN_FULL(val menuItem:MenuItem):CardState()
}

@Composable
fun HomeScreen(
    navController: NavController,
    menuItems:List<MenuItem>,
    mainBottomNavigation: MutableState<MainBottomNavigation>
) {
    val cardState = remember { mutableStateOf<CardState>(CardState.CLOS()) }

    val scope = rememberCoroutineScope()
    val orderDao = createOrderDao()

    if (cardState.value is CardState.CLOS){
        LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
            items(menuItems){ menu ->
                Card(
                    modifier = Modifier.padding(15.dp),
                    shape = AbsoluteRoundedCornerShape(25.dp),
                    backgroundColor = Color.White,
                    onClick = {
                        cardState.value = CardState.OPEN(menu)
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = menu.images.last().url),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(100.dp)
                        )

                        Text(
                            text = menu.name,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "N" + menu.price.toString(),
                            fontSize = 22.sp,
                            modifier = Modifier.padding(5.dp),
                            color = MainColor
                        )
                    }
                }
            }
        })
    }else {

        val menuItem = if (cardState.value is CardState.OPEN) {
            (cardState.value as CardState.OPEN).menuItem
        } else {
            (cardState.value as CardState.OPEN_FULL).menuItem
        }


        val menuCount = remember { mutableStateOf(1) }
        val menuPrice = remember { mutableStateOf(menuCount.value * menuItem.price) }

        LazyColumn(content = {
            item {
                Card(
                    modifier = Modifier.padding(15.dp),
                    shape = AbsoluteRoundedCornerShape(25.dp),
                    backgroundColor = Color.White
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(modifier = Modifier.padding(5.dp),onClick = {
                                if (cardState.value is CardState.OPEN_FULL){
                                    cardState.value = CardState.OPEN(menuItem)
                                }else if (cardState.value is CardState.OPEN){
                                    cardState.value = CardState.CLOS()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }

                            TextButton(
                                modifier = Modifier.padding(5.dp),
                                onClick = {
                                    navController.navigate("menu_item_screen/${menuItem.id}")
                                },
                            ) {
                                Text(text = "More", color = Color.Black)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = menuItem.images.last().url),
                                contentDescription = null,
                                modifier = Modifier.size(width = 216.dp, height = 143.dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    menuItem.name,
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 25.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(40.dp))

                                Text(
                                    "N" + menuPrice.value.toString(),
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 25.sp,
                                    color = MainColor
                                )
                            }
                        }
                        if (cardState.value is CardState.OPEN){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                                            if (menuCount.value != 0){
                                                menuCount.value--
                                                menuPrice.value = menuItem.price * menuCount.value
                                            }
                                        }
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(text = "-",fontSize = 25.sp,color = Color.Black)
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
                                            menuPrice.value = menuItem.price * menuCount.value
                                        }
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(text = "+",fontSize = 25.sp, color = Color.Black)
                                        }
                                    }
                                }

                                Button(
                                    modifier = Modifier.padding(10.dp),
                                    shape = AbsoluteRoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MainColor
                                    ),
                                    onClick = {
                                        cardState.value = CardState.OPEN_FULL(menuItem)
                                        scope.launch {
                                            orderDao.insertOrder(
                                                Order(
                                                    id = 0,
                                                    name = menuItem.name,
                                                    icon = menuItem.images.last().url,
                                                    price = menuPrice.value,
                                                    count = menuCount.value,
                                                    defultPrice = menuItem.price
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Add to cart",
                                            color = Color.White
                                        )

                                        Icon(
                                            painter = painterResource(id = R.drawable.shopping_cart),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                }
                            }
                        } else if (cardState.value is CardState.OPEN_FULL){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = AbsoluteRoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MainColor
                                    ),
                                    onClick = {
                                        cardState.value = CardState.CLOS()
                                    }
                                ) {
                                    Text(
                                        "Continue Shop",
                                        color = Color.White
                                    )
                                }

                                Button(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = AbsoluteRoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MainColor
                                    ),
                                    onClick = {
                                        mainBottomNavigation.value = MainBottomNavigation.ORDER
                                    }
                                ) {
                                    Text(
                                        "Go to cart",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}