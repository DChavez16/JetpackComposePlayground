@file:OptIn(ExperimentalLayoutApi::class)

package com.example.remotedatabase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserTag
import com.example.model.fakeUserTagsList
import com.example.remotedatabase.NotesViewModel
import com.example.remotedatabase.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun TagsBottomSheet(
    notesViewModel: NotesViewModel = viewModel()
) {

}


@Composable
private fun TagsBottomSheetStart(
    userTagsList: () -> List<UserTag>,
    selectedUserTags: () -> List<UserTag>,
    filterMode: () -> Boolean,
    editMode: () -> Boolean,
    onEditModeIconButtonClick: () -> Unit,
    onTagElementClick: (UserTag) -> Unit,
    onTagElementCloseClick: (UserTag) -> Unit,
    onAddTagIconButtonClick: () -> Unit,
    onMainButtonClick: () -> Unit
) {
    // Start tags bottom sheet content column
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        // Header row
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header title
            Text(
                text = stringResource(R.string.remote_database_tags_bottom_sheet_header),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Header edit icon button IF NOT in edit mode
            // Header instruction IF in edit mode
            if (!editMode()) {
                // Edit icon button
                IconButton(
                    onClick = onEditModeIconButtonClick,
                    colors = IconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(R.string.remote_database_tags_bottom_sheet_edit_tags),
                        modifier = Modifier.size(10.dp)
                    )
                }
            } else {
                // Header instruction
                Text(
                    text = stringResource(R.string.remote_database_tags_bottom_sheet_edit_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tags Flow Row
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 108.dp)
        ) {
            // Input chip for each tag in userTagsList
            for (userTag in userTagsList()) {
                InputChip(
                    selected = selectedUserTags().contains(userTag),
                    onClick = { onTagElementClick(userTag) },
                    label = { Text(text = userTag.tagText) },
                    colors = SelectableChipColors(
                        containerColor = Color.Transparent,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        leadingIconColor = Color.Transparent,
                        selectedLeadingIconColor = Color.Transparent,
                        disabledLeadingIconColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        disabledTrailingIconColor = Color.Transparent,
                        disabledSelectedContainerColor = Color.Transparent
                    ),
                    trailingIcon = {
                        // If in edit more, show a close icon button
                        if (editMode()) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.remote_database_tags_bottom_sheet_delete_tag),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onTagElementCloseClick(userTag) }
                            )
                        }
                    },
                    modifier = Modifier.height(24.dp)
                )
            }

            // IF NOT in edit mode, include de add icon button
            if (!editMode()) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.remote_database_tags_bottom_sheet_create_new_tag),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .height(24.dp)
                        .width(18.dp)
                        .clickable { onAddTagIconButtonClick() }
                )
            }
        }

        // Button row
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ElevatedButton(
                onClick = onMainButtonClick,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Text(
                    text = stringResource(
                        if (editMode()) R.string.remote_database_tags_bottom_sheet_save_button_label
                        else if (filterMode()) R.string.remote_database_tags_bottom_sheet_filter_button_label
                        else R.string.remote_database_tags_bottom_sheet_add_tags_button_label
                    )
                )
            }
        }
    }
}


@Composable
private fun TagsBottomSheetEditTag(
    userTagToEdit: UserTag,
    onReturnButtonClick: () -> Unit,
    onMainButtonClick: (UserTag) -> Unit
) {

    // Text from the tag text to edit
    var userTagText by rememberSaveable { mutableStateOf(userTagToEdit.tagText) }

    // Edit tag bottom sheet content column
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        // Header row
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Return icon button
            IconButton(
                onClick = onReturnButtonClick,
                colors = IconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.remote_database_tags_bottom_sheet_edit_tags)
                )
            }

            // Header title
            Text(
                // If the user tag to edit ID is -1, set "New Tag", otherwise set the tag text
                text = if (userTagToEdit.id == -1L)
                    stringResource(R.string.remote_database_tags_bottom_sheet_new_tag_header) else userTagToEdit.tagText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Input text field
        OutlinedTextField(
            value = userTagText,
            onValueChange = { newUserTagText -> userTagText = newUserTagText },
            label = { Text(
                text = stringResource(R.string.remote_database_tags_bottom_sheet_tag_text_label)
            ) },
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Button row
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ElevatedButton(
                onClick = { onMainButtonClick(userTagToEdit.copy(tagText = userTagText)) },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Text(
                    text = stringResource(
                        // If the user tag to edit ID is -1, set "Create Tag", otherwise set "Save"
                        if (userTagToEdit.id == -1L) R.string.remote_database_tags_bottom_sheet_create_tag_label
                        else R.string.remote_database_tags_bottom_sheet_save_button_label
                    )
                )
            }
        }
    }
}


/*
Previews
 */
@CompactSizeScreenThemePreview
@Composable
private fun TagsBottomSheetStartPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            TagsBottomSheetStart(
                userTagsList = { fakeUserTagsList },
                selectedUserTags = {
                    listOf(
                        fakeUserTagsList[0], fakeUserTagsList[2]
                    )
                },
                filterMode = { true },
                editMode = { false },
                onEditModeIconButtonClick = {},
                onTagElementClick = {},
                onTagElementCloseClick = {},
                onAddTagIconButtonClick = {},
                onMainButtonClick = {}
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun TagsBottomSheetStartEditModePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            TagsBottomSheetStart(
                userTagsList = { fakeUserTagsList },
                selectedUserTags = {
                    listOf(
                        fakeUserTagsList[0], fakeUserTagsList[2]
                    )
                },
                filterMode = { false },
                editMode = { true },
                onEditModeIconButtonClick = {},
                onTagElementClick = {},
                onTagElementCloseClick = {},
                onAddTagIconButtonClick = {},
                onMainButtonClick = {}
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun TagsBottomSheetEditTagPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            TagsBottomSheetEditTag(
//                userTagToEdit = UserTag(-1, ""),
                userTagToEdit = fakeUserTagsList[0],
                onReturnButtonClick = {},
                onMainButtonClick = {}
            )
        }
    }
}


/*
Util
 */
private sealed interface TagsBottomSheetVariant {
    data object Start : TagsBottomSheetVariant
    data class EditTag(val tagToEdit: UserTag = UserTag()) : TagsBottomSheetVariant
}