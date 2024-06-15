package com.example.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.Product
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDAO {

    /**
     * Insert a product into the database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)

    /**
     * Obtains a product from the database
     */
    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProduct(id: Long): Product

    /**
     * Updates a product from the database
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)

    /**
     * Deletes a product from the database
     */
    @Delete
    suspend fun deleteProduct(product: Product)

    /**
     * Obtains a list of products from the database
     */
    @Query("SELECT * FROM product")
    fun getProductsFlow(): Flow<List<Product>>
}