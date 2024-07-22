# About

This module provides functions to read (and soon also write) Battle for Middle-Earth 2 and Battle for Middle-Earth 2 Rise of the Witch King map files.
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

The `MapFileWriter` implementation supports writing uncompressed maps (`CkMp`) and zlib compressed maps (`ZL5`).

Here's how you can modify the max camera height of an existing map:

```kotlin
import de.darkatra.bfme2.map.serialization.MapFileReader
import de.darkatra.bfme2.map.serialization.MapFileWriter
import java.nio.file.Path

val mapFilePath: Path = Path.of("/path/to/the/map/file.map")

val mapFile: MapFile = MapFileReader().read(mapFilePath)

// set the max camera height to 700
val editedMap = mapFile.copy(
  worldInfo = mapFile.worldInfo.copy(
    properties = mapFile.worldInfo.properties.filter { property -> property.key.name != "cameraMaxHeight" }
      + mapFile.worldInfo["cameraMaxHeight"]!!.copy(value = 700f)
  )
)

println(editedMap.worldSettings["cameraMaxHeight"])

// results in:
// Property(key=PropertyKey(propertyType=FLOAT, name=cameraMaxHeight), value=700.0)

val editedMapFilePath: Path = Path.of("/path/to/the/map/edited.map")

MapFileWriter().write(editedMapFilePath, editedMap, MapFileCompression.ZLIB)
```
