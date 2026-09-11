# Server tooling guide

[Server tools overview](README.md)

**Status: public tooling documentation is being prepared.** This repository does not yet contain a released utility or plugin.

## Server utilities

Public utilities may assist installation, configuration validation, administration, and diagnostics. Each released utility needs documented inputs, effects, permissions, backups where relevant, and removal steps. Do not publish private administrative tooling.

## Plugins

No plugin artifact is currently provided here. A release must identify its supported server software, Minecraft versions, Java/runtime version, dependencies, commands, and permissions before administrators install it.

## Moderation and staff tooling

Public guidance should explain supported staff actions, required permissions, and how to report problems. Use fictional placeholders in demonstrations. Do not include player identifiers, staff account details, enforcement evidence, or internal detection rules.

## Compatibility

| Item | Current public specification |
| --- | --- |
| Server software | No tested support matrix published |
| Minecraft versions | Not specified per tool yet |
| Java/runtime | Must be specified by each released tool |
| Plugin dependencies | No released plugin dependency list |
| Nimbus interoperability | Public compatibility documentation planned |

Do not infer support for a server fork or proxy from general Minecraft compatibility.

## Nimbus relationship

Nimbus is the server protection area of the Leviathan ecosystem. Public documentation may cover setup, supported versions, permissions, staff workflows, and safe troubleshooting. Detection internals, thresholds, bypass techniques, and unpublished security research remain private.

## Installation and support

Wait for a released artifact and its matching installation instructions. Back up server data before applying changes. Public issue reports should include only product versions, affected behavior, and sanitized errors. Never include remote-console credentials, private addresses, player data, or raw server logs.

## Licensing

This repository currently has no license file. Do not infer an open-source license from visibility or from the SDK's license. Future tool releases must state their licensing explicitly.

See [SECURITY.md](SECURITY.md).
