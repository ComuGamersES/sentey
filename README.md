# Sentey [![GitHub Actions](https://github.com/ComuGamersES/sentey/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/ComuGamersES/sentey/actions/workflows/build.yml) [![CodeFactor](https://www.codefactor.io/repository/github/comugamerses/sentey/badge/master)](https://www.codefactor.io/repository/github/comugamerses/sentey/overview/master)
Protect your Spigot server by blocking unknown BungeeCord and Velocity proxies and checking for invalid IP forwarding 
addresses.

## Downloading
You can download the latest release from [GitHub](https://github.com/ComuGamersES/sentey/releases). Instructions on how to
install the plugin can be seen at the [wiki](https://github.com/ComuGamersES/sentey/wiki).

## Building from source
To build from source, you'll need:
- Git
- Java 8 or later
- Apache Maven
- An Internet connection

To get started, clone the repository:
```shell
$ git clone https://github.com/ComuGamersES/sentey.git sentey
$ cd sentey
```

Then, build from source:
```shell
$ mvn clean package
```

If you also want to publish the plugin to your local Maven repository (`~/.m2/`), consider using the following command instead:
```shell
$ mvn clean install
```

If the build succeeds, you will find two different JAR files at the `target/` folder. Choose the one without `original`
in the file name, and you're ready to go.

## Wiki
We have a wiki now to keep things more organized! Check it out [here](https://github.com/ComuGamersES/sentey/wiki).

### Developer API
Want to use the plugin's API and write your own custom actions? All the information about it can be found at the wiki 
as well. Click [here](https://github.com/ComuGamersES/sentey/wiki/4.-Developer-API) to take a look!

## Special Thanks
![YourKit](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of <a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>,
<a href="https://www.yourkit.com/.net/profiler/">YourKit .NET Profiler</a>,
and <a href="https://www.yourkit.com/youmonitor/">YourKit YouMonitor</a>.
