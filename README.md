<div align="center">

<img width="100%" src="assets/readme-hero.svg" alt="Leviathan Server Tools">

<br>

<img src="https://img.shields.io/badge/status-building%20out-06131d?style=flat-square" alt="Building out">
<img src="https://img.shields.io/badge/scope-public%20server%20tools-06131d?style=flat-square" alt="Public server tools">
<img src="https://img.shields.io/badge/security-sensitive%20ops%20excluded-06131d?style=flat-square" alt="Sensitive operations excluded">

**Public Minecraft server utilities, diagnostics, integrations and developer-facing server tooling.**

[Guide](GUIDE.md) · [Integrations](https://github.com/Lapinite/Leviathan-Integrations) · [Examples](https://github.com/Lapinite/Leviathan-Examples) · [Docs](https://github.com/Lapinite/Leviathan-Docs) · [Security](SECURITY.md)

</div>

## Tooling areas

<table width="100%">
<tr>
<td width="33%" valign="top"><strong>Operations</strong><br><sub>Administration helpers · diagnostics · configuration · compatibility utilities</sub></td>
<td width="33%" valign="top"><strong>Moderation</strong><br><sub>Staff tooling · moderation helpers · public plugin interfaces</sub></td>
<td width="33%" valign="top"><strong>Integration</strong><br><sub>Event bridges · supported platform integrations · server-side developer examples</sub></td>
</tr>
</table>

## Scope

Public server tooling may include:

- server administration utilities
- moderation and staff tooling
- integration components
- configuration helpers
- diagnostics and compatibility utilities
- public plugin interfaces
- server-side developer examples

Security-sensitive internal tooling and private operational systems are intentionally outside the scope of this public repository.

## Tool lifecycle

```text
Idea / requirement
      ↓
Public-safe design
      ↓
Compatibility validation
      ↓
Security review
      ↓
Documented public tool
      ↓
Versioned support
```

A tool should not be represented as supported until its installation, compatibility, security expectations, failure behavior, and support boundaries are documented.

## Compatibility

Supported server software and Minecraft versions will be documented per tool as implementations are released and tested. Compatibility claims should identify the exact environments that have been validated.

## Security boundaries

Public code and documentation must not contain server credentials, remote-console passwords, database credentials, private keys, webhook credentials, bot tokens, private infrastructure addresses, internal administrative endpoints, or personal information.

Configuration examples should use placeholders or environment-variable names.

## Nimbus relationship

Nimbus is part of the broader Leviathan security ecosystem. Public Server Tools may document supported interoperability or public-facing server workflows where appropriate, but proprietary anti-cheat implementation details and security-sensitive detection logic remain private.

## Related repositories

| Repository | Role |
| --- | --- |
| [Leviathan Integrations](https://github.com/Lapinite/Leviathan-Integrations) | Supported integration patterns |
| [Leviathan Examples](https://github.com/Lapinite/Leviathan-Examples) | Public implementation examples |
| [Leviathan Docs](https://github.com/Lapinite/Leviathan-Docs) | Ecosystem documentation |
| [Leviathan Status](https://github.com/Lapinite/Leviathan-Status) | Public service and incident information |
| [Leviathan API Docs](https://github.com/Lapinite/Leviathan-API-Docs) | Public API contracts |

## Project status

This repository is being built out as public server tooling is prepared, validated and documented. Development-stage items are not automatically public releases.
