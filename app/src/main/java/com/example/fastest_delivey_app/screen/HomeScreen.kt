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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.example.fastest_delivey_app.ui.theme.MainGrey

sealed class CardState {
    class CLOS:CardState()
    class OPEN(val menuItem: MenuItem):CardState()
    class OPEN_FULL(val menuItem:MenuItem):CardState()
}

@Composable
fun HomeScreen(
    navController: NavController,
    menuItems:List<MenuItem>
) {
    val cardState = remember { mutableStateOf<CardState>(CardState.CLOS()) }

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
                                    "N" + menuItem.price.toString(),
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 25.sp,
                                    color = MainColor
                                )
                            }
                        }

                        if (cardState.value is CardState.OPEN){

                            val menuCount = remember { mutableStateOf(0) }

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
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    shape = AbsoluteRoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MainColor
                                    ),
                                    onClick = {

                                    }
                                ) {
                                    Text(
                                        "Continue Shop",
                                        color = Color.White
                                    )
                                }

                                Button(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    shape = AbsoluteRoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MainColor
                                    ),
                                    onClick = {

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