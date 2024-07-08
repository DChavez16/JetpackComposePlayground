package com.example.datapersistence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor


@Composable
fun DataPersistenceScreen(
    onMenuButtonClick: () -> Unit,
    dataPersistenceViewModel: DataPersistenceViewModel = hiltViewModel()
) {

    // Collect the number value as a Flow
    val number by dataPersistenceViewModel.numberFlow.collectAsState()
    // Collect the color value as a Flow
    val color by dataPersistenceViewModel.colorFlow.collectAsState()

    val topBarTitle = stringResource(R.string.data_persistence_screen_top_bar_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topBarTitle },
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        DataPersistenceScreenContent(
            number = { number },
            color = { color },
            onNumberChange = dataPersistenceViewModel::updateNumberPreference,
            onColorChange = dataPersistenceViewModel::updateColorPreference,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * Displays the current number and colors, as the controls to change their values
 */
@Composable
private fun DataPersistenceScreenContent(
    number: () -> Int,
    color: () -> Color,
    onNumberChange: (String) -> Unit,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        DisplayContent(
            number = number,
            color = color
        )

        InputFields(
            currentNumber = number,
            currentColor = color,
            onNumberChange = onNumberChange,
            onColorChange = onColorChange
        )
    }
}


/**
 * Composable that displays the current number within the selected background color
 */
@Composable
private fun DisplayContent(
    number: () -> Number,
    color: () -> Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .sizeIn(maxWidth = 320.dp)
                .aspectRatio(1f)
                .drawBehind {
                    drawRect(
                        color = color()
                    )
                }
        ) {
            Text(
                text = "${number()}",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 256.sp,
                modifier = Modifier.semantics { testTag = "NumberDisplay" }
            )
        }
    }
}


/**
 * Composable that contains the fields to change the number and background color
 */
@Composable
private fun InputFields(
    currentNumber: () -> Int,
    currentColor: () -> Color,
    onNumberChange: (String) -> Unit,
    onColorChange: (Color) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        NumberInputField(
            currentNumber = currentNumber,
            onNumberChange = onNumberChange
        )

        ColorInputField(
            currentColor = currentColor,
            onColorChange = onColorChange
        )
    }
}


/**
 * Composable that contains the field to change the number
 */
@Composable
private fun NumberInputField(
    currentNumber: () -> Int,
    onNumberChange: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.data_persistence_screen_number_input_label),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = "${currentNumber()}",
            onValueChange = { newNumberString -> onNumberChange(newNumberString) },
            textStyle = MaterialTheme.typography.titleLarge,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .weight(8f)
                .semantics { testTag = "NumberTextField" }
        )
    }
}


/**
 * Composable that contains the field to change the background color
 */
@Composable
private fun ColorInputField(
    currentColor: () -> Color,
    onColorChange: (Color) -> Unit
) {
    // Flag value that indicates if the dialog is open
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.data_persistence_screen_color_input_label),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(8f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .clickable { isDialogOpen = true }
                    .semantics { contentDescription = "Change background color" }
                    .background(
                        color = currentColor(),
                        shape = RoundedCornerShape(size = 8.dp)
                    )
            )
        }
    }

    // Code that shows the dialog if is open
    if (isDialogOpen) {
        ColorPickerDialog(
            currentColor = currentColor,
            onDismiss = { isDialogOpen = false },
            onColorSelected = { newColor ->
                onColorChange(newColor)
                isDialogOpen = false
            }
        )
    }
}


/**
 * Dialog that overrides the screen to let the user select a color
 */
@Composable
private fun ColorPickerDialog(
    currentColor: () -> Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    var newCurrentColor by remember { mutableStateOf(currentColor) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = "ColorPickerDialog" }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Dialog title
                Text(
                    text = stringResource(R.string.data_persistence_screen_color_picker_header),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                // Color preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .drawBehind {
                            drawRect(
                                color = newCurrentColor()
                            )
                        }
                )

                // Color picker
                ClassicColorPicker(
                    color = HsvColor.from(color = newCurrentColor()),
                    onColorChanged = { hsvColor: HsvColor ->
                        newCurrentColor = { hsvColor.toColor() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .semantics { testTag = "ColorPicker" }
                )

                // Confirm and Dismiss Buttons
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.data_persistence_screen_color_picker_confirm_button_label),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(5f)
                            .clickable { onColorSelected(newCurrentColor()) }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.data_persistence_screen_color_picker_dismiss_button_dialog),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(5f)
                            .clickable { onDismiss() }
                    )
                }
            }
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun DataPersistenceScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DataPersistenceScreenContent(
            number = { 0 },
            color = { Color.Red },
            onNumberChange = {},
            onColorChange = {}
        )
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun ColorInputDialogPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ColorPickerDialog(
            currentColor = { Color.Red },
            onDismiss = {},
            onColorSelected = {}
        )
    }
}