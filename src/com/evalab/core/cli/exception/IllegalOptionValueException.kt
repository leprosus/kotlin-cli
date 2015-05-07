package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class IllegalOptionValueException(val option: Option<*>, val value: String) : OptionException("Illegal value '" + value + "' for option " + (if (option.shortForm != null) "-" + option.shortForm + " / " else "") + "--" + option.longForm)