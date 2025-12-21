package com.mind.ui.service.impl

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import com.mind.ui.model.Node
import com.mind.ui.service.NodeLayoutService
import com.mind.ui.theme.MindMapTheme
import kotlin.math.roundToInt

class NodeLayoutServiceImpl : NodeLayoutService {

    override fun calculateOffset(
        node: Node,
        canvasCenterX: Float,
        canvasCenterY: Float,
        density: Density
    ): IntOffset {
        val halfWidthPx = with(density) { MindMapTheme.nodeWidth.toPx() / 2f }
        val halfHeightPx = with(density) { MindMapTheme.nodeHeight.toPx() / 2f }

        return IntOffset(
            x = (canvasCenterX + node.x.toFloat() - halfWidthPx).roundToInt(),
            y = (canvasCenterY + node.y.toFloat() - halfHeightPx).roundToInt()
        )
    }
}
