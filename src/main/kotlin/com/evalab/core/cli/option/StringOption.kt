package com.evalab.core.cli.option

public class StringOption : Option<String> {
    constructor(longForm: String, isRequired: Boolean, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, isRequired, shortForm, helpDesc)

    override fun parse(arg: String): String = arg
}