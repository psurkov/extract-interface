package com.jetbrains.internship.extractInterfaceProject

import com.github.javaparser.ParseProblemException
import com.jetbrains.internship.extractInterfaceProject.JavaVisibilityModifier.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths

internal class ExtractInterfaceCommandTest {

    @TempDir
    lateinit var tempDir: Path

    private val inputPath = Paths.get("src/test/resources/testData/input")
    private val expectedPath = Paths.get("src/test/resources/testData/expected")

    @Test
    fun testEmpty() {
        checkCorrectSample(
            "Empty",
            "Empty",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testEmptyCustomName() {
        checkCorrectSample(
            "Empty",
            "EmptyCustomName",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            "EmptyCustomName",
        )
    }

    @Test
    fun testJustFields() {
        checkCorrectSample(
            "JustFields",
            "JustFields",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testOnlyPrivateVisibility() {
        checkCorrectSample(
            "Visibility",
            "VisibilityOnlyPrivate",
            null,
            null,
            null,
            listOf(PRIVATE),
            null,
        )
    }

    @Test
    fun testPublicAndInternalVisibility() {
        checkCorrectSample(
            "Visibility",
            "VisibilityPublicAndInternal",
            null,
            null,
            null,
            listOf(PUBLIC, INTERNAL),
            null,
        )
    }

    @Test
    fun testReturnTypes() {
        checkCorrectSample(
            "ReturnTypes",
            "ReturnTypes",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testThrows() {
        checkCorrectSample(
            "Throws",
            "Throws",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testArguments() {
        checkCorrectSample(
            "Arguments",
            "Arguments",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassGenerics() {
        checkCorrectSample(
            "ClassGenerics",
            "ClassGenerics",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testPackageAndImports() {
        checkCorrectSample(
            "PackageAndImports",
            "PackageAndImports",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testModifiers() {
        checkCorrectSample(
            "Modifiers",
            "Modifiers",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesTop() {
        checkCorrectSample(
            "ClassesTop",
            "ClassesTop",
            null,
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesInner() {
        checkCorrectSample(
            "ClassesTop",
            "ClassesInner",
            "ClassesTop.ClassesInner",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesNested() {
        checkCorrectSample(
            "ClassesTop",
            "ClassesNested",
            "ClassesTop.ClassesNested",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testClassesSide() {
        checkCorrectSample(
            "ClassesTop",
            "ClassesSide",
            "ClassesSide",
            null,
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testWhitelist() {
        checkCorrectSample(
            "ManyFunc",
            "ManyFuncSome",
            null,
            setOf("f2", "f5"),
            null,
            JavaVisibilityModifier.values().asList(),
            null,
        )
    }

    @Test
    fun testBlacklist() {
        checkCorrectSample(
            "ManyFunc",
            "ManyFuncSome",
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

    private fun checkCorrectSample(
        inputSampleName: String,
        expectedSampleName: String,
        className: String?,
        whitelist: Set<String>?,
        blacklist: Set<String>?,
        visibility: List<JavaVisibilityModifier>,
        outputInterfaceName: String?
    ) {
        val outputInterfacePath = tempDir.resolve("result.java")
        ExtractInterfaceCommand(
            inputPath.resolve("$inputSampleName.java"),
            className,
            whitelist,
            blacklist,
            visibility,
            outputInterfaceName,
            outputInterfacePath
        ).execute()
        val actual = Files.readAllLines(outputInterfacePath)
        val expected = Files.readAllLines(expectedPath.resolve("$expectedSampleName.java"))
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun testNoSuchFile(@TempDir tempDir: Path) {
        val input = tempDir.resolve("NoSuchFile.java")
        Assertions.assertThrows(NoSuchFileException::class.java) {
            ExtractInterfaceCommand(
                input,
                null,
                null,
                null,
                JavaVisibilityModifier.values().asList(),
                null,
                null
            ).execute()
        }
    }

    @Test
    fun testNotJavaInput(@TempDir tempDir: Path) {
        val input = tempDir.resolve("text.txt")
        input.toFile().writeText("this is not a Java program")
        Assertions.assertThrows(ParseProblemException::class.java) {
            ExtractInterfaceCommand(
                input,
                null,
                null,
                null,
                JavaVisibilityModifier.values().asList(),
                null,
                null
            ).execute()
        }
    }

    @Test
    fun testNoTargetClass(@TempDir tempDir: Path) {
        val input = tempDir.resolve("A.java")
        input.toFile().writeText("public class A {}")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ExtractInterfaceCommand(
                input,
                "B",
                null,
                null,
                JavaVisibilityModifier.values().asList(),
                null,
                null
            ).execute()
        }
    }

    @Test
    fun testNoRootClass(@TempDir tempDir: Path) {
        val input = tempDir.resolve("A.java")
        input.toFile().writeText("interface A {}")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ExtractInterfaceCommand(
                input,
                null,
                null,
                null,
                JavaVisibilityModifier.values().asList(),
                null,
                null
            ).execute()
        }
    }

    @Test
    fun testTargetIsEnum(@TempDir tempDir: Path) {
        val input = tempDir.resolve("A.java")
        input.toFile().writeText("enum A {}")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ExtractInterfaceCommand(
                input,
                "A",
                null,
                null,
                JavaVisibilityModifier.values().asList(),
                null,
                null
            ).execute()
        }
    }
}