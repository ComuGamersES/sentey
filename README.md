# Sentey [![GitHub Actions](https://github.com/ComuGamersES/sentey/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/ComuGamersES/sentey/actions/workflows/maven.yml) ![Jenkins CI](https://ci.pabszito.ml/job/sentey/badge/icon?subject=Jenkins%20CI) [![CodeFactor](https://www.codefactor.io/repository/github/comugamerses/sentey/badge/master)](https://www.codefactor.io/repository/github/comugamerses/sentey/overview/master)
Protect your Spigot server by blocking unknown BungeeCord and Velocity proxies and checking for invalid IP forwarding 
addresses.

## Downloading
You can download the latest release from [GitHub](https://github.com/ComuGamersES/sentey/releases). Additionally, you
can also download the latest build from our [Jenkins CI](https://ci.pabszito.ml/job/sentey). Instructions on how to
install the plugin can be seen at the [wiki](https://github.com/ComuGamersES/sentey/wiki).

## Building from source
Building from source requires:
- Java 8 or later
- Apache Maven
- An internet connection

To get started, clone the repository:
```shell
git clone https://github.com/ComuGamersES/sentey.git sentey
cd sentey
```

Then, build from source:
```shell
mvn clean package
```

If the build succeeds, you will find two different JAR files at the `target/` folder. Choose the one without `original`
in the file name, and you're ready to go.

## Wiki
We have a wiki now to keep things more organized! Check it out [here](https://github.com/ComuGamersES/sentey/wiki).

### Developer API
Want to use the plugin's API and write your own custom actions? All the information about it can be found at the wiki 
as well. Click [here](https://github.com/ComuGamersES/sentey/wiki/4.-Developer-API) to take a look!
