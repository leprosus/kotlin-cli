package com.evalab.core.cli.exception

import com.evalab.core.cli.option.Option

public class MultipleGreedyOptionException(val option: Option<*>) : OptionException("Cannot define another greedy option " + option.toString())