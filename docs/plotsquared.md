---
sidebar_position: 3
---

# PlotSquared worlds

If your server already has PlotSquared plot worlds, you do not need grid clusters there. Grid Selector can use the
plots of PlotSquared directly.

## Setup

There is nothing to set up. Install PlotSquared next to Grid Selector and restart the server. On startup, Grid
Selector logs:

```text
Found plot squared. Registering plot squared world adapter
```

From then on, the [selection tool](./selecting.md) works in every world that has a plot area.

Grid Selector checks for PlotSquared only on startup. If you add PlotSquared later, restart the server.

## What is selected

When you right click a plot with the selection tool:

- The plot is selected without its road and wall.
- For merged plots, the largest rectangle of the merged area is selected.
- Clicking a road selects nothing.
- In classic plot worlds, the floor of the plot is the plot height set in PlotSquared. Only what stands above it is
  saved.
- Other plot worlds have no set plot height. There the schematic starts at the lowest layer of the plot that has air
  in it. A bottom layer of a build that covers the whole plot is taken for ground and not saved.

Selecting does not look at who owns a plot. Players with the selection permission can select any plot they can
click, including the plots of other players.

## Worlds with clusters and plots

A world uses either its clusters or its PlotSquared plots, never both. As soon as a world has at least one
[grid cluster](./grid-clusters.md), only the clusters can be selected there. Remove all clusters of the world to use
its plots again.
