package com.evalab.core.cli.option

public class StringOption : Option<String> {
    constructor(longForm: String, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, shortForm, helpDesc)

    override fun parse(arg: String): String = arg
}