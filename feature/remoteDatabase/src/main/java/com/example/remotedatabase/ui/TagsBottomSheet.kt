package com.example.remotedatabase.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun TagsBottomSheet(

) {

}


@Composable
private fun TagsBottomSheetStart(

) {

}


@Composable
private fun TagsBottomSheetModifyTags(

) {

}


@Composable
private fun TagsBottomSheetEditTag(

) {

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
        TagsBottomSheetStart()
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun TagsBottomSheetModifyTagsPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        TagsBottomSheetModifyTags()
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun TagsBottomSheetEditTagPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        TagsBottomSheetEditTag()
    }
}