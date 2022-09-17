package com.example.fastest_delivey_app.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.Menu
import com.example.fastest_delivey_app.api.createApi
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@Composable
fun Int.toBitmap(
    with:Int = LocalConfiguration.current.screenWidthDp,
    height:Int = LocalConfiguration.current.screenHeightDp
):Bitmap {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources,this)
    return Bitmap.createScaledBitmap(bitmap,with,height,false)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StartScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val api = createApi()

    var menu = remember { mutableStateOf<Menu?>(null) }

//    val sustem = rememberSystemUiController()

    LaunchedEffect(key1 = Unit, block = {
//
//        sustem.setSystemBarsColor(
//            darkIcons = true,
//            color = Color.White
//        )

        menu.value = api.getMenu()

        menu.value?.let {
            navController.navigate("pager_screen")
        }
    })

    Image(
        bitmap = R.drawable.start_beck.toBitmap().asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(250.dp),
            shape = AbsoluteRoundedCornerShape(180.dp),
            backgroundColor = Color(0x6FFFFFFF)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cooking_1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp)
                )

                Text(
                    "WSR Food",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(10.dp)
                )

                CircularProgressIndicator(
                    color = MainColor,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}