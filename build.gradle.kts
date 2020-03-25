plugins {
    kotlin("jvm") version "1.3.61"
    `maven-publish`
    maven
    idea
    java
}

group = "redrune-network-core"
version = "0.0.3"

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

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

