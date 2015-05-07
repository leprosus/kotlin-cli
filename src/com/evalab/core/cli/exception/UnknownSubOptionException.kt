package com.evalab.core.cli.exception

public class UnknownSubOptionException(optionLong: String, val subOption: Char) : UnknownOptionException(optionLong, "Unknown option: '" + subOption + "' in '" + optionLong + "'")