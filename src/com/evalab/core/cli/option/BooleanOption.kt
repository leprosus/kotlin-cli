package com.evalab.core.cli.option

public class BooleanOption : Option<Boolean> {
    constructor(longForm: String, shortForm: Char) : super(longForm, false, shortForm)

    constructor(longForm: String) : super(longForm, false)

    override fun parse(arg: String): Boolean = true
}