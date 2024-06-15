package com.example.themes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme


@Composable
internal fun FABExample() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // A los SmallFAB se les aplica un Shape medium (se le aplica por defecto)
        SmallFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = { }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

        // A los FAB estandar se les aplica un Shape large (se le aplica por defecto)
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = { }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

        // A los LargeFAB se les aplica un Shape extraLarge (se le aplica por defecto)
        LargeFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = { }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

        // A los ExtendedFAB se les aplica un Shape large (se le aplica por defecto)
        ExtendedFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = { }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(text = "Add", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


@Preview
@Composable
private fun FABExamplePreview() {
    AppTheme {
        FABExample()
    }
}