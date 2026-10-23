---
sidebar_position: 1
---

# Grid Selector

Grid Selector is an add-on for [Schematic Brush Reborn](/schematic-brush-reborn).
It lets players pick schematics for their brush straight from the world. They build on a grid of plots, click
the plots they want, and the builds on those plots become the schematics of their brush.

## Why use it

Normally a schematic has to be saved to a file and found by name before a brush can use it.
With Grid Selector, players skip that:

1. Build a few trees, rocks or ruins, each on its own plot.
2. Click the plots you want with the selection tool.
3. Choose the **Grid** selector in the brush editor. The builds are now in your brush.

This is great for a build world where builders try out ideas. They can change a build and use it in a brush a minute
later. If they like the result, they can export the schematics and keep them.

## Features

- Lay out **grid clusters**: a field of square plots, painted onto the ground for you.
- Use the plots of **PlotSquared** worlds instead, if you have PlotSquared.
- Select plots with a **selection tool**. Only the build on the plot is saved, cut down to its size.
- Use the selection in any brush through the **Grid** selector.
- **Export** a selection as schematic files, for yourself or for everyone.
- Optionally protect new clusters with a **WorldGuard** region.

## Requirements

| What | Version |
|---|---|
| Schematic Brush Reborn | 3 |
| FastAsyncWorldEdit | required |
| Paper | 1.21.11 or newer |
| Java | 25 |
| PlotSquared | optional, for [plot worlds](./plotsquared.md). Built against 7.6.0. |
| WorldGuard | optional, for [cluster regions](./grid-clusters.md#worldguard-regions). Built against 7.0.20. |

See [Getting started](/schematic-brush-reborn/getting-started) to set up the core plugin.
Players still need the Schematic Brush Reborn permissions to use brushes, see the
[permission reference](/schematic-brush-reborn/reference/permissions).

## Installation

1. Make sure Schematic Brush Reborn 3 and FastAsyncWorldEdit are installed.
2. Download the Grid Selector jar from [Lyna](https://lyna.eldoria.de/products/8) and put it into your server's
   `plugins` folder.
3. Restart the server.
4. Give the [permissions](./commands.md#permissions) to the players who should use it.

The add-on creates its files in `plugins/GridSelector/`.

:::note Permissions are not given to operators
The permissions to create, repair and remove clusters and to export schematics are off for everyone by default,
operators included. Grant them with your permission plugin. See [Permissions](./commands.md#permissions).
:::

### Updating from version 1

Version 2 needs Schematic Brush Reborn 3. Your clusters in `clusterWorlds.yml` are kept.

If your config was written by a version older than 1.3.0, update to the latest 1.3 release first and start the server
once. Otherwise version 2 stops with an error on startup.

## How it works

1. **Plots.** You need plots to build on. Lay out a [grid cluster](./grid-clusters.md), or use a
   [PlotSquared world](./plotsquared.md).
2. **Select.** Run `/sbrg select` with an item in your hand. Right click plots to select them. Left click into the
   air to save the selection. See [Selecting schematics](./selecting.md).
3. **Use.** Give a schematic set of your brush the **Grid** selector. It uses the schematics you saved last.
4. **Keep.** A selection is deleted when the server stops. Use `/sbrg export` to keep it as schematic files.

## Next steps

- [Grid clusters](./grid-clusters.md): lay out, repair and remove grids of plots.
- [PlotSquared worlds](./plotsquared.md): select from PlotSquared plots.
- [Selecting schematics](./selecting.md): the selection tool, the Grid selector and export.
- [Commands and permissions](./commands.md): every command and permission.
- [Configuration](./configuration.md): the config files.
