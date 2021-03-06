package com.jetbrains.internship.extractInterfaceProject

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import java.nio.file.Files
import java.nio.file.Path

class ExtractInterfaceCommand(
    val inputFile: Path,
    val className: String?,
    val whitelist: Set<String>?,
    val blacklist: Set<String>?,
    val visibility: List<JavaVisibilityModifier>,
    val outputInterfaceName: String?,
    val outputFile: Path?
) : CLI.CLICommand {

    override fun execute(): String {
        val inputCompilationUnit = StaticJavaParser.parse(inputFile)
        val selectedClass = findClass(inputCompilationUnit, className)

        val targetCompilationUnit = CompilationUnit()
        val targetInterface =
            targetCompilationUnit.addInterface(outputInterfaceName ?: selectedClass.nameAsString + "Interface")

        addPackageAndImports(inputCompilationUnit, targetCompilationUnit)
        addClassGenerics(selectedClass, targetInterface)
        addMethods(selectedClass, targetInterface, whitelist, blacklist)
        addStaticClasses(selectedClass, targetInterface)

        Files.writeString(
            outputFile ?: inputFile.toAbsolutePath().parent.resolve(
                "${selectedClass.nameAsString}Interface.java"
            ),
            targetCompilationUnit.toString()
        )
        return "Successfully extracted interface \"${targetInterface.nameAsString}\" " +
                "from class \"${selectedClass.nameAsString}\""
    }

    private fun findClass(
        compilationUnit: CompilationUnit,
        targetClassName: String?
    ): ClassOrInterfaceDeclaration {
        return if (targetClassName == null) {
            (compilationUnit.primaryType.orElse(null) as? ClassOrInterfaceDeclaration)?.takeUnless {
                it.isInterface
            } ?: throw IllegalArgumentException("Cannot find primary class")
        } else {
            compilationUnit.findFirst(
                ClassOrInterfaceDeclaration::class.java
            ) {
                !it.isInterface && it.fullyQualifiedName.filter { className -> className == targetClassName }.isPresent
            }.orElseThrow { IllegalArgumentException("Cannot find class $targetClassName") }
        }
    }

    private fun addPackageAndImports(
        inputCompilationUnit: CompilationUnit,
        targetCompilationUnit: CompilationUnit
    ) {
        inputCompilationUnit.packageDeclaration.ifPresent { targetCompilationUnit.setPackageDeclaration(it) }
        targetCompilationUnit.imports = inputCompilationUnit.imports
    }

    private fun addClassGenerics(
        selectedClass: ClassOrInterfaceDeclaration,
        targetInterface: ClassOrInterfaceDeclaration
    ) {
        selectedClass.typeParameters
            .forEach(targetInterface::addTypeParameter)
    }

    private fun addMethods(
        selectedClass: ClassOrInterfaceDeclaration,
        targetInterface: ClassOrInterfaceDeclaration,
        whitelist: Set<String>?,
        blacklist: Set<String>?
    ) {
        if (whitelist.orEmpty().intersect(blacklist.orEmpty()).isNotEmpty()) {
            throw IllegalArgumentException("Whitelist and blacklist overlap")
        }
        selectedClass.methods.filter {
            when {
                it.isPrivate -> visibility.contains(JavaVisibilityModifier.PRIVATE)
                it.isProtected -> visibility.contains(JavaVisibilityModifier.PROTECTED)
                it.isPublic -> visibility.contains(JavaVisibilityModifier.PUBLIC)
                else -> visibility.contains(JavaVisibilityModifier.INTERNAL)
            } && (whitelist == null || whitelist.contains(it.nameAsString))
                    && (blacklist == null || !blacklist.contains(it.nameAsString))
        }.forEach {
            targetInterface.members.add(it.apply {
                if (!it.isStatic) {
                    removeBody()
                }
                removeModifier(
                    Modifier.Keyword.PRIVATE,
                    Modifier.Keyword.PROTECTED,
                    Modifier.Keyword.PUBLIC,
                    Modifier.Keyword.ABSTRACT,
                    Modifier.Keyword.FINAL,
                    Modifier.Keyword.SYNCHRONIZED,
                    Modifier.Keyword.NATIVE
                )
            })
        }
    }

    private fun addStaticClasses(
        selectedClass: ClassOrInterfaceDeclaration,
        targetInterface: ClassOrInterfaceDeclaration
    ) {
        selectedClass.childNodes
            .filterIsInstance<ClassOrInterfaceDeclaration>()
            .filter { it.isStatic }
            .forEach(targetInterface::addMember)
    }
}