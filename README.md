# DynoUI Developer Documentation

DynoUI is a Kotlin Multiplatform Server-Driven UI (SDUI) library that renders dynamic JSON payloads into native UI (currently leveraging Jetpack Compose). This documentation covers integration, extension, and native bridging.

---

## 1. Core Architecture & Schema 

The core of DynoUI revolves around the `DynoSchema` domain model. When the server sends a dynamic payload, it is strictly deserialized into the following object:

```kotlin
@Serializable
data class DynoSchema(
    val apiVersion: String? = null,
    val theme: DynoTheme? = null, // Holds color palettes, text styles, and reusable modifier "classes" 
    val actions: Map<String, DynoAction>? = null, // Dictionary mapping action IDs to behavior definitions
    val layout: Map<String, DynoNode> // The actual UI tree, mapped by screen breakpoint (e.g. "compact")
)
```

The UI tree consists entirely of `DynoNode` objects defined as:
```kotlin
@Serializable
data class DynoNode(
    val type: String, // e.g. "column", "text", "row"
    val properties: JsonObject? = null, // Component-specific arguments
    val modifiers: List<DynoModifier>? = null, // Constraints like padding/sizing
    val classes: List<String>? = null, // References to theme classes
    val children: List<DynoNode>? = null // Nested elements
)
```

---

## 2. Platform Setup & Integration

### Minimal KMP Setup

To integrate DynoUI into a Compose Multiplatform app (Android/iOS shared layer), include the libraries for both the logical core and UI renderer:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation(project(":core"))
        implementation(project(":compose"))
        implementation(libs.kotlinx.serialization.json)
    }
}
```

### Initializing the Render Canvas

1. Parse the JSON using `DynoParser`.
2. Register the base library elements using `registerCoreComponents()`.
3. Provide the global `ActionHandler` for click events.
4. Call `DynoRenderer.Render()` on the root layout node for the device's screen size.

```kotlin
val parsedSchema = DynoParser.parseSchema(SERVER_JSON_STRING)

setContent {
    // 1. Initialize primitive elements (Text, Box, Row, Column, Spacer)
    registerCoreComponents()

    // 2. Set up the global Action Interceptor
    CompositionLocalProvider(
        LocalDynoActionHandler provides { actionId ->
            // Extract the action block defined by the server for this ID
            val actionDef = parsedSchema.actions?.get(actionId)
            
            when (actionDef?.type) {
                 "navigate" -> navController.navigate(actionDef.properties?.get("route")?.jsonPrimitive?.content ?: "")
                 "api_call" -> viewModel.performMutation(actionDef.properties?.get("endpoint")?.jsonPrimitive?.content)
                 else -> println("Unhandled click action: $actionId")
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // 3. Render the UI for Mobile ("compact").
            DynoRenderer.Render(parsedSchema.layout["compact"]!!)
        }
    }
}
```

---

## 3. Creating Custom Native Components

If the server specifies `"type": "video_player"`, the framework will silently ignore the node unless you bind a Native Compose function to it. 

Use the Singleton `DynoRegistry` to map strings to Compose functions.

### Example: Creating a Native Image Component

```kotlin
DynoRenderer.registry.register("image") { node ->
    // 1. Extract Custom Properties
    val url = node.properties?.get("url")?.jsonPrimitive?.content ?: ""
    val contentScaleStr = node.properties?.get("scale")?.jsonPrimitive?.content
    
    val contentScale = when(contentScaleStr) {
        "crop" -> ContentScale.Crop
        "fit" -> ContentScale.Fit
        else -> ContentScale.Crop
    }
    
    // 2. Render Native Jetpack Compose Element
    // Be sure to pass node.modifiers through the framework translator!
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = contentScale,
        modifier = Modifier.applyDynoModifiers(node.modifiers) 
    )
}
```

---

## 4. State Interpolation (Data Binding)

A common SDUI requirement is sending templates from the server and populating the data locally on the device (e.g. `Welcome ${"$"}{user.firstName}!`).

You can inject local app state directly into the node's properties immediately before rendering them.

```kotlin
// Data Map (usually sourced from ViewModel)
val clientStateMap = mapOf("user.firstName" to "Jane Doe", "battery" to "84%")

DynoRenderer.registry.register("text") { node ->
    // Resolves "${"$"}{user.firstName}" inside the JSON string to "Jane Doe"
    val interpolatedProps = DynoStateResolver.resolve(node.properties, clientStateMap)
    
    val textValue = interpolatedProps?.get("text")?.jsonPrimitive?.content ?: ""
    Text(
         text = textValue,
         modifier = Modifier.applyDynoModifiers(node.modifiers)
    )
}
```

*Note: The system attempts to avoid resolving modifiers or structural constraints, primarily substituting string values within the `properties` block.*
