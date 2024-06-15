package com.example.model

class Car(
    private val engine: Engine
) {
    fun getCarEngineType() = engine.getTypeOfEngine()
}