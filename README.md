[![Maven Package](https://github.com/DarkAtra/bfme2-modding-utils/workflows/Maven%20Package/badge.svg)](https://github.com/DarkAtra/bfme2-modding-utils/releases)

# About
This project provides functions to read and write some of the EA file formats, such as:
- [BIG](https://github.com/TheAssemblyArmada/Thyme/wiki/BIG-File-Format)
- [RefPack](http://wiki.niotso.org/RefPack#Bitstream_specification)
- Map

## Build
Clone the project:
```
git clone https://git.darkatra.de/DarkAtra/Patcher.git
```
Build the project using the following command:
```
mvn clean install
```
The jar files are located in the target folders of the respective applications. E.g.: `updater/target/updater.jar`

**Thanks to the [OpenSage Team](https://github.com/OpenSAGE/OpenSAGE) for providing a reference implementation for most of these formats.**
