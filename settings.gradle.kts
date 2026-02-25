pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		gradlePluginPortal()
		mavenCentral()
	}
}
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
	kotlin("jvm") version "2.3.10" apply false
}

include(":hostMod")
include(":apiLayer")
include(":exampleMod")