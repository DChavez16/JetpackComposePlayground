package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.ProductsDatabase
import com.example.database.daos.ProductDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides an instances of [ProductsDatabase]
     */
    @Provides
    fun provideProductsDatabase(
        @ApplicationContext context: Context
    ): ProductsDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ProductsDatabase::class.java,
            name = "products.db"
        ).build()
    }

    /**
     * Provides an instance of [ProductDAO]
     */
    @Provides
    fun provideProductsDao(
        productsDatabase: ProductsDatabase
    ): ProductDAO {
        return productsDatabase.productDao()
    }
}