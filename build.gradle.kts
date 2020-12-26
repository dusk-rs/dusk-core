import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

val ossrhUsername: String? by ext
val ossrhPassword: String? by ext

description = "#1 rsps. no noobs allowed"

plugins {
	base
	id("java")
	id()
	id("com.github.ben-manes.versions") version Versions.versionsPlugin
	
	kotlin("multiplatform") version Versions.kotlin apply false
	id("kotlinx.benchmark") version Versions.kotlinBenchmark apply false
	id("org.jetbrains.dokka") version Versions.dokka apply false
	id("org.jetbrains.kotlin.plugin.allopen") version Versions.kotlin apply false
}

tasks.withType<DependencyUpdatesTask> {
	rejectVersionIf {
		listOf("alpha", "beta", "rc", "cr", "m", "eap", "pr", "dev").any {
			candidate.version.contains(it, ignoreCase = true)
		}
	}
}

allprojects {
	repositories {
		mavenCentral()
		jcenter()
		maven("https://dl.bintray.com/kotlin/kotlinx")
	}
}

subprojects {
	plugins.withType<MavenPublishPlugin> {
		apply(plugin = "org.gradle.signing")
		
		plugins.withType<KotlinMultiplatformPluginWrapper> {
			apply(plugin = "org.jetbrains.dokka")
			
			val dokka by tasks.existing(DokkaTask::class) {
				outputFormat = "javadoc"
				outputDirectory = "$buildDir/docs/javadoc"
			}
			
			val javadocJar by tasks.registering(Jar::class) {
				group = LifecycleBasePlugin.BUILD_GROUP
				description = "Assembles a jar archive containing the Javadoc API documentation."
				archiveClassifier.set("javadoc")
				dependsOn(dokka)
				from(dokka.get().outputDirectory)
			}
			
			configure<KotlinMultiplatformExtension> {
				explicitApi()
				
				jvm {
					mavenPublication {
						artifact(javadocJar.get())
					}
				}
				
				js {
					browser()
					nodejs()
				}
			}
		}
		
		configure<PublishingExtension> {
			repositories {
				maven {
					if (project.version.toString().endsWith("SNAPSHOT")) {
						setUrl("https://oss.sonatype.org/content/repositories/snapshots")
					} else {
						setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
					}
					
					credentials {
						username = ossrhUsername
						password = ossrhPassword
					}
				}
			}
			
			publications.withType<MavenPublication> {
				pom {
					name.set(project.name)
					url.set("https://github.com/dusk-rs/dusk-core")
					inceptionYear.set("2020)
					
					developers {
						developer {
							name.set("Tyluur")
							url.set("dusk.rs/tyluur")
						}
					}
					
					scm {
						connection.set("scm:git:https://github.com/dusk-rs/dusk-core")
						developerConnection.set("scm:git:git@github.com:dusk-rs/dusk-core.git")
						url.set("https://github.com/dusk-rs/dusk-core")
					}
					
					issueManagement {
						system.set("GitHub")
						url.set("https://github.com/dusk-rs/dusk-core/issues")
					}
					
					ciManagement {
						system.set("GitHub")
						url.set("https://github.com/dusk-rs/dusk-core/actions?query=workflow%3Aci")
					}
				}
			}
			
			configure<SigningExtension> {
				useGpgCmd()
				sign(publications)
			}
		}
	}
}