package com.evalab.core.cli.option

public class StringOption : Option<String> {
    constructor(longForm: String, shortForm: Char) : super(longForm, true, shortForm)

    constructor(longForm: String) : super(longForm, true)

    override fun parse(arg: String): String {
        return arg
    }
}