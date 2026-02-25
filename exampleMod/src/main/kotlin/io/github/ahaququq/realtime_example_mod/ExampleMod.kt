package io.github.ahaququq.realtime_example_mod

import io.github.ahaququq.realtime_modloader.api.Entrypoint
import io.github.ahaququq.realtime_modloader.api.RealtimeMod

@Entrypoint(Entrypoint.Environment.BOTH)
class ExampleMod: RealtimeMod {
	override fun load() {
		println("Example mod loaded!")
	}
}