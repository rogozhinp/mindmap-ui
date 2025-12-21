package com.mind.ui.service

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import com.mind.ui.model.Node

interface NodeLayoutService {

    fun calculateOffset(
        node: Node,
        canvasCenterX: Float,
        canvasCenterY: Float,
        density: Density
    ): IntOffset
}
