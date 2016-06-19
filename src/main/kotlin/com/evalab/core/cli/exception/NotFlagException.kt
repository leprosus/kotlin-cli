package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class NotFlagException(options: String, val option: Option<*>) : UnknownOptionException(options, "Illegal option: -" + option.shortForm + " requires a value in '" + options + "'")