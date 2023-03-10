package com.example.fastest_delivey_app.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface Api {

    @GET("/Menu/Items")
    suspend fun getMenuItems():List<MenuItem>

    @GET("/Menu/Items/{id}")
    suspend fun getMenuItem(
        @Path("id") id:Int
    ):MenuItem

    @POST("/Registration")
    suspend fun registration(
        @Body body:RegisrtData
    ):Response<Unit?>

    @POST("/Authorization")
    suspend fun auth(
        @Body body:AuthData
    ):AuhtResult

    @POST("/History/Order")
    suspend fun postOrder(
        @Body body: HistoryOrder,
        @Header("Authorization") token:String
    )

    @GET("/History/Order")
    suspend fun getOrders(
        @Header("Authorization") token:String
    ):List<HistoryOrder>
}

fun createRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:5000")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun createApi(retrofit: Retrofit = createRetrofit()):Api = retrofit.create<Api>()

data class HistoryOrder(
    val id:Int = 0,
    val courierId:Int = 0,
    val address:String = "Test adress",
    val date:String,
    val price:Int? = null,
    val items:List<HistoryOrderItem>
)

data class HistoryOrderItem(
    val id:Int = 0,
    val number:Int,
    val menuItem:MenuItem
)

data class MenuItem(
    val id:Int = 0,
    val name:String = "",
    var price:Int = 0,
    val description:String = "",
    val images: List<Image> = emptyList(),
    val type: MenuType = MenuType.FOOD
)

enum class MenuType(val title:String) {
    FOOD("Foods"),
    DRINK("Drinks"),
    SNACK("Snacks"),
    SAUCE("Sauce")
}

data class Image(
    val id:Int,
    val url:String
)

data class RegisrtData(
    val email:String,
    val fio:String,
    val phone:Int,
    val password:String
)

data class AuthData(
    val email:String,
    val password:String
)

data class AuhtResult(
    val access_token:String
)