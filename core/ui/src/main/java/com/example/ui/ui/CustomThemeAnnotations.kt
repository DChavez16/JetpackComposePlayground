package com.example.ui.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Theme Preview",
    group = "Conmpact Size Screen Theme Preview",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
@Preview(
    name = "Dark Theme Preview",
    group = "Conmpact Size Screen Theme Preview",
    device = "id:pixel_5",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
annotation class CompactSizeScreenThemePreview



@Preview(
    name = "Light Theme Preview",
    group = "Medium Size Screen Theme Preview",
    device = "spec:parent=8in Foldable",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
@Preview(
    name = "Dark Theme Preview",
    group = "Medium Size Screen Theme Preview",
    device = "spec:parent=8in Foldable",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
annotation class MediumSizeScreenThemePreview



@Preview(
    name = "Light Theme Preview",
    group = "Medium Size Screen Theme Preview",
    device = "id:pixel_tablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
@Preview(
    name = "Dark Theme Preview",
    group = "Medium Size Screen Theme Preview",
    device = "id:pixel_tablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
annotation class ExpandedSizeScreenThemePreview