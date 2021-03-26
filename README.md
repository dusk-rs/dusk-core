# Dusk Shared Core

A highly functioning runescape private server core which provides additional network security, additional pipeline management, and more.

[![revision][rev-badge]][patch] [![license][license-badge]][isc] [![chat][discord-badge]][discord]

[isc]: https://opensource.org/licenses/isc
[license]: https://github.com/rsmod/rsmod/blob/master/LICENSE.md
[discord]: https://dusk.rs
[patch]: https://oldschool.runescape.wiki/w/Update:God_Wars_Instancing_and_Soul_Wars_Improvements
[rev-badge]: https://img.shields.io/badge/revision-667-important
[license-badge]: https://img.shields.io/badge/license-ISC-informational
[discord-badge]: https://img.shields.io/discord/238151952121331712?color=%237289da&logo=discord

---
All modules in Dusk that use internal/external network connections, excluding the client, are dependent upon this project.

# Setup
`gradle build publishToMavenLocal`