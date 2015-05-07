package com.evalab.core.cli.exception

public open class UnknownOptionException(val optionLong: String, description: String = "Unknown option '" + optionLong + "'") : OptionException(description)