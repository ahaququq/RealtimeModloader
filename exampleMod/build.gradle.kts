plugins {
	kotlin("jvm")
}

dependencies {
	compileOnly(project(":apiLayer"))
}

repositories {
	mavenCentral()
}

kotlin {
	jvmToolchain(8)
}