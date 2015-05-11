package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class IllegalOptionNameException(val option: Option<*>) : OptionException("Illegal name option " + option.toString())