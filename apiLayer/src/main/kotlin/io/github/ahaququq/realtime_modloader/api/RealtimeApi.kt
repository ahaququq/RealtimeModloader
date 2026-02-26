package io.github.ahaququq.realtime_modloader.api

import com.sun.org.slf4j.internal.Logger

interface RealtimeApi {
	companion object {
		var instance: RealtimeApi? = null
	}

	val logger: Logger
}