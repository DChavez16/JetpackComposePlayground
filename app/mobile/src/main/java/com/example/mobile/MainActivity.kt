package com.example.mobile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.mobile.ui.ComposePlaygroundApp
import com.example.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // Create a hilt-injected ViewModel
            val mainActivityViewModel = hiltViewModel<MainActivityViewModel>()

            // Dark theme State Flow obtained from the ViewModel
            val darkTheme by mainActivityViewModel.darkTheme.collectAsState()

            // Dynamic theme State Flow obtained from the ViewModel
            val dynamicTheme by mainActivityViewModel.dynamicTheme.collectAsState()

            // Root NacController
            val rootNavController = rememberNavController()

            AppTheme(
                isDarkTheme = { darkTheme },
                isDynamicTheme = { dynamicTheme }
            ) {
                ComposePlaygroundApp(
                    rootNavController = rootNavController
                )
            }

            // If the intent uri is not null, navigate the rootNavController to the given URI
            // ATTENTION: This was auto-generated to handle app links.
            val intentUri: Uri? = intent.data
            if (intentUri != null) {
                Log.d("PlaygroundMainActivity", "Uri obtained from intent: $intentUri")
                rootNavController.navigate(deepLink = intentUri)
            }
        }
    }
}