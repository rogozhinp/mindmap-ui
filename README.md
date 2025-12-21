# Mind Map Compose UI 

A desktop mind map editor built with **Kotlin Multiplatform** and **Jetpack Compose for Desktop**.

## Features

- Create and edit mind maps with a visual node-based canvas
- Add child nodes via right-click context menu
- Rename nodes by double-clicking
- Drag nodes to reposition them on the canvas
- Automatic child node layout using an alternating offset pattern
- Connection lines rendered between parent and child nodes

## Tech Stack

- **Kotlin** 2.1.0 / **Java** 21
- **Jetpack Compose** 1.7.3 (Desktop)
- **Ktor** 2.3.12 (HTTP client, CIO engine)
- **Jackson** (JSON serialization)
- **JUnit 5**, **Mockito-Kotlin**, **Compose UI Tests**
- **JaCoCo** + **SonarQube** (code quality)
- **Gradle**

## Architecture

MVVM with layered separation:

```
model/        Data models (MindMap, Node)
network/      Ktor HTTP client
repository/   Data access abstraction over the API
service/      Business logic (node layout, node actions)
view/         Composables + MainViewModel
renderer/     Node and line rendering
theme/        Colors, dimensions, styling
```

**Data flow:** View composables → ViewModel (Compose state) → Repository → Ktor HTTP client → Backend API

## Prerequisites

- JDK 21+
- A running backend API at `http://localhost:8080` (see [API Endpoints](#api-endpoints))

## Build & Run

```bash
# Build
./gradlew clean assemble

# Run the desktop application
./gradlew run

# Run tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Package as macOS DMG
./gradlew packageDmg
```

## API Endpoints

The application expects a backend providing these endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/mindmaps/blank` | Create a blank mind map |
| POST | `/api/v1/mindmaps/nodes/{parentNodeId}/children` | Add a child node |
| PATCH | `/api/v1/mindmaps/nodes/{nodeId}` | Update node text |

## Testing

Tests mirror the main source structure under `src/jvmTest/`. Patterns used:

- **Ktor `MockEngine`** for HTTP layer tests
- **Mockito-Kotlin** for service and viewmodel mocking
- **Compose UI tests** with `runDesktopComposeUiTest`
- **JUnit 5** as the test platform
