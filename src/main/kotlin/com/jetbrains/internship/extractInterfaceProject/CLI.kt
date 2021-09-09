package com.jetbrains.internship.extractInterfaceProject

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.multiple
import java.nio.file.Paths

class CLI {
    interface CLICommand {
        fun execute(): CLICommandResult
    }

    class CLICommandResult {
        private val errors = ArrayList<String>()
        private val warnings = ArrayList<String>()
        fun addError(error: String): CLICommandResult {
            errors.add(error)
            return this
        }

        fun addWarning(warning: String): CLICommandResult {
            warnings.add(warning)
            return this
        }

        fun flattenToString(): String {
            if (errors.isNotEmpty()) {
                return errors.joinToString(separator = "\n") { "[ERROR] $it" }
            }
            if (warnings.isNotEmpty()) {
                return warnings.joinToString(separator = "\n", postfix = "[INFO] Successfully") { "[WARNING] $it" }
            }
            return "[INFO] Successfully"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CLICommandResult

            if (errors != other.errors) return false
            if (warnings != other.warnings) return false

            return true
        }

        override fun hashCode(): Int {
            var result = errors.hashCode()
            result = 31 * result + warnings.hashCode()
            return result
        }
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
        ).multiple()
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
            whitelist,
            blacklist,
            visibility,
            outputInterfaceName,
            outputFile?.let { Paths.get(it) }
        )
    }

    fun executeAndPrint(cliCommand: CLICommand) {
        print(cliCommand.execute().flattenToString())
    }
}