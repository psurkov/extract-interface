package com.jetbrains.internship.extractInterfaceProject

import com.jetbrains.internship.extractInterfaceProject.JavaVisibilityModifier.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class CLITest {

    @Test
    fun testReadCommandDefaults() {
        val extractInterfaceCommand = CLI().readCommand(arrayOf("input/file.java")) as ExtractInterfaceCommand
        Assertions.assertEquals(Paths.get("input", "file.java"), extractInterfaceCommand.inputFile)
        Assertions.assertNull(extractInterfaceCommand.className)
        Assertions.assertNull(extractInterfaceCommand.whitelist)
        Assertions.assertNull(extractInterfaceCommand.blacklist)
        Assertions.assertEquals(listOf(PUBLIC), extractInterfaceCommand.visibility)
        Assertions.assertEquals(null, extractInterfaceCommand.outputInterfaceName)
        Assertions.assertEquals(null, extractInterfaceCommand.outputFile)
    }

    @Test
    fun testReadCommand() {
        val extractInterfaceCommand = CLI().readCommand(
            arrayOf(
                "input/file.java",
                "-c", "name",
                "-w", "f1",
                "-w", "f2",
                "--blacklist", "f3",
                "--blacklist", "f4",
                "--blacklist", "f5",
                "-v", "private",
                "-v", "internal",
                "-i", "interfaceName",
                "-o", "output/file.java"
            )
        ) as ExtractInterfaceCommand
        Assertions.assertEquals(Paths.get("input", "file.java"), extractInterfaceCommand.inputFile)
        Assertions.assertEquals("name", extractInterfaceCommand.className)
        Assertions.assertEquals(setOf("f1", "f2"), extractInterfaceCommand.whitelist)
        Assertions.assertEquals(setOf("f3", "f4", "f5"), extractInterfaceCommand.blacklist)
        Assertions.assertEquals(listOf(PRIVATE, INTERNAL), extractInterfaceCommand.visibility)
        Assertions.assertEquals("interfaceName", extractInterfaceCommand.outputInterfaceName)
        Assertions.assertEquals(Paths.get("output", "file.java"), extractInterfaceCommand.outputFile)
    }
}