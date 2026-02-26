package io.github.ahaququq.realtime_modloader.core

class Mod2 {
	enum class State {
		WAITING,
		LOADING,
		LOADING_ERROR,
		LOADED,
		RUNTIME_ERROR,
		PERFORMANCE_ERROR
	}

	var state = State.WAITING
	val
}