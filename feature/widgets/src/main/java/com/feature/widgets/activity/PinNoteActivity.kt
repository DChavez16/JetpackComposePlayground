package com.feature.widgets.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.theme.AppTheme
import com.example.ui.theme.PreviewAppTheme
import com.feature.widgets.receiver.UpdatePinnedNoteIdBroadcastReceiver

class PinNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val glanceWidgetId = intent.getIntExtra("GLANCE_ID_INT_KEY", -1)

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        glanceId = glanceWidgetId,
                        a = {
                            val intent =
                                Intent(UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID).apply {
                                    putExtra("new_pinned_note_id", -1)
                                    putExtra("widget_id", glanceWidgetId)
                                }

                            sendBroadcast(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    glanceId: Int,
    a: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Glance id: $glanceId",
        modifier = modifier.clickable { a }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PreviewAppTheme {
        Greeting(-1, {})
    }
}