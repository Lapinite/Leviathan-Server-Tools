# Leviathan Client Rules

**Status: in development, not released.** Built and tested by CI. It has not been tried on a live server yet.

A small Spigot/Paper plugin that turns **Leviathan Client** features **off** for players on your server, by world. For example: no zoom and no coordinates in your PvP arena.

## What players experience
- **Only the Leviathan Client is affected.** Players without it are never messaged.
- **The client only turns things off, never on.** It never saves the rules, and clears them when the player leaves your server.
- **Players are told:** a notice says "This server turned off: Zoom, Coordinates", and the client's Modules screen marks those modules.
- **Nothing is read from players** besides the client's protocol version on `leviathan:hello`.

## Requirements
| | |
|---|---|
| Server | Spigot or Paper **1.21.1** (built against `spigot-api 1.21.1-R0.1-SNAPSHOT`) |
| Java | 21 |
| Client | Leviathan Client with server rules (protocol 1) |
| Dependencies | none |

## Install
1. Put `Leviathan-Client-Rules-<version>.jar` in `plugins/` and start the server once.
2. Edit `plugins/LeviathanClientRules/config.yml`.
3. Run `/leviathanrules reload`.

## Config
```yaml
# Everywhere, unless a world has its own list.
default: []

# A world's own list replaces the default list there.
worlds:
  pvp_arena:
    - ZOOM
    - HUD:COORDINATES
```

- **Modules:** `HUD_OVERLAY` (the whole HUD), `FPS_COUNTER`, `PING_DISPLAY`, `ZOOM`, `TOGGLE_SPRINT`, `TOGGLE_SNEAK`.
- **HUD widgets:** `HUD:FPS`, `HUD:PING`, `HUD:CPS`, `HUD:COORDINATES`, `HUD:CLOCK`, `HUD:KEYSTROKES`, `HUD:ARMOR_STATUS`, `HUD:POTIONS`, `HUD:SPRINT_STATE`, `HUD:SESSION_TIME`.
- **Unknown names** are ignored with a console warning. At most 64 per list.

Rules are sent when a player's client registers the channel, and again when they change world. `/leviathanrules reload` resends them to everyone.

## Commands and permissions
| Command | Permission | Default |
|---|---|---|
| `/leviathanrules show` (alias `/lcrules`): current rules, and how many players online have the client | `leviathan.clientrules.admin` | op |
| `/leviathanrules reload`: reload `config.yml` and resend the rules | `leviathan.clientrules.admin` | op |

When the plugin is disabled, it lifts the rules for players still online.

## Protocol
| Channel | Direction | Payload |
|---|---|---|
| `leviathan:hello` | client → server | VarInt protocol version |
| `leviathan:rules` | server → client | VarInt byte length + UTF-8 JSON `{"v":1,"disable":[…]}`, at most 4096 bytes |

The full specification is in the Leviathan Client's `docs/server-rules.md`.

## Build
```
cd plugins/client-rules
mvn clean package
```
