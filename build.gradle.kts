import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
import org.gradle.api.publish.maven.MavenPom
import java.util.*

plugins {
    kotlin("jvm") version "1.3.61"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    maven
    idea
    java
}

group = "redrune-network-core"
version = "0.0.1"

val bintrayUser: String? by project
val bintrayKey: String? by project
val versionName = version.toString()

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://repo.maven.apache.org/maven2")
    maven(url = "https://dl.bintray.com/michaelbull/maven")
}

dependencies {
    //Main
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("io.netty:netty-all:4.1.44.Final")
    implementation(group = "org.yaml", name = "snakeyaml", version = "1.8")
    implementation(group = "io.github.classgraph", name = "classgraph", version = "4.6.3")
    implementation(group = "com.michael-bull.kotlin-inline-logger", name = "kotlin-inline-logger-jvm", version = "1.0.2")

    //Logging
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    //Utilities
    implementation("com.google.guava:guava:19.0")
    implementation("org.apache.commons:commons-lang3:3.0")

    //Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
}

artifacts {
    archives(sourcesJar)
}

fun MavenPom.addDependencies() = withXml {
    asNode().appendNode("dependencies").let { depNode ->
        configurations.compile.get().allDependencies.forEach {
            depNode.appendNode("dependency").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}


publishing {
    publications {
        create("production", MavenPublication::class) {
            artifact("$buildDir/outputs/aar/app-release.aar")
            groupId
            artifactId = "redrune-network-core"
            version = versionName
            pom.addDependencies()
        }
    }
}

bintray {
    user = bintrayUser
    key = bintrayKey
    setConfigurations("archives")
    publish = true
    pkg(delegateClosureOf<PackageConfig> {
        repo = "redrune-rsps"
        name = "redrune-network-core"
        userOrg = "redrune"
        vcsUrl = "https://gitlab.com/redrune-rsps/core/network.git"
        setLabels("kotlin")
        setLicenses("BSD 3-Clause")
        publicDownloadNumbers = true
        version(delegateClosureOf<VersionConfig> {
            name = versionName
            desc = "The RedRune network is maintained at its core within this project."
            released = Date().toString()
            vcsTag = "v$versionName"
        })
    })
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

