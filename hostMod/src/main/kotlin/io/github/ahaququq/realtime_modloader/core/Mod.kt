package io.github.ahaququq.realtime_modloader.core

import io.github.ahaququq.realtime_modloader.RealtimeModloader.Companion.LOGGER
import io.github.ahaququq.realtime_modloader.api.Entrypoint
import io.github.ahaququq.realtime_modloader.api.RealtimeMod
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import java.io.File
import java.net.URLClassLoader

class Mod {
	companion object {
		const val METADATA_PATH = "realtime.mod.json"
	}

	val source: File
	val id: String

	var loaded = false
	var status = ""

	var classLoader: URLClassLoader? = null
	var metadataVersion: Int? = null

	var entrypoints = mutableListOf<EntryClass<*>>()

	class EntryClass<T: RealtimeMod>(val entryClass: Class<T>, val instance: T, val annotation: Entrypoint)

	fun load(environment: Entrypoint.Environment) {
		status = "Running"
		entrypoints.forEach { entryClass ->
			LOGGER.info("Running ${entryClass.entryClass.name}")
			if (!environment.canRun(entryClass.annotation.env)) {
				LOGGER.info("Can't run ${entryClass.entryClass.name} (${entryClass.annotation.env.name}) on ${environment.name}")
				return@forEach
			}
			try {
				entryClass.instance.load()
			} catch (e: Throwable) {
				LOGGER.error("Error while loading entrypoint ${entryClass.entryClass.name} from mod ${source.name}:")
				LOGGER.error(e.stackTraceToString())
			}
		}
		status = "Idle"
	}

	constructor(file: File) {
		source = file
		if (!source.isFile) throw ModloadingException("Path ${source.path} does not name a file")
		if (source.extension != "jar" && source.extension != "rtm") {
			status = "Not a mod - Wrong file extension"
			return
		}

		classLoader = URLClassLoader(
			arrayOf(source.toURI().toURL()),
			javaClass.classLoader
		)
		classLoader ?: throw ModloadingException("Class loader is (somehow) null")

		val metadataUrl = classLoader!!.findResource(METADATA_PATH)
		if (metadataUrl == null) {
			status = "Missing metadata (realtime.mod.json)"
			return
		}

		var metadataString = """{}"""
		try {
			metadataUrl.openStream().use { stream ->
				metadataString = String(stream.readAllBytes())
			}
		} catch (e: IOException) {
			status = "Failed to read metadata: \n${e.stackTraceToString()}"
			return
		}

		try {
			var metadataJson = Json.parseToJsonElement(metadataString).jsonObject
			metadataVersion = metadataJson["version"]?.jsonPrimitive?.intOrNull
			val entryList = metadataJson["entrypoints"]?.jsonArray
			entryList?.forEach { element ->
				val value = element.jsonPrimitive.contentOrNull ?: return@forEach
				val eClass = classLoader!!.loadClass(value)

				if (!RealtimeMod::class.java.isAssignableFrom(eClass)) {
					LOGGER.warn("Entrypoint for mod ${file.absolutePath} does not extend io.github.ahaququq.realtime_modloader.api.RealtimeMod")
					return@forEach
				}

				val clazz = eClass as? Class<RealtimeMod>

				if (clazz == null) {
					LOGGER.warn("Entrypoint for mod ${file.absolutePath} is missing annotation")
					return@forEach
				}

				val entrypointAnnotation = clazz.getAnnotation(Entrypoint::class.java)
				if (entrypointAnnotation == null) {
					LOGGER.warn("Entrypoint for mod ${file.absolutePath} is missing annotation")
					return@forEach
				}

				if (clazz.constructors.none { constructor -> constructor.parameterCount == 0 }) {
					LOGGER.warn("Entrypoint for mod ${file.absolutePath} does not define a zero argument constructor")
					return@forEach
				}

				val instance = clazz.getConstructor().newInstance()

				entrypoints.add(EntryClass(clazz, instance, entrypointAnnotation))
			}

		} catch (e: SerializationException) {
			status = "Failed to load metadata: \n${e.stackTraceToString()}"
			return
		} catch (e: IllegalArgumentException) {
			status = "Malformed metadata file: \n${e.stackTraceToString()}"
			return
		}

		loaded = true
		status = "Ready"
	}

	override fun toString(): String {
		return "${source.name}"
	}
}