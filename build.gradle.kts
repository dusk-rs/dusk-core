buildscript {
	repositories {
		jcenter()
	}
	
	dependencies {
		classpath(kotlin("gradle-plugin", version = "1.3.71"))
	}
}

plugins {
	kotlin("jvm") version "1.3.71"
	`maven-publish`
	maven
	idea
	java
}

subprojects {
	apply(plugin = "kotlin")
	apply(plugin = "idea")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	
	
	group = "rs.dusk.core"
	version = "0.1.0"
	
	java.sourceCompatibility = JavaVersion.VERSION_11
	
	repositories {
		mavenLocal()
		mavenCentral()
		jcenter()
		maven(url = "https://repo.maven.apache.org/maven2")
		maven(url = "https://dl.bintray.com/michaelbull/maven")
	}
	
	dependencies {
		//Main
		implementation(kotlin("stdlib-jdk8"))
		implementation(kotlin("reflect"))
		implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.3.5")
		implementation(group = "io.netty", name = "netty-all", version = "4.1.44.Final")
		implementation(group = "org.yaml", name = "snakeyaml", version = "1.8")
		implementation(group = "io.github.classgraph", name = "classgraph", version = "4.6.3")
		implementation(
			group = "com.michael-bull.kotlin-inline-logger",
			name = "kotlin-inline-logger-jvm",
			version = "1.0.2"
		)
		
		//Logging
		implementation("org.slf4j:slf4j-api:1.7.30")
		implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
		
		//Utilities
		implementation(group = "com.google.guava", name = "guava", version = "19.0")
		implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.0")
		
		//Testing
		testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.5.2")
		
	}
	
	tasks {
		compileKotlin {
			kotlinOptions.jvmTarget = "1.8"
		}
		compileTestKotlin {
			kotlinOptions.jvmTarget = "1.8"
		}
	}
	
}

subprojects {
	apply(plugin="maven-publish")
	val sourcesJar by tasks.registering(Jar::class) {
		archiveClassifier.set("sources")
		from(sourceSets.getByName("main").java.srcDirs)
	}
	
	artifacts {
		archives(sourcesJar.get())
	}
	
	publishing {
		repositories {
			mavenLocal()
		}
		publications {
			register("mavenJava", MavenPublication::class) {
				from(components["java"])
				artifact(sourcesJar.get())
			}
		}
	}
}