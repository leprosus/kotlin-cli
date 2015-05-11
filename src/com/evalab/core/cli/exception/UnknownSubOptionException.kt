package com.evalab.core.cli.exception

public class UnknownSubOptionException(options: String, val subOption: Char) : UnknownOptionException(options, "Unknown option: '" + subOption + "' in '" + options + "'")