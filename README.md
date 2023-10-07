[![Maven Package](https://github.com/DarkAtra/bfme2-modding-utils/workflows/Maven%20Package/badge.svg)](https://github.com/DarkAtra/bfme2-modding-utils/releases)

# About

This project provides functions to read and write some of EA's file formats, such as:

- [BIG](https://github.com/TheAssemblyArmada/Thyme/wiki/BIG-File-Format)
- [RefPack](http://wiki.niotso.org/RefPack#Bitstream_specification)
- Map

## Build

Clone the project:

```
git clone git@github.com:DarkAtra/bfme2-modding-utils.git
```

Build the project using the following command:

```
mvn clean install
```

**Thanks to the [OpenSage Team](https://github.com/OpenSAGE/OpenSAGE) for providing a reference implementation for most of these formats.**

## Examples

* [Reading maps](./map/src/test/kotlin/de/darkatra/bfme2/map/serialization/MapFileReaderTest.kt)
* [Writing maps](./map/src/test/kotlin/de/darkatra/bfme2/map/serialization/MapFileWriterTest.kt)
* [Editing camera settings for all maps](https://github.com/DarkAtra/bfme2-patcher/blob/main/map-builder/src/main/kotlin/de/darkatra/patcher/mapbuilder/MapBuilderApplication.kt)
* [Extracting files from a big archive](https://github.com/DarkAtra/bfme2-patcher/blob/main/mod-builder/src/main/kotlin/de/darkatra/patcher/modbuilder/ModBaselineExtractorApplication.kt)
* [Creating new big archives](https://github.com/DarkAtra/bfme2-patcher/blob/main/mod-builder/src/main/kotlin/de/darkatra/patcher/modbuilder/ModBuilderApplication.kt)
