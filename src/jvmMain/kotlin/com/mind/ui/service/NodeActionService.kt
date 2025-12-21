package com.mind.ui.service

import com.mind.ui.model.Node
import java.util.UUID

interface NodeActionService {
    fun onRightClick(node: Node)
    fun onNodeDragged(node: Node, dx: Float, dy: Float)
    fun onRename(node: Node, newText: String)
}