package com.jetbrains.internship.extractInterfaceProject

fun main(args: Array<String>) {
    with(CLI()) {
        executeAndPrint(readCommand(args))
    }
}