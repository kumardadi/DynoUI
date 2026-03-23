# DynoUI JSON Schema Guide for Generative AI Agents

You are generating **DynoUI JSON Schemas**, a strictly typed Server-Driven UI protocol. The client reads this JSON to generate Native UI components natively on mobile.

**CRITICAL RULE: DO NOT INVENT ARBITRARY "type" ELEMENTS.** 
You cannot use `"type": "app_bar"`, `"type": "card"`, or `"type": "avatar"`. The client will permanently ignore unknown types. You must compose complex interfaces purely by nesting the 5 supported primitives below.

---

## 1. Global JSON Schema Root

Your payload must match this exact architectural skeleton:
```json
{
  "apiVersion": "1.0",
  "theme": { ... },
  "actions": { ... },
  "layout": {
    "compact": { ... },
    "medium": { ... },
    "expanded": { ... }
  }
}
```

* `"actions"`: A dictionary of functional behaviors. The key is the `actionId`. E.g., `"nav_home": { "type": "navigate", "properties": { "route": "home" } }`.
* `"layout"`: Contains the actual visual tree mapped to viewport sizes. `"compact"` is the mobile phone size. `"medium"` is tablet/landscape. `"expanded"` is desktop. Use this dictionary to build completely separate nested node trees optimized for different screens.

---

## 2. Directory of Supported UI Elements (Nodes)

Every visual node in the layout tree must declare a `"type"`. You are strictly limited to these exact types:

### `column`
Stacks `"children"` vertically downward.
- **Properties allowed**:
  - `verticalArrangement`: Spacing mapping. Allowed values: `"space_between"`. (Default: top-aligned)
  - `horizontalAlignment`: X-axis alignment. Allowed values: `"center_horizontally"`, `"end"`, `"start"`.

### `row`
Stacks `"children"` horizontally side-by-side.
- **Properties allowed**:
  - `horizontalArrangement`: Spacing mapping. Allowed values: `"space_evenly"`, `"space_between"`. (Default: start-aligned)

### `box`
Layers `"children"` on top of each other. Automatically centers content along X and Y axes natively. No properties required.

### `text`
Draws a string to the screen. 
- **Properties allowed**:
  - `text` (String): The textual content. Supports interpolation via `${variable}`.
  - `color` (String): HEX color string (e.g. `"#FF0000"`).
  - `fontSize` (Int): Size in scalable pixels.
  - `fontWeight` (String): Allowed values: `"bold"`.

### `spacer`
Renders an empty layout block used to explicitly reserve spacing between neighbor elements.
- **Properties allowed**:
  - `width` (Int): Fixed width in dp.
  - `height` (Int): Fixed height in dp.

*(To make a node expand to take up remaining space, DO NOT USE WEIGHT. Instead wrap the elements inside a `row` or `column` that is set to `space_evenly` or `space_between`).*

---

## 3. Directory of Supported Modifiers

Modifiers are declared in a sequentially ordered array on any UI Node: `"modifiers": [{ "type": "padding", "properties": ... }]`.

1. **`padding`**: Applies internal spacing to the element boundary.
   - Properties: `{"all": 16}` OR `{"horizontal": 16, "vertical": 8}` OR `{"top": 12, "bottom": 8}`.
2. **`fillMaxSize`**: Stretches element to completely consume parent width and height constraints.
   - No properties needed.
3. **`fillMaxWidth`**: Stretches element horizontally across parent view. Ideal for rows.
   - No properties needed.
4. **`size`**: Applies a fixed square constraint. Best used on `box` to create round icons.
   - Properties: `{"value": 48}` (Value in dp).
5. **`height`**: Applies fixed vertical height constraint. 
   - Properties: `{"value": 150}`.
6. **`width`**: Applies fixed horizontal width constraint.
   - Properties: `{"value": 150}`.
7. **`background`**: Applies a solid background color and optional clipping rounded corners. 
   - Properties: `{"color": "#FFFFFF", "cornerRadius": 16}`. (Radius sets circular clipping amount).
8. **`clickable`**: Binds a touch interaction to an `actionId` defined in the root actions block. Can be applied to ANY element (e.g., wrap a `row` to make a clickable list map).
   - Properties: `{"actionId": "submit_login_form"}`.

---

## 4. Complete Action Flow Example

Here is how you properly tie a visual button to an Action behavior:

```json
{
  "actions": {
    "login_action": { "type": "api_call", "properties": { "endpoint": "/auth" } }
  },
  "layout": {
    "compact": {
      "type": "box",
      "modifiers": [
         { "type": "fillMaxWidth" },
         { "type": "height", "properties": { "value": 50 } },
         { "type": "background", "properties": { "color": "#1A73E8", "cornerRadius": 25 } },
         { "type": "clickable", "properties": { "actionId": "login_action" } }
      ],
      "children": [
         { "type": "text", "properties": { "text": "SIGN IN", "color": "#FFFFFF", "fontWeight": "bold" } }
      ]
    }
  }
}
```
