# Dusk Core
-- --

The dusk core is a multi application library. 

The network core can be used for any network server or client which wishes to use the netty network library as well as object oriented design for the network pipeline.

## Requirements

You must have gradle installed on your machine, as well as maven.

## Implementation

Gradle (kts)

```kotlin
val duskCoreVersion = "1.0.0"

repositories {
	mavenLocal()
}
	
implementation(group = "dusk.rs", name = "core", version = duskCoreVersion)
````

## Example

### Server

```kotlin
class TestServer(private val port : Int) : NetworkServer() {
	
	override fun listen() {
		configureDefault()
		start(port)
	}
		
}
```

### Client

```kotlin
class TestClient(host : String, private val port : Int) : NetworkClient(host) {

	override fun connect() {
		configureDefault()
		start(port)
	}

}
```

## Setup

In order to access this library on your machine, it must be built and published to your maven local repository.
In the terminal relevant to this project, run this command. 

```sh
gradle build
gradle public
gradle run
```

You should see output such as:

```sh
BUILD SUCCESSFUL in 5s
23 actionable tasks: 23 executed
````
