package io.github.ahaququq.realtime_modloader.api

@Target(AnnotationTarget.CLASS)
annotation class Entrypoint(val env: Environment) {
	enum class Environment(val client: Boolean, val server: Boolean) {
		CLIENT(true,  false),
		SERVER(false, true),
		BOTH  (true,  true);

		fun canRun(on: Environment): Boolean {
			return on.client == client || on.server == server
		}
	}
}
