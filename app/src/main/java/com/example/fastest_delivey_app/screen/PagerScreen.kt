@file:OptIn(ExperimentalPagerApi::class)

package com.example.fastest_delivey_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PagerScreen(
    navController: NavController
) {
    val with = LocalConfiguration.current.screenWidthDp.dp

    val pagerState = rememberPagerState(pageCount = 2)

//    val sustem = rememberSystemUiController()
//
//    LaunchedEffect(key1 = Unit, block = {
//        sustem.setSystemBarsColor(
//            darkIcons = true,
//            color = MainColor
//        )
//    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainColor
    ) {
        HorizontalPager(state = pagerState) {
            when(it){
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            R.drawable.ililustration.toBitmap(height = 400).asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(vertical = 10.dp)
                        )

                        Text(
                            "Fastest Delivery \n 24/7",
                            color = Color.Black,
                            modifier = Modifier.padding(5.dp),
                            fontSize = 35.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalPagerIndicator(pagerState = pagerState, activeColor = Color.White)
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            R.drawable.illustration_1.toBitmap(height = 400).asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(vertical = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                modifier = Modifier.size(width = 157.dp, height = 68.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White
                                ),
                                shape = AbsoluteRoundedCornerShape(20.dp),
                                onClick = { navController.navigate("auth_screen") }
                            ) {
                                Text(
                                    "Sign In",
                                    fontStyle = FontStyle.Italic,
                                    color = Color.Black
                                )
                            }

                            Button(
                                modifier = Modifier.size(width = 157.dp, height = 68.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White
                                ),
                                shape = AbsoluteRoundedCornerShape(20.dp),
                                onClick = {  navController.navigate("reg_screen") }
                            ) {
                                Text(
                                    "Sign Up",
                                    fontStyle = FontStyle.Italic,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalPagerIndicator(pagerState = pagerState, activeColor = Color.White)

                        TextButton(
                            modifier = Modifier.padding(5.dp),
                            onClick = { navController.navigate("main_screen") }
                        ) {
                            Text("Skip Authorization", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}