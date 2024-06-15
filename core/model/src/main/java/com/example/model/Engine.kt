package com.example.model

import javax.inject.Inject

// Interface for a car engine
interface Engine {
    fun getTypeOfEngine(): String
}

// Oil implementation of Engine
class OilEngine @Inject constructor(): Engine {
    override fun getTypeOfEngine(): String {
        return "Oil Engine"
    }
}

// Diesel implementation of Engine
class DieselEngine @Inject constructor(): Engine {
    override fun getTypeOfEngine(): String {
        return "Diesel Engine"
    }
}

// Hybrid implementation of Engine
class HybridEngine @Inject constructor(): Engine {
    override fun getTypeOfEngine(): String {
        return "Hybrid Engine"
    }
}

// Electric implementation of Engine
class ElectricEngine @Inject constructor(): Engine {
    override fun getTypeOfEngine(): String {
        return "Electric Engine"
    }
}