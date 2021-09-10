package com.jetbrains.internship.extractInterfaceProject

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import java.nio.file.Files
import java.nio.file.Path

class ExtractInterfaceCommand(
    private val inputFile: Path,
    private val className: String?,
    private val whitelist: Set<String>?,
    private val blacklist: Set<String>?,
    private val visibility: List<JavaVisibilityModifier>,
    private val outputInterfaceName: String?,
    private val outputFile: Path?
) : CLI.CLICommand {

    override fun execute(): CLI.CLICommandResult {
        val inputCompilationUnit = StaticJavaParser.parse(inputFile)
        val selectedClass = findClass(inputCompilationUnit, className)

        val targetCompilationUnit = CompilationUnit()
        val targetInterface =
            targetCompilationUnit.addInterface(outputInterfaceName ?: selectedClass.nameAsString + "Interface")

        addPackageAndImports(inputCompilationUnit, targetCompilationUnit)
        addMethods(selectedClass, targetInterface, whitelist, blacklist)
        addStaticClasses(selectedClass, targetInterface)

        Files.writeString(
            outputFile ?: inputFile.parent.resolve("${selectedClass.nameAsString}Interface.java"),
            targetCompilationUnit.toString()
        )
        return CLI.CLICommandResult()
    }

    private fun findClass(
        compilationUnit: CompilationUnit,
        targetClassName: String?
    ): ClassOrInterfaceDeclaration {
        return if (targetClassName == null) {
            compilationUnit.primaryType.orElse(null) as? ClassOrInterfaceDeclaration
        } else {
            compilationUnit.findFirst(
                ClassOrInterfaceDeclaration::class.java
            ) {
                it.fullyQualifiedName.filter { className -> className == targetClassName }.isPresent
            }.orElse(null)
        }?.takeIf { !it.isInterface } ?: TODO()
    }

    private fun addPackageAndImports(
        inputCompilationUnit: CompilationUnit,
        targetCompilationUnit: CompilationUnit
    ) {
        inputCompilationUnit.packageDeclaration.ifPresent { targetCompilationUnit.setPackageDeclaration(it) }
        targetCompilationUnit.imports = inputCompilationUnit.imports
    }

    private fun addMethods(
        targetClass: ClassOrInterfaceDeclaration,
        resultInterface: ClassOrInterfaceDeclaration,
        whitelist: Set<String>?,
        blacklist: Set<String>?
    ) {
        // TODO check whitelist & blacklist intersection
        targetClass.methods.filter {
            when {
                it.isPrivate -> visibility.contains(JavaVisibilityModifier.PRIVATE)
                it.isProtected -> visibility.contains(JavaVisibilityModifier.PROTECTED)
                it.isPublic -> visibility.contains(JavaVisibilityModifier.PUBLIC)
                else -> visibility.contains(JavaVisibilityModifier.INTERNAL)
            } && (whitelist == null || whitelist.contains(it.nameAsString))
                    && (blacklist == null || !blacklist.contains(it.nameAsString))
        }.forEach {
            resultInterface.members.add(it.apply {
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
        targetClass: ClassOrInterfaceDeclaration,
        resultInterface: ClassOrInterfaceDeclaration
    ) {
        targetClass.childNodes
            .filterIsInstance<ClassOrInterfaceDeclaration>()
            .filter { it.isStatic }
            .forEach(resultInterface::addMember)
    }
}