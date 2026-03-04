package io.github.ahaququq.realtime_modloader.api

import io.github.ahaququq.realtime_modloader.api.Identifier.Companion.validId

/**
 * # Identifier Class
 * Identifiers consist of a namespace and a path.
 * Namespace is one ID part, while the path can be multiple separated by `/`.
 * An ID part can consist of lowercase letters, numbers, `_`, `-` and `.`.
 * All ID parts must start with a letter.
 * @property id Identifier itself, in the form **`namespace` `:` `part1`** `/` `part2` `/` `rest_of_path`
 * @since 1.0
 * @author Ahaququq
 */
class Identifier {
	companion object {
		private const val PART = "[a-z][a-z0-9\\-.]*"
		private infix fun String.matches(regex: String) = regex.toRegex().matchEntire(this) != null

		fun String.validId() = this matches "$PART:(?:$PART/?)*$PART"
		fun String.validPart() = this matches PART
		fun String.validPath() = this matches "(?:$PART/?)*$PART"

		fun String.requireValidID(): String {
			if (!validId()) throw IllegalArgumentException("Invalid ID: \n Must be a namespace and a path, separated by ':'")
			return this
		}

		fun String.requireValidPart(): String {
			if (!validPart()) throw IllegalArgumentException("Invalid ID part: \n Must be lowercase, start with a letter and can contain only letters, digits, '_', '-', and '.'")
			return this
		}

		fun String.requireValidPath(): String {
			if (!validPath()) throw IllegalArgumentException("Invalid path: \n Must be one or more ID parts separated by '/'")
			return this
		}
	}

	/**
	 * ID stored in the identifier
	 * @see validId
	 */
	val id: String

	/// Precomputed hash for quicker inequality
	private val hash: Int

	constructor(id: String) {
		this.id = id.requireValidID()
		this.hash = id.hashCode()
	}

	constructor(namespace: String, path: String) {
		this.id = "${namespace.requireValidPart()}:${path.requireValidPath()}"
		this.hash = id.hashCode()
	}

	constructor(namespace: String, vararg path: String) {
		this.id = "${namespace.requireValidPart()}:${path.joinToString("/") { it.requireValidPart() }}"
		this.hash = id.hashCode()
	}

	override fun equals(other: Any?) =
		other is Identifier
		&& other.hash == hash
		&& other.id == id

	override fun hashCode() = hash
	override fun toString() = id

	val namespace: String get() = id.split(":")[0]
	val path: String get() = id.split(":")[1]
}
