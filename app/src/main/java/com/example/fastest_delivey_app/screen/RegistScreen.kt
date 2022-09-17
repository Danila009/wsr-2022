package com.example.fastest_delivey_app.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fastest_delivey_app.R
import com.example.fastest_delivey_app.api.AuthData
import com.example.fastest_delivey_app.api.RegisrtData
import com.example.fastest_delivey_app.api.createApi
import com.example.fastest_delivey_app.ui.theme.MainColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RegistScreen(
    navController: NavController
){
    val api = createApi()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val fio = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }

    val regResponse = remember { mutableStateOf("") }

    if (regResponse.value == "Ok"){
        scope.launch {
            token = api.auth(AuthData(
                email = email.value,
                password = password.value
            )).access_token

            navController.navigate("main_screen")
        }
    }

//    val sustem = rememberSystemUiController()
//
//    LaunchedEffect(key1 = Unit, block = {
//        sustem.setSystemBarsColor(
//            darkIcons = true,
//            color = Color.White
//        )
//    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                backgroundColor = Color(0xFFF3F3F3),
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 0.dp,
                    topRight = 0.dp,
                    bottomLeft = 25.dp,
                    bottomRight = 25.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cooking_1),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }

            MainOutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                label = "Email"
            )

            MainOutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            MainOutlinedTextField(
                value = fio.value,
                onValueChange = { fio.value = it },
                label = "Full name"
            )

            MainOutlinedTextField(
                value = phone.value,
                onValueChange = { phone.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                label = "Phone number"
            )


            Button(
                modifier = Modifier
                    .size(width = 300.dp, height = 68.dp)
                    .padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MainColor
                ),
                shape = AbsoluteRoundedCornerShape(20.dp),
                onClick = {
                    if (
                        !email.value.contains(".") && !email.value.contains("@")
                    ){
                        Toast.makeText(context, "Email contains", Toast.LENGTH_SHORT).show()
                    } else if(
                        password.value.length < 8 && email.value.length < 8
                    ) {
                        Toast.makeText(context, "Pasword or email length", Toast.LENGTH_SHORT).show()
                    }else {
                        scope.launch {
                            try {
                                if (api.registration(RegisrtData(
                                        fio = fio.value,
                                        email = email.value,
                                        password = password.value,
                                        phone = phone.value.toInt()
                                    )).isSuccessful){
                                    regResponse.value = "Ok"
                                }
                            }catch (e:Exception){}
                        }
                    }
                }
            ) {
                Text(
                    "Register Now",
                    color = Color.White
                )
            }

            Button(
                modifier = Modifier
                    .size(width = 300.dp, height = 68.dp)
                    .padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MainColor
                ),
                shape = AbsoluteRoundedCornerShape(20.dp),
                onClick = { navController.navigateUp() }
            ) {
                Text(
                    "Cancel",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MainOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label:String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    Column {
        Text(
            text = label,
            color = Color(0xFF837773),
            modifier = Modifier.padding(top = 5.dp)
        )

        TextField(
            value = value,
            visualTransformation = visualTransformation,
            onValueChange = onValueChange,
            modifier = Modifier.padding(bottom = 3.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            keyboardOptions = keyboardOptions
        )
    }
}