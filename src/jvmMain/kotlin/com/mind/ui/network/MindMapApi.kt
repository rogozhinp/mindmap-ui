package com.mind.ui.network

import com.mind.ui.model.BlankMindMapResponse
import com.mind.ui.model.Node
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import java.util.*

class MindMapApi(engine: HttpClientEngine) {

    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            jackson()
        }
    }

    /** Fetch a new blank mind map with the central node */
    suspend fun getBlankMindMap(): BlankMindMapResponse {
        return client.get("http://localhost:8080/api/v1/mindmaps/blank").body()
    }

    /** Add a child node under the specified parent */
    suspend fun addChildNode(parentNodeId: UUID, text: String, x: Double = 0.0, y: Double = 0.0): Node {
        return client.post("http://localhost:8080/api/v1/mindmaps/nodes/$parentNodeId/children") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "text" to text,
                    "x" to x,
                    "y" to y
                )
            )
        }.body()
    }

    companion object {
        /** Default API using CIO engine */
        val Default: MindMapApi by lazy { MindMapApi(CIO.create()) }
    }

    /** Update an existing node's text */
    suspend fun updateNodeText(nodeId: UUID, text: String): Node {
        return client.patch("http://localhost:8080/api/v1/mindmaps/nodes/$nodeId") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("text" to text))
        }.body()
    }
}
