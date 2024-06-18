package com.example.ui.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

// Select transitions menu
@Composable
fun CustomDropdownMenu(
    dropdownMenuLabel: String,
    currentElementDisplay: () -> String,
    optionsList: List<String>,
    onElementSelected: (String) -> Unit
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$dropdownMenuLabel: ", style = MaterialTheme.typography.titleMedium)
        Column(modifier = Modifier.weight(1f)) {
            TextField(
                value = currentElementDisplay(),
                onValueChange = {},
                enabled = false,
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(
                            if (dropdownMenuExpanded) 180f else 0f
                        )
                    )
                },
                modifier = Modifier.clickable { dropdownMenuExpanded = !dropdownMenuExpanded }
            )
            DropdownMenu(
                expanded = dropdownMenuExpanded,
                onDismissRequest = { dropdownMenuExpanded = false }
            ) {
                optionsList.forEach { element ->
                    DropdownMenuItem(
                        text = { Text(element) },
                        onClick = {
                            onElementSelected(element)
                            dropdownMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}