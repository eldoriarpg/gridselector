---
sidebar_position: 2
---

# Grid clusters

A grid cluster is a field of square plots that Grid Selector paints onto the ground. Each plot holds one build.
Players select plots later with the [selection tool](./selecting.md).

Any world can have clusters. A world with at least one cluster is a **cluster world**. You can have as many clusters
in a world as you like, as long as they do not overlap.

All cluster commands are listed on the [commands page](./commands.md#cluster-commands).

## What a cluster looks like

Seen from above, a cluster with two rows and two columns looks like this:

```text
B B B B B B B B B o B B B B B B B B B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B f f f f f f f B o B f f f f f f f B
B B B B B B B B B o B B B B B B B B B
o o o o o o o o o o o o o o o o o o o
B B B B B B B B B o B B B B B B B B B
B f f f f f f f B o B f f f f f f f B
...
```

- `f` is the **floor** of a plot. Builds go on top of it.
- `B` is the **border** around each plot. It is one block wide.
- `o` is the **offset**: the gap between two plots.

The cluster is painted as a single layer of blocks. Everything above that layer stays as it is.

## Lay out a cluster

1. Find or make a **flat** area that is large enough. A flat world works well.
2. Stand where the cluster should start and look in the direction it should grow.
3. Run `/sbrg cluster create`. The cluster builder opens in chat.
4. Change the settings with the `[change]` buttons, see [Settings](#settings).
5. Click `[Create]`.

Grid Selector paints the cluster and tells you how many blocks it changed. The cluster is saved right away, and you
see its [ID](#cluster-ids).

### Where the cluster goes

- **Start.** The block you stood on when you opened the builder becomes the outer corner of the first plot.
  Use `[change]` next to *Location* to move the start to where you stand now.
- **Direction.** Rows run in the direction you looked when you opened the builder: north, east, south or west.
  Use `[change]` next to *Direction* to take the direction you look at now.
- **Side.** Columns grow to your right. Use `[change]` next to *Expand* to switch between right and left.
- **Height.** The cluster is painted on the highest block at its start. That block layer becomes the floor of
  every plot. Make sure nothing is above the start: a roof or leaves there would set the height instead.
- **World.** The cluster is created in the world of its start, even if you are somewhere else when you click
  `[Create]`.

:::tip
Painting a cluster is a normal WorldEdit change in your session. If the result is not what you wanted, `//undo`
puts the blocks back. The cluster itself stays registered, so remove it with `/sbrg cluster remove` as well.
:::

A cluster must not overlap another cluster in the same world. Clusters may sit right next to each other, but they
must not share a single block. If they do, you get the message
"Cluster overlaps with cluster" and the ID of the other cluster. Move or shrink the new cluster, or remove the
other one first.

The paint job counts against your WorldEdit limits. If the cluster is too large for them, you get the message
"Grid exceeds maximum world edit size." The cluster is then not created and your session stays open. The part that
was already painted stays in the world; `//undo` removes it. Make the cluster smaller or raise your limits, then
click `[Create]` again.

### Settings

| Setting | Default | What it means |
|---|---|---|
| Location | where you stand | The corner where the cluster starts. Only the X and Z position count. |
| Direction | where you look | The direction in which the rows run. |
| Expand | right | Whether the columns grow to the right or to the left of the direction. |
| Size | `7` | Width of the floor of each plot, in blocks. At least 1. With the border, a plot is two blocks wider. |
| Rows | `2` | How many plots the cluster has in the direction. At least 1. |
| Columns | `2` | How many plots the cluster has to the side. At least 1. |
| Offset | `1` | Gap between two plots, in blocks. 0 or more. |
| Floor Material | `WHITE_CONCRETE` | Block for the floor of each plot. |
| Border Material | `RED_CONCRETE` | Block for the border around each plot. |
| Offset Material | `LIGHT_GRAY_CONCRETE` | Block for the gaps between the plots. |

The `[change]` buttons for numbers and blocks put a command into your chat bar. Add the value and press enter, for
example `/sbrg cluster modify size 15`. Block names are tab completed. Only blocks are accepted, not items.

A cluster is `(size + 2) × rows + offset × (rows - 1)` blocks long and
`(size + 2) × columns + offset × (columns - 1)` blocks wide. With the defaults that is 19 by 19 blocks.

:::tip Pick a size that fits your builds
The selection tool only saves what stands on the floor, inside the border. Make the floor a little larger than
your largest build.
:::

## Sessions

The builder keeps your settings in a **session** while you work on them.

- Each player has one session.
- `/sbrg cluster create` opens your session again with the settings you left it with. A new session starts at your
  position and direction.
- Clicking `[Create]` ends the session, once the cluster was created.
- Clicking `[x]` or running `/sbrg cluster close` throws the session away without creating anything.
- Sessions are only kept in memory. They are lost when the server restarts.

## Repair a cluster

Did someone break the floor or the border? Stand inside the cluster and run:

```text
/sbrg cluster repair
```

Grid Selector paints the cluster again, with its blocks and at its height, and says "Cluster redrawn." Like the first
paint job, this is a WorldEdit change in your session, so `//undo` reverts it. It counts against your WorldEdit
limits too: if the cluster is too large for them, you get "Grid exceeds maximum world edit size." and `//undo` removes
the part that was painted.

:::warning
Repairing paints the whole cluster layer again. Blocks of a build that sit in that layer are replaced.
:::

You can also repair a cluster by its ID from anywhere in the same world: `/sbrg cluster repair <id>`.

## Remove a cluster

Stand inside the cluster and run:

```text
/sbrg cluster remove
```

Or name the cluster by its ID: `/sbrg cluster remove <id>`. The ID only works in the world you are in.

Removing a cluster forgets it. The plots can no longer be selected. **The blocks stay in the world.** Clear the area
with WorldEdit if you want it gone.

## Cluster IDs

Each cluster gets a number when it is created. The numbers start at 1 in each world and go up.

You see the ID when you create the cluster. Later, `/sbrg cluster list` shows every cluster of your world with its ID,
corners, rows, columns and size:

```text
Clusters in this world
1: 74|-27 to 92|-9, 2 × 2 plots of size 7
```

`/sbrg cluster repair` and `/sbrg cluster remove` also tab complete the IDs of your world.

Most of the time you do not need it. Just stand inside the cluster and leave out the ID.

## WorldGuard regions

If WorldGuard is installed, Grid Selector can protect each new cluster with a WorldGuard region. Turn it on with
`createWorldGuardRegions: true` in the [configuration](./configuration.md#general). It is off by default.

When it is on:

- Creating a cluster also creates a cuboid WorldGuard region. It covers the whole cluster from the bottom to the top
  of the world.
- The player who created the cluster is the owner of the region.
- The region is named after the corner of the cluster with the lowest X and Z, for example `74,-27`.
- Removing the cluster also removes its region.

Grid Selector only creates the region. Set flags and add members with WorldGuard's own commands.

:::note
- Repairing a cluster does not create a region.
- The setting is checked at the time you create a cluster. Clusters created while it was off have no region.
- Removing a cluster removes its region even if you turned the setting off since.
:::
