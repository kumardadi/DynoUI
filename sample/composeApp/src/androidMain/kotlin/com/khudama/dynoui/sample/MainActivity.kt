package com.khudama.dynoui.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.khudama.dynoui.compose.DynoRenderer
import com.khudama.dynoui.compose.LocalDynoActionHandler
import com.khudama.dynoui.compose.components.registerCoreComponents
import com.khudama.dynoui.core.DynoParser

val UBER_JSON = """
{
  "actions": {
    "click_ride": { "type": "navigate", "properties": { "route": "rideflow" } },
    "nav_home": { "type": "tab", "properties": { "tab": "home" } }
  },
  "layout": {
    "compact": {
      "type": "column",
      "properties": { "verticalArrangement": "space_between" },
      "modifiers": [
        { "type": "fillMaxSize" },
        { "type": "background", "properties": { "color": "#FFFFFF" } }
      ],
      "children": [
        {
          "type": "box",
          "modifiers": [
            { "type": "fillMaxWidth" },
            { "type": "padding", "properties": { "horizontal": 16, "top": 16, "bottom": 8 } },
            { "type": "height", "properties": { "value": 150 } },
            { "type": "background", "properties": { "color": "#2C3555", "cornerRadius": 16 } },
            { "type": "clickable", "properties": { "actionId": "click_ride" } }
          ],
          "children": [
             {
                 "type": "column",
                 "modifiers": [ { "type": "padding", "properties": { "all": 16 } } ],
                 "children": [
                    { "type": "text", "properties": { "text": "Get out and about", "color": "#FFFFFF", "fontSize": 20, "fontWeight": "bold" } },
                    { "type": "spacer", "properties": { "height": 8 } },
                    { "type": "text", "properties": { "text": "Ride with Uber \u2192", "color": "#FFFFFF", "fontSize": 14 } }
                 ]
             }
          ]
        },
        { "type": "spacer", "properties": { "height": 16 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [
             { "type": "fillMaxWidth" },
             { "type": "padding", "properties": { "horizontal": 16 } }
           ],
           "children": [
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_ride" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "🚗", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Ride", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_food" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "🍔", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Food", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_package" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "📦", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Package", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_reserve" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "📆", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Reserve", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] }
           ]
        },
        { "type": "spacer", "properties": { "height": 8 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [
             { "type": "fillMaxWidth" },
             { "type": "padding", "properties": { "horizontal": 16 } }
           ],
           "children": [
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_grocery" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "🛒", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Grocery", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_transit" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "🚆", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Transit", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_rent" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "🔑", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Rent", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] },
               { "type": "column", "properties": { "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_more" } } ], "children": [ { "type": "box", "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "#EEEEEE", "cornerRadius": 12 } } ], "children": [ { "type": "text", "properties": { "text": "•••", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "More", "fontSize": 12, "color": "#444444", "fontWeight": "bold" } } ] }
           ]
        },
        { "type": "spacer", "properties": { "height": 24 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_between" },
           "modifiers": [
             { "type": "fillMaxWidth" },
             { "type": "padding", "properties": { "horizontal": 16 } },
             { "type": "height", "properties": { "value": 56 } },
             { "type": "background", "properties": { "color": "#F3F3F3", "cornerRadius": 28 } },
             { "type": "clickable", "properties": { "actionId": "click_search" } }
           ],
           "children": [
               {
                 "type": "row",
                 "children": [
                   { "type": "spacer", "properties": { "width": 16 } },
                   { "type": "text", "properties": { "text": "Q", "color": "#000000", "fontSize": 18, "fontWeight": "bold" } },
                   { "type": "spacer", "properties": { "width": 12 } },
                   { "type": "text", "properties": { "text": "Where to?", "color": "#000000", "fontSize": 18, "fontWeight": "bold" } }
                 ]
               },
               {
                 "type": "row",
                 "children": [
                   { 
                     "type": "row",
                     "modifiers": [
                       { "type": "padding", "properties": { "horizontal": 12, "vertical": 8 } },
                       { "type": "background", "properties": { "color": "#FFFFFF", "cornerRadius": 20 } },
                       { "type": "clickable", "properties": { "actionId": "click_now" } }
                     ],
                     "children": [
                        { "type": "text", "properties": { "text": "⌚ Now v", "color": "#000000", "fontSize": 14 } }
                     ]
                   },
                   { "type": "spacer", "properties": { "width": 8 } }
                 ]
               }
           ]
        },
        { "type": "spacer", "properties": { "height": 32 } },
        {
           "type": "row",
           "modifiers": [ 
             { "type": "fillMaxWidth" }, 
             { "type": "padding", "properties": { "horizontal": 16, "vertical": 12 } },
             { "type": "clickable", "properties": { "actionId": "click_saved" } }
           ],
           "children": [
               { "type": "text", "properties": { "text": "★", "fontSize": 20, "color": "#555555" } },
               { "type": "spacer", "properties": { "width": 16 } },
               { "type": "text", "properties": { "text": "Choose a saved place", "color": "#222222", "fontSize": 16, "fontWeight": "bold" } }
           ]
        },
        {
           "type": "row",
           "modifiers": [ 
             { "type": "fillMaxWidth" }, 
             { "type": "padding", "properties": { "horizontal": 16, "vertical": 12 } },
             { "type": "clickable", "properties": { "actionId": "click_map" } }
           ],
           "children": [
               { "type": "text", "properties": { "text": "📍", "fontSize": 20, "color": "#555555" } },
               { "type": "spacer", "properties": { "width": 16 } },
               { "type": "text", "properties": { "text": "Set destination on map", "color": "#222222", "fontSize": 16, "fontWeight": "bold" } }
           ]
        },
        { "type": "spacer", "properties": { "height": 24 } },
        {
           "type": "text",
           "properties": { "text": "Around you", "fontSize": 18, "fontWeight": "bold", "color": "#000000" },
           "modifiers": [ { "type": "padding", "properties": { "horizontal": 16 } } ]
        },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [
              { "type": "fillMaxWidth" },
              { "type": "height", "properties": { "value": 60 } },
              { "type": "background", "properties": { "color": "#FFFFFF" } }
           ],
           "children": [
              { "type": "column", "properties": { "verticalArrangement": "space_evenly", "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "nav_home" } } ], "children": [ { "type": "text", "properties": { "text": "🏠", "fontSize": 24 } }, { "type": "text", "properties": { "text": "Home", "fontSize": 10, "color": "#000000", "fontWeight": "bold" } } ] },
              { "type": "column", "properties": { "verticalArrangement": "space_evenly", "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "nav_activity" } } ], "children": [ { "type": "text", "properties": { "text": "📝", "fontSize": 24 } }, { "type": "text", "properties": { "text": "Activity", "fontSize": 10, "color": "#777777" } } ] },
              { "type": "column", "properties": { "verticalArrangement": "space_evenly", "horizontalAlignment": "center_horizontally" }, "modifiers": [ { "type": "clickable", "properties": { "actionId": "nav_account" } } ], "children": [ { "type": "text", "properties": { "text": "👤", "fontSize": 24 } }, { "type": "text", "properties": { "text": "Account", "fontSize": 10, "color": "#777777" } } ] }
           ]
        }
      ]
    }
  }
}
"""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        registerCoreComponents()

        val parsedSchema = DynoParser.parseSchema(UBER_JSON)
        
        setContent {
            CompositionLocalProvider(
                LocalDynoActionHandler provides { actionId ->
                    val actionDef = parsedSchema.actions?.get(actionId)
                    Toast.makeText(this@MainActivity, "Action Fired: ${"$"}{actionId} (Type: ${"$"}{actionDef?.type})", Toast.LENGTH_SHORT).show()
                }
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    DynoRenderer.Render(parsedSchema.layout["compact"]!!)
                }
            }
        }
    }
}
