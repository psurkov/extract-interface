package com.jetbrains.internship.extractInterfaceProject

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
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
        val compilationUnit = StaticJavaParser.parse(inputFile)
        val res = CompilationUnit()

        val inputClass = if (className == null) {
            compilationUnit.primaryType.orElse(null) as? ClassOrInterfaceDeclaration
        } else {
            compilationUnit.getClassByName(className).orElse(null)
        }?.takeIf { !it.isInterface } ?: TODO()

        val result = res.addInterface(inputClass.nameAsString + "Interface")
        inputClass.methods.forEach {
            result.addMethod(it.nameAsString)
        }
        Files.writeString(outputFile, result.toString())
        return CLI.CLICommandResult()
    }
}