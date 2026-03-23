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
import com.khudama.dynoui.compose.LocalDynoListState
import com.khudama.dynoui.compose.LocalDynoTheme
import com.khudama.dynoui.compose.components.registerCoreComponents
import com.khudama.dynoui.core.DynoParser

val UBER_JSON = """
{
  "theme": {
    "colors": {
      "lightGray": "#EEEEEE",
      "white": "#FFFFFF",
      "darkText": "#444444",
      "primaryBg": "#2C3555",
      "navBg": "#F3F3F3"
    },
    "classes": {
      "icon_bg": {
         "modifiers": [ { "type": "size", "properties": { "value": 60 } }, { "type": "background", "properties": { "color": "${"$"}{theme.colors.lightGray}", "cornerRadius": 12 } } ]
      },
      "grid_label": {
         "properties": { "fontSize": 12, "color": "${"$"}{theme.colors.darkText}", "fontWeight": "bold" }
      },
      "grid_col": {
         "properties": { "horizontalAlignment": "center_horizontally" }
      },
      "banner": {
         "modifiers": [
            { "type": "fillMaxWidth" },
            { "type": "padding", "properties": { "horizontal": 16, "top": 16, "bottom": 8 } },
            { "type": "height", "properties": { "value": 150 } },
            { "type": "background", "properties": { "color": "${"$"}{theme.colors.primaryBg}", "cornerRadius": 16 } }
         ]
      },
      "search_bar": {
         "modifiers": [
             { "type": "fillMaxWidth" },
             { "type": "padding", "properties": { "horizontal": 16 } },
             { "type": "height", "properties": { "value": 56 } },
             { "type": "background", "properties": { "color": "${"$"}{theme.colors.navBg}", "cornerRadius": 28 } }
         ]
      }
    }
  },
  "actions": {
    "click_ride": { "type": "navigate", "properties": { "route": "rideflow" } },
    "nav_home": { "type": "tab", "properties": { "tab": "home" } },
    "click_past_ride": { "type": "navigate", "properties": { "route": "receipt" } }
  },
  "layout": {
    "compact": {
      "type": "column",
      "properties": { "verticalArrangement": "space_between" },
      "modifiers": [
        { "type": "fillMaxSize" },
        { "type": "background", "properties": { "color": "${"$"}{theme.colors.white}" } }
      ],
      "children": [
        {
          "type": "box",
          "classes": ["banner"],
          "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_ride" } } ],
          "children": [
             {
                 "type": "column",
                 "modifiers": [ { "type": "padding", "properties": { "all": 16 } } ],
                 "children": [
                    { "type": "text", "properties": { "text": "Get out and about", "color": "${"$"}{theme.colors.white}", "fontSize": 20, "fontWeight": "bold" } },
                    { "type": "spacer", "properties": { "height": 8 } },
                    { "type": "text", "properties": { "text": "Ride with Uber \u2192", "color": "${"$"}{theme.colors.white}", "fontSize": 14 } }
                 ]
             }
          ]
        },
        { "type": "spacer", "properties": { "height": 16 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [ { "type": "fillMaxWidth" }, { "type": "padding", "properties": { "horizontal": 16 } } ],
           "children": [
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_ride" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "🚗", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Ride" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_food" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "🍔", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Food" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_package" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "📦", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Package" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_reserve" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "📆", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Reserve" }, "classes": ["grid_label"] } ] }
           ]
        },
        { "type": "spacer", "properties": { "height": 8 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [ { "type": "fillMaxWidth" }, { "type": "padding", "properties": { "horizontal": 16 } } ],
           "children": [
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_grocery" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "🛒", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Grocery" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_transit" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "🚆", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Transit" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_rent" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "🔑", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "Rent" }, "classes": ["grid_label"] } ] },
               { "type": "column", "classes": ["grid_col"], "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_more" } } ], "children": [ { "type": "box", "classes": ["icon_bg"], "children": [ { "type": "text", "properties": { "text": "•••", "fontSize": 24 } } ] }, { "type": "spacer", "properties": { "height": 8 } }, { "type": "text", "properties": { "text": "More" }, "classes": ["grid_label"] } ] }
           ]
        },
        { "type": "spacer", "properties": { "height": 24 } },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_between" },
           "classes": ["search_bar"],
           "modifiers": [ { "type": "clickable", "properties": { "actionId": "click_search" } } ],
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
                       { "type": "background", "properties": { "color": "${"$"}{theme.colors.white}", "cornerRadius": 20 } },
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
           "type": "lazy_column",
           "properties": { "listKey": "past_rides" },
           "modifiers": [
              { "type": "fillMaxWidth" },
              { "type": "padding", "properties": { "horizontal": 16, "vertical": 8 } }
           ],
           "children": [
              {
                 "type": "row",
                 "properties": { "horizontalArrangement": "space_between" },
                 "modifiers": [ 
                    { "type": "fillMaxWidth" }, 
                    { "type": "padding", "properties": { "vertical": 12 } },
                    { "type": "clickable", "properties": { "actionId": "click_past_ride" } }
                 ],
                 "children": [
                    {
                       "type": "column",
                       "children": [
                          { "type": "text", "properties": { "text": "${"$"}{destination}", "fontWeight": "bold", "fontSize": 16 } },
                          { "type": "spacer", "properties": { "height": 4 } },
                          { "type": "text", "properties": { "text": "${"$"}{date}", "color": "#555555", "fontSize": 14 } }
                       ]
                    },
                    { "type": "text", "properties": { "text": "${"$"}{price}", "fontWeight": "bold", "fontSize": 16 } }
                 ]
              }
           ]
        },
        {
           "type": "row",
           "properties": { "horizontalArrangement": "space_evenly" },
           "modifiers": [
              { "type": "fillMaxWidth" },
              { "type": "height", "properties": { "value": 60 } },
              { "type": "background", "properties": { "color": "${"$"}{theme.colors.white}" } }
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
        
        val pastRides = listOf(
            mapOf("destination" to "SFO Airport", "date" to "Oct 24, 2026", "price" to "$42.50"),
            mapOf("destination" to "Downtown Mall", "date" to "Oct 22, 2026", "price" to "$12.00"),
            mapOf("destination" to "Central Park", "date" to "Oct 18, 2026", "price" to "$18.75")
        )

        setContent {
            CompositionLocalProvider(
                LocalDynoActionHandler provides { actionId ->
                    val actionDef = parsedSchema.actions?.get(actionId)
                    Toast.makeText(this@MainActivity, "Action Fired: ${"$"}{actionId} (Type: ${"$"}{actionDef?.type})", Toast.LENGTH_SHORT).show()
                },
                LocalDynoListState provides mapOf("past_rides" to pastRides)
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    DynoRenderer.RenderResponsive(parsedSchema)
                }
            }
        }
    }
}
