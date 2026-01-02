[![Build & Release](https://github.com/DarkAtra/bfme2-modding-utils/actions/workflows/build.yml/badge.svg)](https://github.com/DarkAtra/bfme2-modding-utils/actions/workflows/build.yml)

# bfme2-modding-utils

## About

This project provides functions to read and write some of EA's file formats, such as:

- [BIG](https://github.com/TheAssemblyArmada/Thyme/wiki/BIG-File-Format)
- [RefPack](http://wiki.niotso.org/RefPack#Bitstream_specification)
- Map

It also comes with support for GraalVM Native Image as of version `1.1.1`.

## Usage

Add the following dependencies to your `pom.xml` according to your needs:

[//]: # (@formatter:off)
```xml
<dependency>
  <groupId>de.darkatra.bfme2</groupId>
  <artifactId>big</artifactId>
  <version>1.1.1</version> <!-- please check if this is the current version -->
</dependency>
```

```xml
<dependency>
  <groupId>de.darkatra.bfme2</groupId>
  <artifactId>core</artifactId>
  <version>1.1.1</version> <!-- please check if this is the current version -->
</dependency>
```

```xml
<dependency>
  <groupId>de.darkatra.bfme2</groupId>
  <artifactId>map</artifactId>
  <version>1.1.1</version> <!-- please check if this is the current version -->
</dependency>
```

```xml
<dependency>
  <groupId>de.darkatra.bfme2</groupId>
  <artifactId>refpack</artifactId>
  <version>1.1.1</version> <!-- please check if this is the current version -->
</dependency>
```
[//]: # (@formatter:on)

## Build

Clone the project:

[//]: # (@formatter:off)
```bash
git clone git@github.com:DarkAtra/bfme2-modding-utils.git
```
[//]: # (@formatter:on)

Build the project using the following command:

[//]: # (@formatter:off)
```bash
./mvnw clean install
```
[//]: # (@formatter:on)

**Thanks to the [OpenSage Team](https://github.com/OpenSAGE/OpenSAGE) for providing a reference implementation for most of these formats.**

## Examples

* [Reading maps](./map/src/test/kotlin/de/darkatra/bfme2/map/serialization/MapFileReaderTest.kt)
* [Writing maps](./map/src/test/kotlin/de/darkatra/bfme2/map/serialization/MapFileWriterTest.kt)
* [Editing camera settings for all maps](https://github.com/DarkAtra/bfme2-patcher/blob/main/map-builder/src/main/kotlin/de/darkatra/patcher/mapbuilder/MapBuilderApplication.kt)
* [Extracting files from a big archive](https://github.com/DarkAtra/bfme2-patcher/blob/main/mod-builder/src/main/kotlin/de/darkatra/patcher/modbuilder/ModBaselineExtractorApplication.kt)
* [Creating new big archives](https://github.com/DarkAtra/bfme2-patcher/blob/main/mod-builder/src/main/kotlin/de/darkatra/patcher/modbuilder/ModBuilderApplication.kt)
