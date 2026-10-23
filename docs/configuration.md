---
sidebar_position: 6
---

# Configuration

Grid Selector keeps its files in `plugins/GridSelector/`:

| File or folder | What it holds |
|---|---|
| `config.yml` | The settings |
| `clusterWorlds.yml` | Your grid clusters |
| `base_configuration.yml` | Bookkeeping and the log level |
| `messages/` | The name of the Grid selector, in English and German |
| `schematics/` | Saved selections. Emptied on every start and stop. |

The files are created with default values when they are needed first.

The add-on reads the files on startup. It writes the three `.yml` files again whenever a cluster is created or removed,
and when an admin answers the update question in chat. Changes you make to a file while the server runs
are then overwritten. Stop the server, edit the file, then start the server again.

## config.yml

This is the file with all defaults:

```yaml
updates:
  notice: true
  autoUpdate: false
  askForAutoUpdate: true
general:
  createWorldGuardRegions: false
  schematicPath: "SchematicBrushReborn/schematics"
  absolutePath: false
highlight:
  highlight: "SEA_LANTERN"
  highlightBorder: false
  highlightBounds: true
```

### general

| Key | Default | What it does |
|---|---|---|
| `createWorldGuardRegions` | `false` | Create a WorldGuard region for each new cluster. Needs WorldGuard. See [WorldGuard regions](./grid-clusters.md#worldguard-regions). |
| `schematicPath` | `SchematicBrushReborn/schematics` | The folder that [`/sbrg export`](./selecting.md#export) writes to. |
| `absolutePath` | `false` | `false`: `schematicPath` starts in the server's `plugins` folder. `true`: `schematicPath` is a full path anywhere on the machine. |

The default `schematicPath` is one of the folders Schematic Brush Reborn reads schematics from. If you point it
somewhere else, add that folder as a schematic source in Schematic Brush Reborn's
[configuration](/schematic-brush-reborn/configuration). Otherwise exported schematics cannot be selected.

### highlight

These keys control the [markers](./selecting.md#markers) a player sees on selected plots.

| Key | Default | What it does |
|---|---|---|
| `highlight` | `SEA_LANTERN` | The block used for the markers. Use the Bukkit name of a block in upper case. |
| `highlightBounds` | `true` | Mark the eight corners of the selected build. |
| `highlightBorder` | `false` | Mark a ring on top of the border of the selected plot. |

### updates

The add-on looks for new versions every six hours.

| Key | Default | What it does |
|---|---|---|
| `notice` | `true` | Tell the console and admins when a new version is out. |
| `autoUpdate` | `false` | Download new stable versions on its own. They take effect on the next start. A new major version is never downloaded on its own. |
| `askForAutoUpdate` | `true` | Ask admins in chat whether to switch `autoUpdate` on. |

When both `notice` and `autoUpdate` are `false`, the add-on does not look for new versions at all.

Admins with `gridselector.updates` see the notice when they join, see [Permissions](./commands.md#permissions).
Choosing "enable-auto" in the chat message sets `autoUpdate` to `true`. Choosing "never" sets `askForAutoUpdate` to
`false`.

Configs from version 1 had a `checkUpdates` switch. It is read once as the starting value of `notice` and then
dropped.

## clusterWorlds.yml

This file holds every [grid cluster](./grid-clusters.md), grouped by the UUID of its world. You do not need to edit
it. To look up the ID of a cluster, use [`/sbrg cluster list`](./grid-clusters.md#cluster-ids).

```yaml
clusterWorlds:
  "46be765d-9464-4335-9a08-d2377c5e187f":
    uid: "46be765d-9464-4335-9a08-d2377c5e187f"
    cluster:
    - id: 1
      minHeight: 63
      plot:
        min:
          x: 74
          z: -27
        max:
          x: 92
          z: -9
      elementSize: 7
      offset: 1
      rows: 2
      columns: 2
      borderMaterial: "RED_CONCRETE"
      offsetMaterial: "LIGHT_GRAY_CONCRETE"
      floorMaterial: "WHITE_CONCRETE"
    id: 2
```

| Key | What it means |
|---|---|
| `uid` | The UUID of the world |
| `cluster` | The clusters of the world |
| `id` (of the world) | The ID the next cluster in this world gets |
| `id` (of a cluster) | The ID of the cluster, used by `repair` and `remove` |
| `minHeight` | The height of the floor layer |
| `plot` | The corners of the whole cluster, X and Z |
| `elementSize` | The *Size* setting: width of the floor of each plot |
| `offset`, `rows`, `columns` | The settings of the same name |
| `borderMaterial`, `offsetMaterial`, `floorMaterial` | The blocks of the cluster |

The settings are explained on [Grid clusters](./grid-clusters.md#settings).

## base_configuration.yml

On a fresh install of 2.0.0 the file looks like this:

```yaml
version: 1
lastInstalledVersion: "2.0.0"
logLevel: "INFO"
```

| Key | Meaning |
|---|---|
| `version` | Version of the config layout. Do not change it. |
| `lastInstalledVersion` | The add-on version that created the file |
| `logLevel` | How much the add-on logs. `INFO` is normal. |

The log levels work as in Schematic Brush Reborn, see its
[configuration](/schematic-brush-reborn/configuration). When Grid Selector writes the file, `DEBUG` is stored
as `CONFIG`. Both mean the same.

## Messages

The `messages` folder holds `messages_en_US.properties` and `messages_de_DE.properties`. They only contain the name
and description of the Grid selector: **Grid** in English, **Gitter** in German. Which one is used follows the
`language` in Schematic Brush Reborn's [configuration](/schematic-brush-reborn/configuration). Restart the server
after you change a message file.

All other messages of Grid Selector are in English and cannot be changed.
