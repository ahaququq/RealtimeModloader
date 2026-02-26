package io.github.ahaququq.realtime_modloader.core

import io.github.ahaququq.realtime_modloader.RealtimeModloader.Companion.LOGGER
import io.github.ahaququq.realtime_modloader.api.Entrypoint
import java.io.File

object ModloadingSystem {
	val modFolders = arrayOf("mods", "realtime_mods")
	val mods = mutableListOf<Mod>()

	fun load() {
		modFolders.forEach { dir -> loadFromDirectory(File(dir)) }
		LOGGER.info("Mod status:")
		mods.forEach { mod ->
			LOGGER.info(" ${if(mod.loaded) "+" else "-"} [$mod]: ${mod.status}")
		}
		mods.forEach { mod ->
			mod.load(Entrypoint.Environment.BOTH)
		}
		LOGGER.info("Mod status:")
		mods.forEach { mod ->
			LOGGER.info(" ${if(mod.loaded) "+" else "-"} [$mod]: ${mod.status}")
		}
	}

	fun loadFromDirectory(dir: File) {
		LOGGER.info("Loading mods from: " + dir.absolutePath)
		dir.listFiles { file -> file.extension == "jar" }.forEach { file ->
			LOGGER.info(" + ${file.absolutePath}")
			mods.add(Mod(file))
		}
	}
}