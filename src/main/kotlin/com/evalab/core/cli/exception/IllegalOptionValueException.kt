package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class IllegalOptionValueException(val option: Option<*>, val value: String) : OptionException("Illegal value '" + value + "' for option " + option.toString())