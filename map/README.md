| ⚠                                                                                                                                                                        |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **This document is not final and does not represent the current state of this module. It rather describes what i envision this module to look like in version `1.0.0`.** |

# About

This module provides functions to read and write Battle for Middle-Earth 2 and Battle for Middle-Earth 2 Rise of the Witch King map files.
The motivation is to make it possible to change settings for multiple maps using small and readable scripts instead of doing it manually in EA's World Builder.

Please note, that this implementation is specifically designed to support BfME2 and RotWK map files. Map files of other Sage games are not officially supported.
If you need to read map files for other Sage games you can have a look at [OpenSage](https://github.com/OpenSAGE/OpenSAGE). Their implementation should cover
all Sage games. This module is heavily inspired by their phenomenal work and would've taken a lot longer without their reference implementation.

## Usage

### Reading a `MapFile`

The `MapFileReader` implementation auto-detects which compression is used by the map file. It currently supports reading uncompressed maps (`CkMp`), refpack
compressed maps (`EAR`) and zlib compressed maps (`ZL5`), just like the games themselves do.

To read a map from the file system use:

```kotlin
import de.darkatra.bfme2.map.serialization.MapFileReader
import java.nio.file.Path

val mapFilePath: Path = Path.of("/path/to/the/map/file.map")

val mapFile: MapFile = MapFileReader().read(mapFilePath)

println(mapFile.worldSettings["cameraMaxHeight"])

// results in:
// Property(key=PropertyKey(propertyType=FLOAT, name=cameraMaxHeight), value=800.0)
```

### Writing a `MapFile`

TBD
