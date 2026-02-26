package io.github.ahaququq.realtime_modloader.api

import java.util.*

class Identifier {
	val namespace: String
	val path: Array<out String>

	constructor() {
		namespace = "default"
		path = arrayOf()
	}

	constructor(vararg path: String) {
		namespace = "default"
		this.path = path
	}

	constructor(namespace: String, vararg path: String) {
		this.namespace = namespace
		this.path = path
	}

	override fun equals(other: Any?) =
		other is Identifier
		&& other.namespace == namespace
		&& other.path.size == path.size
		&& (other.path zip path).all { (a, b) -> a == b }

	override fun hashCode() = Objects.hash(namespace, path.contentHashCode())

	override fun toString(): String {
		return buildString {
			append(namespace)
			append(":")
			if (path.isNotEmpty()) append(path[0])
			if (path.size >= 2) path.drop(1).forEach { append("/$it") }
		}
	}
}