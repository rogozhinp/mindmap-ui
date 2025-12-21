package com.mind.ui.repository.impl

import com.mind.ui.model.Node
import com.mind.ui.network.MindMapApi
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class MindMapRepositoryImplTest {

    private val mindMapId = UUID.randomUUID()
    private val parentNodeId = UUID.randomUUID()

    private fun createMockApi(jsonResponse: String): MindMapApi {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(jsonResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return MindMapApi(mockEngine)
    }

    @Test
    fun `getBlankMindMap returns parsed MindMap and nodes`() = runBlocking {
        val json = """
        {
            "mindMap": {"id": "$mindMapId", "title": "Test Map"},
            "nodes": [
                {"id": "$parentNodeId", "text": "Central Node", "x": 0.0, "y": 0.0, "mindMapId": "$mindMapId"}
            ]
        }
        """.trimIndent()

        val repository = MindMapRepositoryImpl(createMockApi(json))

        val response = repository.getBlankMindMap()

        assertEquals("Test Map", response.mindMap.title)
        assertEquals(1, response.nodes.size)
        assertEquals("Central Node", response.nodes.first().text)
    }

    @Test
    fun `addChildNode returns created Node with correct relative positioning`() = runBlocking {
        val newNodeId = UUID.randomUUID()

        // The expected coordinates based on our new logic (first child = 0 vertical offset)
        val expectedX = 200.0 // Parent X (0.0) + Theme Horizontal Spacing (200.0)
        val expectedY = 0.0   // Parent Y (0.0) + Multiplier (0) * Vertical Spacing

        val json = """
        {"id":"$newNodeId","text":"Child Node","x":$expectedX,"y":$expectedY,"mindMapId":"$mindMapId","parentNodeId":"$parentNodeId"}
        """.trimIndent()

        val repository = MindMapRepositoryImpl(createMockApi(json))
        val parentNode = Node(parentNodeId, "Parent", 0.0, 0.0, mindMapId)

        // Updated call signature: passing an empty list as currentNodes
        val node = repository.addChildNode(parentNode, "Child Node", emptyList())

        assertEquals("Child Node", node.text)
        assertEquals(parentNode.id, node.parentNodeId)
        assertEquals(expectedX, node.x)
        assertEquals(expectedY, node.y)
    }

    @Test
    fun `updateNodeText returns updated Node`() = runBlocking {
        val nodeId = UUID.randomUUID()
        val json = """
        {"id":"$nodeId","text":"Updated Node","x":0.0,"y":0.0,"mindMapId":"$mindMapId"}
        """.trimIndent()

        val repository = MindMapRepositoryImpl(createMockApi(json))

        val updatedNode = repository.updateNodeText(nodeId, "Updated Node")

        assertEquals("Updated Node", updatedNode.text)
        assertEquals(nodeId, updatedNode.id)
    }
}
