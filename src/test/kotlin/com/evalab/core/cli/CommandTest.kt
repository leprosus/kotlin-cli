package com.evalab.core.cli

import com.evalab.core.cli.exception.OptionException
import org.testng.Assert.*
import org.testng.annotations.Test

class CommandTest {
    @Test
    fun test() {
        val command = Command("command", "Command just for testing")

        command.addBooleanOption("debug", false, 'd', "Flag of debug mode")
                .addBooleanOption("verbose", false, 'v', "Returns detailed information")
                .addIntegerOption("size", false, 's', "Sets size")
                .addDoubleOption("fraction", false, 'f', "Sets fraction")
                .addStringOption("name", true, 'n', "Sets name")
                .addGreedyListOption("files", true, 'l', "Files to load")

        try {
            command.parse(arrayOf("-d", "-s", "10", "--fraction=.2", "--name=fred", "--", "one", "two", "three"))
        } catch (e: OptionException) {
            println(e.message)
            println(command.getHelp())
            System.exit(2)
        }

        assertTrue(command.getFlag("debug"))
        assertFalse(command.getFlag("verbose"))
        assertEquals(command.getIntegerValue("size"),10)
        assertEquals(command.getDoubleValue("fraction"),.2)
        assertEquals(command.getStringValue("name"),"fred")
        assertEquals(command.getListValue("files"), listOf("one", "two", "three"))
    }
}