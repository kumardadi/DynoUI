package com.khudama.dynoui.core

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DynoParserTest {

    @Test
    fun testParseAndInterpolate() {
        val json = """
            {
                "layout": {
                    "compact": {
                        "type": "text",
                        "properties": {
                            "text": "Hello ${"$"}{user}!"
                        }
                    }
                }
            }
        """.trimIndent()

        val parsed = DynoParser.parseSchema(json)
        val compactNode = parsed.layout["compact"]
        assertNotNull(compactNode)
        assertEquals("text", compactNode.type)
        
        val props = compactNode.properties
        val rawText = (props?.get("text") as? JsonPrimitive)?.content
        assertEquals("Hello \${user}!", rawText)
        
        val state = mapOf("user" to "Android")
        val resolvedProps = DynoStateResolver.resolve(props, state)
        val resolvedText = (resolvedProps?.get("text") as? JsonPrimitive)?.content
        
        assertEquals("Hello Android!", resolvedText)
    }
}
