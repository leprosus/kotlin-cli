package com.evalab.core.cli.exception

public class NotFlagException(optionLong: String, public val optionShort: Char) : UnknownOptionException(optionLong, "Illegal option: '" + optionLong + "', '" + optionShort + "' requires a value")