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
    private val whitelist: List<String>?,
    private val blacklist: List<String>?,
    private val visibility: List<JavaVisibilityModifier>,
    private val outputInterfaceName: String?,
    private val outputFile: Path?
) : CLI.CLICommand {

    override fun execute(): CLI.CLICommandResult {
        val inputCompilationUnit = StaticJavaParser.parse(inputFile)
        val inputClass = if (className == null) {
            inputCompilationUnit.primaryType.orElse(null) as? ClassOrInterfaceDeclaration
        } else {
            inputCompilationUnit.getClassByName(className).orElse(null)
        }?.takeIf { !it.isInterface } ?: TODO()

        val resultCompilationUnit = CompilationUnit()
        inputCompilationUnit.packageDeclaration.ifPresent { resultCompilationUnit.setPackageDeclaration(it) }
        resultCompilationUnit.imports = inputCompilationUnit.imports
        val resultInterface = resultCompilationUnit.addInterface(inputClass.nameAsString + "Interface")
        inputClass.methods.filter {
            when {
                it.isPrivate -> visibility.contains(JavaVisibilityModifier.PRIVATE)
                it.isProtected -> visibility.contains(JavaVisibilityModifier.PROTECTED)
                it.isPublic -> visibility.contains(JavaVisibilityModifier.PUBLIC)
                else -> visibility.contains(JavaVisibilityModifier.INTERNAL)
            }
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
        Files.writeString(outputFile, resultCompilationUnit.toString())
        return CLI.CLICommandResult()
    }
}