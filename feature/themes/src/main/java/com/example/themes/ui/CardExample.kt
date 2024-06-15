package com.example.themes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.themes.R
import com.example.ui.theme.AppTheme


@Composable
internal fun CardExample() {

    // A las Card se les aplica un Shape medium (se le aplica por defecto)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.jetpack_compose_icon),
                modifier = Modifier.size(100.dp),
                contentDescription = null
            )
            Column {
                Text(
                    text = "Titulo de prueba",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Cuerpo de prueba",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview
@Composable
private fun CardExamplePreview() {
    AppTheme {
        CardExample()
    }
}