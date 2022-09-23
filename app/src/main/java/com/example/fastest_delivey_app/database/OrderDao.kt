package com.example.fastest_delivey_app.database

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Composable
fun createOrderDatabase():OrderDatabase {
    val context = LocalContext.current

    return Room.databaseBuilder(
        context.applicationContext,
        OrderDatabase::class.java,
        "order_database"
    ).build()
}

@Composable
fun createOrderDao(
    database: OrderDatabase = createOrderDatabase()
):OrderDao = database.orderDao()

@Database(
    entities = [Order::class],
    version = 1
)
abstract class OrderDatabase :RoomDatabase() {
    abstract fun orderDao():OrderDao
}

@Dao
interface OrderDao {

    @Query("SELECT * FROM `order` ORDER BY price")
    fun getOrders():Flow<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM `order`")
    suspend fun clear()
}

@Entity(
    tableName = "order"
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val icon:String,
    val name:String,
    var price:Int,
    val defultPrice:Int,
    var count:Int
)