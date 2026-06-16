---
name: Industrial Logic
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#43474f'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#737780'
  outline-variant: '#c3c6d1'
  surface-tint: '#3a5f94'
  primary: '#001e40'
  on-primary: '#ffffff'
  primary-container: '#003366'
  on-primary-container: '#799dd6'
  inverse-primary: '#a7c8ff'
  secondary: '#165db2'
  on-secondary: '#ffffff'
  secondary-container: '#6ca3fd'
  on-secondary-container: '#003874'
  tertiary: '#191f25'
  on-tertiary: '#ffffff'
  tertiary-container: '#2e343a'
  on-tertiary-container: '#969ca4'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d5e3ff'
  primary-fixed-dim: '#a7c8ff'
  on-primary-fixed: '#001b3c'
  on-primary-fixed-variant: '#1f477b'
  secondary-fixed: '#d6e3ff'
  secondary-fixed-dim: '#aac7ff'
  on-secondary-fixed: '#001b3e'
  on-secondary-fixed-variant: '#00458d'
  tertiary-fixed: '#dde3eb'
  tertiary-fixed-dim: '#c1c7cf'
  on-tertiary-fixed: '#161c22'
  on-tertiary-fixed-variant: '#41474e'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  display:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  title-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.05em
  mono-label:
    fontFamily: JetBrains Mono
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  unit: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  panel-width: 320px
  toolbar-height: 56px
---

## Brand & Style
The brand personality is authoritative, precise, and highly efficient. Designed for enterprise maintenance and low-code engineering, the design system prioritizes clarity over decoration. It evokes an emotional response of reliability and "work-mode" focus.

The style is **Corporate / Modern** with a lean toward **Minimalism**. It utilizes a systematic approach to density, ensuring that complex data remains legible. The interface relies on a rigorous grid, high-contrast text, and subtle tonal layering to differentiate between the building canvas and the administrative chrome.

## Colors
The palette is anchored in "Deep Navy" (#003366) for primary actions and structural headers to establish trust. "Professional Blue" (#0055AA) is used for interactive states, secondary buttons, and focal points. 

The neutral scale uses **Slate Grays** to provide a clean, "engineering-grade" backdrop. Backgrounds use a tiered approach: white for primary workspaces and a subtle off-white/gray for utility panels and property rails. Success, warning, and error states should be desaturated to maintain the professional aesthetic, avoiding "neon" tones.

## Typography
This design system uses **Inter** for all UI elements to ensure maximum legibility at small sizes, which is critical for property panels and data grids. 

- **Data Density:** Use `body-sm` for most table content and property inputs.
- **Hierarchy:** Use `label-md` (uppercase) for section headers in sidebars.
- **Monospace:** For technical identifiers, IDs, or low-code logic snippets, use a clean monospace font (e.g., JetBrains Mono) at 12px.
- **Headlines:** Use tight letter spacing for display text to maintain a modern, compact appearance.

## Layout & Spacing
The layout follows a **Fixed-Fluid hybrid grid**. 
- **The Workbench:** A central fluid canvas for the visual builder.
- **The Panels:** Fixed-width sidebars (Left: Components, Right: Properties) at 320px.
- **The Rhythm:** Based on a 4px baseline. Use 8px (sm) for internal element spacing and 16px (md) for container padding. 

On mobile, the sidebars collapse into bottom sheets or full-screen overlays, while the central canvas remains the focal point with horizontal scrolling enabled for large data grids.

## Elevation & Depth
Depth is achieved through **Tonal Layering** and **Low-Contrast Outlines** rather than heavy shadows.
- **Level 0 (Canvas):** The base background, often slightly darker or textured with a dot-grid.
- **Level 1 (Panels):** Pure white background with a 1px solid border (#CBD5E1). No shadow.
- **Level 2 (Floating/Modals):** A very soft, diffused shadow (0px 4px 12px rgba(0,0,0,0.08)) is used only for active floating menus or drag-previews to indicate they are lifted off the canvas.
- **Interactive States:** Pressed buttons move "down" via a darker background fill rather than a change in shadow depth.

## Shapes
The shape language is **Soft (0.25rem)**. This provides a professional balance—square enough to feel precise and technical, but slightly rounded to avoid the aggressive look of 90s enterprise software. 

- **Inputs & Buttons:** 4px (0.25rem) radius.
- **Cards & Property Groups:** 8px (0.5rem) radius.
- **Drag Handles:** Use a "6-dot" icon pattern for handles; avoid fully rounded pills for handles to maintain the geometric theme.

## Components
- **Buttons:** Primary buttons use Deep Navy with white text. Secondary buttons use Slate Gray outlines. No gradients.
- **Data Grids:** Use a zebra-striping pattern for rows (White / #F8FAFC). Borders should be thin and subtle. Header cells should have a slightly darker gray background with bold, 12px uppercase labels.
- **Property Panels:** Use "stacked" inputs. Labels should sit above the input field in `label-md` style. Input fields use a 1px border that turns Professional Blue on focus.
- **Visual Builder Blocks:** Components on the canvas should have a distinct 1px border. When selected, change the border to Professional Blue with a 2px stroke and show a "grab" handle at the top-left.
- **Chips/Status Tags:** Use a "Pastel-on-Dark" approach—very light background tints (e.g., light green) with high-contrast dark text for status indicators like "Active" or "Critical."