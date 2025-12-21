package com.mind.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object MindMapTheme {
    // Canvas & Background
    val backgroundColor = Color(0xFF14522B)

    // Node Aesthetics
    val nodeColor = Color(0xFF26A69A)
    val nodeTextColor = Color.White
    val nodeWidth = 160.dp
    val nodeHeight = 60.dp
    val nodeShape = RoundedCornerShape(percent = 50)

    // Connection Lines
    val lineColor = Color.White
    val lineWidth = 2f

    /**
     * Spacing used for automatic node placement.
     * Horizontal spacing should generally be > nodeWidth to avoid overlap.
     */
    val nodeHorizontalSpacing = 400.0
    val nodeVerticalSpacing = 200.0
}
