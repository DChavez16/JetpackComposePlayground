package com.example.persistentWork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersistentWorkViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
): ViewModel() {

    // Define a Flow for isWorkRunning value and its backing property
    private val _isWorkRunning = MutableStateFlow(false)
    val isWorkRunning: StateFlow<Boolean> = _isWorkRunning

    init {
        // Collect Flow for isWorkRunning value from NotificationRepository
        viewModelScope.launch {
            notificationRepository.isWorkRunning()
                .collect { newDynamicThemeValue ->
                    _isWorkRunning.value = newDynamicThemeValue
                }
        }
    }

    // Show notification
    fun showNotification() {
        notificationRepository.showNotification()
    }

    // Cancel notification
    fun cancelNotification() {
        notificationRepository.cancelNotification()
    }
}