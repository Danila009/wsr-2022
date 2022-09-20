package com.example.fastest_delivey_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fastest_delivey_app.api.MenuItem
import com.example.fastest_delivey_app.ui.theme.MainColor

@Composable
fun HomeScreen(
    menuItems:List<MenuItem>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(menuItems){ menu ->
            Card(
                modifier = Modifier.padding(15.dp),
                shape = AbsoluteRoundedCornerShape(25.dp),
                backgroundColor = Color.White
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
}