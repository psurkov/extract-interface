package com.jetbrains.internship.extractInterfaceProject

import com.jetbrains.internship.extractInterfaceProject.JavaVisibilityModifier.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class ExtractInterfaceCommandTest {

    @TempDir
    lateinit var tempDir: Path

    private val inputPath = Paths.get("src/test/resources/testData/input")
    private val expectedPath = Paths.get("src/test/resources/testData/expected")

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

    @Test
    fun testEmptyCustomName() {
        testSample(
            "Empty",
            "EmptyCustomName",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            "EmptyCustomName",
        )
    }

    @Test
    fun testJustFields() {
        testSample(
            "JustFields",
            "JustFields",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testOnlyPrivateVisibility() {
        testSample(
            "Visibility",
            "VisibilityOnlyPrivate",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            listOf(PRIVATE),
            null,
        )
    }

    @Test
    fun testPublicAndInternalVisibility() {
        testSample(
            "Visibility",
            "VisibilityPublicAndInternal",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            listOf(PUBLIC, INTERNAL),
            null,
        )
    }

    @Test
    fun testReturnTypes() {
        testSample(
            "ReturnTypes",
            "ReturnTypes",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testThrows() {
        testSample(
            "Throws",
            "Throws",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testArguments() {
        testSample(
            "Arguments",
            "Arguments",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassGenerics() {
        testSample(
            "ClassGenerics",
            "ClassGenerics",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testPackageAndImports() {
        testSample(
            "PackageAndImports",
            "PackageAndImports",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testModifiers() {
        testSample(
            "Modifiers",
            "Modifiers",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesTop() {
        testSample(
            "ClassesTop",
            "ClassesTop",
            CLI.CLICommandResult(),
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesInner() {
        testSample(
            "ClassesTop",
            "ClassesInner",
            CLI.CLICommandResult(),
            "ClassesTop.ClassesInner",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesNested() {
        testSample(
            "ClassesTop",
            "ClassesNested",
            CLI.CLICommandResult(),
            "ClassesTop.ClassesNested",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesSide() {
        testSample(
            "ClassesTop",
            "ClassesSide",
            CLI.CLICommandResult(),
            "ClassesSide",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testWhitelist() {
        testSample(
            "ManyFunc",
            "ManyFuncSome",
            CLI.CLICommandResult(),
            null,
            setOf("f2", "f5"),
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testBlacklist() {
        testSample(
            "ManyFunc",
            "ManyFuncSome",
            CLI.CLICommandResult(),
            null,
            null,
            setOf("f1", "f3", "f4"),
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testDefaultOutputPath(@TempDir tempDir: Path) {
        val input = tempDir.resolve("Name.java")
        input.toFile().writeText("public class Name {}")
        ExtractInterfaceCommand(
            input,
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
            null
        ).execute()
        val actual = listOf("public interface NameInterface {", "}")
        val expected = Files.readAllLines(tempDir.resolve("NameInterface.java"))
        Assertions.assertEquals(actual, expected)
    }

    private fun testSample(
        inputSampleName: String,
        expectedSampleName: String,
        expectedCLICommandResult: CLI.CLICommandResult,
        className: String?,
        whitelist: Set<String>?,
        blacklist: Set<String>?,
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