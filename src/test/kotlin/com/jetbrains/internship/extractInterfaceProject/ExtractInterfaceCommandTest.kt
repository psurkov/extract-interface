package com.jetbrains.internship.extractInterfaceProject

import com.jetbrains.internship.extractInterfaceProject.JavaVisibilityModifier.*
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
}