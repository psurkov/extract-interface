package com.jetbrains.internship.extractInterfaceProject

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class AbstractExtractInterfaceCommandTest {
    @TempDir
    lateinit var tempDir: Path

    private val inputPath = Paths.get("src/test/resources/testData/input")
    private val expectedPath = Paths.get("src/test/resources/testData/expected")

    protected fun testSample(
        inputSampleName: String,
        expectedSampleName: String,
        expectedCLICommandResult: CLI.CLICommandResult,
        className: String?,
        whitelist: List<String>?,
        blacklist: List<String>?,
        visibility: List<JavaVisibilityModifier>,
        outputInterfaceName: String?
    ) {
        val outputInterfacePath = tempDir.resolve("result.java")
        val cliResult = ExtractInterfaceCommand(
            inputPath.resolve("$inputSampleName.java"),
            className,
            whitelist,
            blacklist,
            visibility,
            outputInterfaceName,
            outputInterfacePath
        ).execute()
        Assertions.assertEquals(expectedCLICommandResult, cliResult)
        val actual = Files.readAllLines(outputInterfacePath)
        val expected = Files.readAllLines(expectedPath.resolve("$expectedSampleName.java"))
        Assertions.assertEquals(expected, actual)
    }
}
