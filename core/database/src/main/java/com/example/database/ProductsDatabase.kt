package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.daos.ProductDAO
import com.example.model.Product


@Database(
    entities = [Product::class],
    exportSchema = false,
    version = 1
)
abstract class ProductsDatabase: RoomDatabase() {
    // DAOS
    abstract fun productDao(): ProductDAO
}