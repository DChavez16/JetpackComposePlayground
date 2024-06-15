package com.example.dependencyinjection

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class DependencyInjectionViewModel @Inject constructor(
    @Named("oilEngineCar") val oilCar: com.example.model.Car,
    @Named("dieselEngineCar") val dieselCar: com.example.model.Car,
    @Named("hybridEngineCar") val hybridCar: com.example.model.Car,
    @Named("electricEngineCar") val electricCar: com.example.model.Car
): ViewModel()