---
sidebar_position: 5
---

# Commands and permissions

## Commands

The base command is `/schematicbrushgrid`. The short form is `/sbrg`.
All commands can only be used by players, not from the console.

| Command | Permission | What it does |
|---|---|---|
| `/sbrg select` | `gridselector.select` | Bind the [selection tool](./selecting.md#the-selection-tool) to the item in your main hand. |
| `/sbrg export <name> [-g] [-f]` | `gridselector.export` | [Export](./selecting.md#export) your newest saved selection as schematic files. |
| `/sbrg cluster create` | `gridselector.cluster.create` | Open the cluster builder. |
| `/sbrg cluster repair [id]` | `gridselector.cluster.repair` | Paint a cluster again. |
| `/sbrg cluster remove [id]` | `gridselector.cluster.remove` | Forget a cluster. |

`/sbrg` and `/sbrg cluster` on their own open the cluster builder, like `/sbrg cluster create`.

:::note
The base command itself needs `gridselector.use`. Give it to everyone who should run any `/sbrg` command.
:::

### export

```text
/sbrg export <name> [-g] [-f]
```

| Argument | Required | What it means |
|---|---|---|
| `name` | yes | Start of the file names. The files are called `<name>_0.schem`, `<name>_1.schem` and so on. |
| `-g` | no | Export into the shared schematic folder for everyone. Needs `gridselector.export.global`. |
| `-f` | no | Overwrite files that already exist. |

See [Export](./selecting.md#export) for where the files go.

### Cluster commands

| Command | What it does |
|---|---|
| `/sbrg cluster create` | Open your [cluster builder session](./grid-clusters.md#sessions), or start a new one. |
| `/sbrg cluster repair` | Paint the cluster you stand in again. |
| `/sbrg cluster repair <id>` | Paint the cluster with this ID in your world again. |
| `/sbrg cluster remove` | Remove the cluster you stand in. The blocks stay. |
| `/sbrg cluster remove <id>` | Remove the cluster with this ID in your world. The blocks stay. |

See [Cluster IDs](./grid-clusters.md#cluster-ids) for how to find an ID.

### Commands the builder uses

The buttons of the cluster builder run these commands. They are hidden from tab completion, but you can type them.
They all need `gridselector.cluster.create`. The `modify` commands change your session and show the builder again.
If you have no session yet, they start one.

| Command | What it does |
|---|---|
| `/sbrg cluster draw` | Create and paint the cluster. This is the `[Create]` button. |
| `/sbrg cluster close` | Throw your session away. This is the `[x]` button. |
| `/sbrg cluster modify center` | Move the start of the cluster to where you stand. |
| `/sbrg cluster modify direction` | Let the rows run in the direction you look. |
| `/sbrg cluster modify expandRight` | Switch between columns to the right and to the left. |
| `/sbrg cluster modify size <amount>` | Width of the floor of each plot. At least 1. |
| `/sbrg cluster modify rows <amount>` | Number of plots in the direction. At least 1. |
| `/sbrg cluster modify columns <amount>` | Number of plots to the side. At least 1. |
| `/sbrg cluster modify offset <amount>` | Gap between plots. 0 or more. |
| `/sbrg cluster modify floorMaterial <material>` | Block for the floor of each plot. |
| `/sbrg cluster modify borderMaterial <material>` | Block for the border around each plot. |
| `/sbrg cluster modify offsetMaterial <material>` | Block for the gaps between plots. |

The settings are explained on [Grid clusters](./grid-clusters.md#settings).

## Permissions

| Permission | Default | What it allows |
|---|---|---|
| `gridselector.use` | operators | Using the `/sbrg` command at all. |
| `gridselector.select` | operators | `/sbrg select`, and the Grid selector in brushes. |
| `gridselector.export` | nobody | `/sbrg export`, into your own folder. |
| `gridselector.export.global` | nobody | `/sbrg export -g`, into the shared folder. Needs `gridselector.export` too. |
| `gridselector.cluster.create` | nobody | Laying out new clusters with the builder. |
| `gridselector.cluster.repair` | nobody | `/sbrg cluster repair` |
| `gridselector.cluster.remove` | nobody | `/sbrg cluster remove` |
| `gridselector.updates` | operators | Hearing about new versions on join and downloading them. |

"Nobody" means that operators do not have the permission either. Grant it with your permission plugin.

Grid Selector also relies on Schematic Brush Reborn's permissions. Using the selection tool and the brushes needs
`schematicbrush.brush.use`. See the [permission reference](/schematic-brush-reborn/reference/permissions).

A good start for builders is `gridselector.use`, `gridselector.select` and `gridselector.export`, together with the
builder permissions of Schematic Brush Reborn.
