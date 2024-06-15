package com.example.model.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object CarsModule {

    // Provides an instance of an oil engine car
    @Provides
    @Named("oilEngineCar")
    fun provideOilEngineCar(
        @Named("oilEngine") oilEngine: com.example.model.Engine
    ): com.example.model.Car = com.example.model.Car(oilEngine)

    // Provides an instance of a diesel engine car
    @Provides
    @Named("dieselEngineCar")
    fun provideDieselEngineCar(
        @Named("dieselEngine") dieselEngine: com.example.model.Engine
    ): com.example.model.Car = com.example.model.Car(dieselEngine)

    // Provides an instance of an hybrid engine car
    @Provides
    @Named("hybridEngineCar")
    fun provideHybridEngineCar(
        @Named("hybridEngine") hybridEngine: com.example.model.Engine
    ): com.example.model.Car = com.example.model.Car(hybridEngine)

    // Provides an instance of an electric engine car
    @Provides
    @Named("electricEngineCar")
    fun provideElectricEngineCar(
        @Named("electricEngine") electricEngine: com.example.model.Engine
    ): com.example.model.Car = com.example.model.Car(electricEngine)
}