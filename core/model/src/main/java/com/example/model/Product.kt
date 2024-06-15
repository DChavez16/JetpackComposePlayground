package com.example.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "availability")
    var isAvailable: Boolean,

    @ColumnInfo(name = "description")
    var description: String
)

fun Product.isValidForSave(): Boolean {
    return this.name.isNotEmpty() && this.description.isNotEmpty()
}



val emptyProduct = Product(
    name = "",
    quantity = 0,
    description = "",
    isAvailable = false
)


val fakeProductsList = listOf(
    Product(1, "Product 1", 10, true, "Product 1 description"),
    Product(2, "Product 2", 20, false, "Product 2 description"),
    Product(3, "Product 3", 30, true, "Product 3 description")
)