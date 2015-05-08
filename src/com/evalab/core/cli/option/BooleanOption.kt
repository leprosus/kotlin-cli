package com.evalab.core.cli.option

public class BooleanOption : Option<Boolean> {
    constructor(longForm: String, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, false, shortForm, helpDesc)

    override fun parse(arg: String): Boolean = true
}