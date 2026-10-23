---
sidebar_position: 4
---

# Selecting schematics

Selecting works the same in [grid clusters](./grid-clusters.md) and in [PlotSquared worlds](./plotsquared.md).
You click plots with the selection tool, save the selection, and use it in a brush with the **Grid** selector.

## The selection tool

Hold an item that is not a block, for example a stick, and run:

```text
/sbrg select
```

The selection tool is now bound to that item. You see "Selection tool bound. Select schematics with right clicks.
Left click when you are done."

The selection tool is a WorldEdit brush. It is bound to the item type, like other brushes, and replaces a brush
that was bound to it before. Binding it needs `gridselector.select`. Using it needs Schematic Brush Reborn's
`schematicbrush.brush.use`.

### Select plots

**Right click** a plot to select it. Aim at a block on the plot, like you aim a brush.

- The corners of the selected build are marked with blocks, see [Markers](#markers).
- Right click a selected plot again to remove it from the selection.
- Clicking outside a plot gives the message "No grid or plot present." This includes the gaps between the plots of a
  cluster and the roads of a plot world.

### Save the selection

**Left click into the air** to save all selected plots. You see "Selections saved. Use the grid Selector to use
them." The markers go away and you can start a new selection.

- The click has to go into the air. Left clicking a block does not save.
- If nothing is selected, you get "You dont have any regions selected".
- Each save replaces your previous one. The Grid selector always uses your newest save.

If you change to another world, your selected plots are dropped and you see "You changed the world. All selections
will be removed."

### What is saved

Each selected plot becomes one schematic. Grid Selector does not save the whole plot. It cuts out the build:

- Only the area inside the plot border is used.
- The floor of the plot is not saved. The schematic starts on the first layer above it.
- Air around the build is cut away on all sides and at the top, so the schematic is as small as the build.

Selecting a plot with nothing built on it gives the message "This plot is empty."

### Markers

While a plot is selected, you see blocks that mark it:

- **Corners.** Eight blocks just outside the corners of the box that is saved. On by default.
- **Border.** A ring of blocks on top of the plot border. Off by default.

Only you can see the markers. They are not real blocks and go away when you deselect the plot or save. Switch them
on or off, and pick the block, in the [configuration](./configuration.md#highlight).

## The Grid selector

Grid Selector adds a selector named **Grid** to Schematic Brush Reborn. It has no values. It gives a schematic set
the schematics of your newest save.

To use it, open the brush editor with `/sbr`, open a schematic set, change its selector and pick **Grid**.
Or use the command:

```text
/sbr modifyset <id> selector Grid
```

See [Getting started](/schematic-brush-reborn/getting-started) if you have not built a brush yet.

- The Grid selector needs `gridselector.select`. Players without it do not see it in the dialog editor.
- A selector must find at least one schematic. Save a selection before you pick Grid.
- A set remembers the schematics its selector found. After you saved a new selection, click **Refresh Schematics**
  in the editor, or run `/sbr refreshSchematics` while you hold the brush.

:::danger Selections do not survive a restart
Saved selections are kept in `plugins/GridSelector/schematics`. Grid Selector empties that folder every time the
server starts or stops. After a restart, every brush and preset that uses the Grid selector finds nothing.
Use [export](#export) to keep the schematics you want.
:::

## Export

Export copies your newest saved selection into Schematic Brush Reborn's schematic folder. There it is an ordinary
set of schematic files that stays.

```text
/sbrg export <name> [-g] [-f]
```

| Argument | Required | What it means |
|---|---|---|
| `name` | yes | Start of the file names. |
| `-g` | no | Export for everyone instead of only for you. Needs `gridselector.export.global`. |
| `-f` | no | Overwrite files that already exist. |

Every schematic of the selection becomes its own file, named after `name` and numbered from 0 in the order you
selected the plots. `/sbrg export oak` writes `oak_0.schem`, `oak_1.schem` and so on.

Where the files go, with the default `schematicPath`:

| Export | Folder | Who can use it |
|---|---|---|
| without `-g` | `plugins/SchematicBrushReborn/schematics/<your UUID>/` | only you |
| with `-g` | `plugins/SchematicBrushReborn/schematics/` | everyone |

`plugins/SchematicBrushReborn/schematics` is one of Schematic Brush Reborn's default schematic folders. It notices
the new files after a few seconds. Pick them with the Name selector, for example `oak_*`. A folder named after your
UUID belongs to you, so only your selectors find the files inside it.

If you change `schematicPath` in the [configuration](./configuration.md#general), make sure Schematic Brush Reborn
reads that folder too. See [Configuration](/schematic-brush-reborn/configuration).

:::warning
Without `-f`, the export stops at the first file that already exists, with the message "Name already in use."
Files before it may already be copied. Pick a new name or add `-f`.
:::
