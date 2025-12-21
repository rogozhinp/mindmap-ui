package com.mind.ui.model

import java.util.UUID

data class MindMap(
    val id: UUID,
    val title: String
)

data class Node(
    val id: UUID,
    val text: String,
    val x: Double,
    val y: Double,
    val mindMapId: UUID,
    val parentNodeId: UUID? = null
)

data class BlankMindMapResponse(
    val mindMap: MindMap,
    val nodes: List<Node>
)