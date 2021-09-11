package com.jetbrains.internship.extractInterfaceProject

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.multiple
import java.nio.file.Paths

class CLI {
    interface CLICommand {
        fun execute(): String
    }

    fun readCommand(args: Array<String>): CLICommand {
        val parser = ArgParser("extract-interface")
        val inputFile by parser.argument(
            ArgType.String,
            description = "Input file"
        )
        val className by parser.option(
            ArgType.String,
            shortName = "c",
            description = "Class name"
        )
        val whitelist by parser.option(
            ArgType.String,
            shortName = "w",
            description = "Whitelist"
        ).multiple()
        val blacklist by parser.option(
            ArgType.String,
            shortName = "b",
            description = "Blacklist"
        ).multiple()
        val visibility by parser.option(
            ArgType.Choice<JavaVisibilityModifier>(),
            shortName = "v",
            description = "Visibility"
        ).multiple().default(listOf(JavaVisibilityModifier.PUBLIC))
        val outputInterfaceName by parser.option(
            ArgType.String,
            shortName = "i",
            description = "Output interface name"
        )
        val outputFile by parser.option(
            ArgType.String,
            shortName = "o",
            description = "Output file"
        )
        parser.parse(args)
        return ExtractInterfaceCommand(
            Paths.get(inputFile),
            className,
            whitelist.toSet().takeIf { it.isNotEmpty() },
            blacklist.toSet().takeIf { it.isNotEmpty() },
            visibility,
            outputInterfaceName,
            outputFile?.let { Paths.get(it) }
        )
    }

    fun executeAndPrint(cliCommand: CLICommand) {
        print(
            try {
                cliCommand.execute()
            } catch (exception: Exception) {
                exception.toString()
            }
        )
    }
}