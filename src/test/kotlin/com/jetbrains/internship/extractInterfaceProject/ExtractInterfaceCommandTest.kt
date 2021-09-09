package com.jetbrains.internship.extractInterfaceProject

import org.junit.jupiter.api.Test

internal class ExtractInterfaceCommandTest : AbstractExtractInterfaceCommandTest() {

    @Test
    fun testEmpty() {
        testSample(
            "Empty",
            "Empty",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }
}