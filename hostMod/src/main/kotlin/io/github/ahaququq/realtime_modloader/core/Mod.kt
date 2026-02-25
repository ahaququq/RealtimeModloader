package io.github.ahaququq.realtime_modloader.core

import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.File
import java.net.URLClassLoader

class Mod {
	companion object {
		const val METADATA_PATH = "realtime.mod.json"
	}

	val jarFile: File

	var loaded = false
	var status = ""

	var classLoader: URLClassLoader? = null
	var metadataJson: JsonElement? = null
	var entrypoints = mutableListOf<Entrypoint>()

	data class Entrypoint(val className: String)

	constructor(file: File) {
		jarFile = file
		if (!jarFile.isFile) throw ModloadingException("Path ${jarFile.path} does not name a file")
		if (jarFile.extension != "jar" && jarFile.extension != "rtm") {
			status = "Not a mod - Wrong file extension"
			return
		}

		classLoader = URLClassLoader(
			arrayOf(jarFile.toURI().toURL()),
			javaClass.classLoader
		)
		classLoader ?: throw ModloadingException("Class loader is (somehow) null")

		val metadataUrl = classLoader!!.findResource(METADATA_PATH)

		if (metadataUrl == null) {
			status = "Missing metadata (realtime.mod.json)"
			return
		}

		val metadataFile = File(metadataUrl.toURI())

		if (!metadataFile.canRead()) {
			status = "Can't read metadata"
			return
		}

		var metadataString = """{}"""

		try {
			val stream = metadataFile.inputStream()
			metadataString = String(stream.readAllBytes())
			stream.close()
		} catch (e: IOException) {
			status = "Failed to read metadata: \n${e.stackTraceToString()}"
			return
		}

		try {
			metadataJson = Json.parseToJsonElement(metadataString)
		} catch (e: SerializationException) {
			status = "Failed to load metadata: \n${e.stackTraceToString()}"
			return
		}


	}
}