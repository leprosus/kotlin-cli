package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class RequiredOptionException(val option: Option<*>, description: String = "Option " + option.toString() + " is required ") : OptionException(description)