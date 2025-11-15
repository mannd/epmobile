package org.epstudios.epmobile.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CenteringGridLayout(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    spacing: Dp = 16.dp, // Vertical spacing between rows
    itemSpacing: Dp = 16.dp, // Horizontal spacing between items
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val spacingPx = spacing.roundToPx()
        val itemSpacingPx = itemSpacing.roundToPx()

        val availableWidth = constraints.maxWidth
        val columnWidth = (availableWidth - (columns - 1) * itemSpacingPx) / columns
        val itemConstraints = constraints.copy(minWidth = 0, maxWidth = columnWidth)

        val placeables = measurables.map { it.measure(itemConstraints) }
        if (placeables.isEmpty()) {
            return@Layout layout(availableWidth, 0) {}
        }

        val rows = placeables.chunked(columns)

        var totalHeight = 0
        val rowHeights = rows.map { row ->
            val rowHeight = row.maxOfOrNull { it.height } ?: 0
            totalHeight += rowHeight
            rowHeight
        }
        totalHeight += (rows.size - 1).coerceAtLeast(0) * spacingPx

        layout(availableWidth, totalHeight) {
            var yPosition = 0

            rows.forEachIndexed { rowIndex, row ->
                val itemsInRow = row.size
                val totalRowWidth = itemsInRow * columnWidth + (itemsInRow - 1).coerceAtLeast(0) * itemSpacingPx
                var xPosition = (availableWidth - totalRowWidth) / 2

                val rowHeight = rowHeights[rowIndex]

                row.forEach { placeable ->
                    val centeredX = xPosition + (columnWidth - placeable.width) / 2
                    placeable.placeRelative(
                        x = centeredX,
                        y = yPosition
                    )
                    xPosition += columnWidth + itemSpacingPx
                }
                yPosition += rowHeight + spacingPx
            }
        }
    }
}
