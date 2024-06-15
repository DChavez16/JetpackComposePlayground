package com.example.model.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
abstract class EnginesModule {

    // Binds an implementation of oil engine
    @Binds
    @Named("oilEngine")
    abstract fun bindOilEngine(
        oilEngine: com.example.model.OilEngine
    ): com.example.model.Engine

    // Binds an implementation of diesel engine
    @Binds
    @Named("dieselEngine")
    abstract fun bindDieselEngine(
        dieselEngine: com.example.model.DieselEngine
    ): com.example.model.Engine

    // Binds an implementation of hybrid engine
    @Binds
    @Named("hybridEngine")
    abstract fun bindHybridEngine(
        hybridEngine: com.example.model.HybridEngine
    ): com.example.model.Engine

    // Binds an implementation of electric engine
    @Binds
    @Named("electricEngine")
    abstract fun bindElectricEngine(
        electricEngine: com.example.model.ElectricEngine
    ): com.example.model.Engine
}