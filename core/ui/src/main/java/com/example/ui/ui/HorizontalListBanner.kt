package com.example.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PreviewAppTheme

@Composable
fun HorizontalListBanner(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .width(16.dp)
                .padding(1.dp)
        )

        Text(
            text = title,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 8.dp)
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(8f, fill = true)
                .padding(1.dp)
        )
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun HorizontalListBannerPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column {
            HorizontalListBanner(title = "Titulo")
            HorizontalListBanner(title = "Titulo Titulo")
            HorizontalListBanner(title = "Titulo Titulo")
            HorizontalListBanner(title = "Titulo Titulo")
        }
    }
}