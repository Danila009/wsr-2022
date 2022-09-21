package com.example.fastest_delivey_app.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.MenuItem
import com.example.fastest_delivey_app.api.MenuType
import com.example.fastest_delivey_app.api.createApi
import com.example.fastest_delivey_app.ui.theme.Grey
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.example.fastest_delivey_app.ui.theme.MainGrey
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter

enum class MainBottomNavigation(val icon:Int) {
    HOME(R.drawable.main_icon),
    ORDER(R.drawable.order_icon),
    PROFILE(R.drawable.user_icon),
    HISTORY(R.drawable.history_icon)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController
) {
    val api = createApi()

    val menuItemApi = remember { mutableStateOf(emptyList<MenuItem>()) }
    val menuItems = remember { mutableStateOf(emptyList<MenuItem>()) }

    val searchState = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    val menuType = remember { mutableStateOf(MenuType.FOOD) }

    val mainBottomNavigation = remember { mutableStateOf(MainBottomNavigation.HOME) }


    LaunchedEffect(key1 = Unit, block = {
        menuItemApi.value = api.getMenuItems()
    })

    LaunchedEffect(
        searchText.value,
        menuType.value,
        searchState.value,
        menuItemApi.value
    ){
        if (menuItemApi.value.isNotEmpty()){
            if (searchText.value.isNotEmpty() && searchState.value){
                menuItems.value = menuItemApi.value.filter {
                    it.name.contains(searchText.value)
                }
            }else {
                menuItems.value = menuItemApi.value.filter {
                    it.type == menuType.value
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MainGrey
            ) {
                Column {
                    if (mainBottomNavigation.value == MainBottomNavigation.HOME){
                        if (!searchState.value){

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.padding(10.dp))

                                Text(
                                    text = "Выберите адрес доставки",
                                    modifier = Modifier.padding(10.dp),
                                    color = Grey
                                )

                                Spacer(modifier = Modifier.padding(10.dp))

                                Icon(
                                    painter = painterResource(id = R.drawable.group),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .size(35.dp),
                                    tint = Grey
                                )

                                IconButton(modifier = Modifier.padding(5.dp),onClick = { searchState.value = !searchState.value }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.padding(5.dp),
                                        tint = Grey
                                    )
                                }

                                Spacer(modifier = Modifier.padding(20.dp))
                            }

                            AndroidView(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .height(100.dp),
                                factory = {
                                    AdView(it).apply {
//                                        adSize = AdSize.BANNER
                                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                        loadAd(AdRequest.Builder().build())
                                    }
                                }
                            )

                        }else{
                            TextField(
                                value = searchText.value,
                                onValueChange = { searchText.value = it },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.White,
                                    cursorColor = Color.Black
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                                    .clip(AbsoluteRoundedCornerShape(15.dp)),
                                placeholder = { Text("Search",color = Grey) },
                                trailingIcon = {
                                    Row {
                                        IconButton(
                                            modifier = Modifier.padding(5.dp),
                                            onClick = { searchState.value = false }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                        }

                                        IconButton(
                                            modifier = Modifier.padding(5.dp),
                                            onClick = {  }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        if (searchText.value.isEmpty()){
                            TabRow(
                                modifier = Modifier.padding(5.dp),
                                selectedTabIndex = menuType.value.ordinal,
                                backgroundColor = MainGrey,
                                contentColor = MainColor
                            ) {
                                MenuType.values().forEach { type ->
                                    Tab(
                                        selected = menuType.value == type,
                                        onClick = { menuType.value = type },
                                        text = { Text(text = type.title) },
                                        selectedContentColor = if (type == menuType.value) MainColor else Grey
                                    )
                                }
                            }
                        }else {
                            Text(
                                text = "Results",
                                modifier = Modifier.padding(5.dp),
                                fontStyle = FontStyle.Italic,
                                fontSize = 40.sp
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = MainGrey
            ) {
                MainBottomNavigation.values().forEach { item ->
                    BottomNavigationItem(
                        selected = item == mainBottomNavigation.value,
                        onClick = { mainBottomNavigation.value = item },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                tint = if (item == mainBottomNavigation.value) MainColor else Grey,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MainGrey
        ) {
            when(mainBottomNavigation.value){
                MainBottomNavigation.HOME -> HomeScreen(
                    menuItems = menuItems.value,
                    navController = navController
                )
                MainBottomNavigation.ORDER -> {}
                MainBottomNavigation.PROFILE -> {}
                MainBottomNavigation.HISTORY -> {}
            }
        }
    }
}