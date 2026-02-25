package io.github.ahaququq.realtime_modloader.core

import io.github.ahaququq.realtime_modloader.RealtimeModloader.Companion.LOGGER
import java.io.File

object ModloadingSystem {
	val modFolders = arrayOf("mods", "realtime_mods")

	fun load() {
		modFolders.forEach { dir -> loadFromDirectory(File(dir)) }
	}

	fun loadFromDirectory(dir: File) {
		LOGGER.info("Loading mods from: " + dir.absolutePath)
		dir.listFiles { file -> file.extension == "jar" }.forEach {
			file -> LOGGER.info(" + ${file.absolutePath}")
		}
	}
}