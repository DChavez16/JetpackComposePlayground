package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mobile.ui.ComposePlaygroundApp
import com.example.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Create an instance of MainActivityViewModel
    @Inject lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // Dark theme State Flow obtained from the ViewModel
            val darkTheme by mainActivityViewModel.darkTheme.collectAsState()

            // Dynamic theme State Flow obtained from the ViewModel
            val dynamicTheme by mainActivityViewModel.dynamicTheme.collectAsState()

            AppTheme(
                isDarkTheme = { darkTheme },
                isDynamicTheme = { dynamicTheme }
            ) {
                ComposePlaygroundApp()
            }
        }
    }
}