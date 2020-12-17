val koinVersion = "2.1.5"

repositories {
	jcenter()
}

dependencies {
	implementation(group = "org.koin", name = "koin-core", version = koinVersion)
	implementation(group = "org.koin", name = "koin-logger-slf4j", version = koinVersion)
}