package com.evalab.core.cli.exception

public open class RequiredOptionException(val optionLong: String, description: String = "Option '" + optionLong + "' is required") : OptionException(description)