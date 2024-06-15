package com.example.product.di

import com.example.product.LocalProductRepository
import com.example.product.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductRepositoryModule {

    /**
     * Binds a [LocalProductRepository] implementation of [ProductRepository]
     */
    @Binds
    abstract fun bindLocalProductRepository(
        localProductRepository: LocalProductRepository
    ): ProductRepository
}