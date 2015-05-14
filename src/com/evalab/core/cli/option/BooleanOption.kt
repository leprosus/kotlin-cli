package com.evalab.core.cli.option

public class BooleanOption : Option<Boolean> {
    constructor(
            longForm: String,
            isRequired: Boolean,
            shortForm: Char? = null,
            helpDesc: String? = null
    ) : super(longForm, false, isRequired, shortForm, helpDesc)

    override fun parse(arg: String): Boolean = true
}