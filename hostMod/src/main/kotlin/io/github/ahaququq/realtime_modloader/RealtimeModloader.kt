package io.github.ahaququq.realtime_modloader

import io.github.ahaququq.realtime_modloader.core.ModloadingSystem
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RealtimeModloader : ModInitializer {
	companion object {
		const val MOD_ID: String = "realtime-modloader"
		val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	}

	override fun onInitialize() {
		println("HELLO FROM REALTIME MOD LOADER")
		ModloadingSystem.load()
		println("GOODBYE FROM REALTIME MOD LOADER")
	}
}
