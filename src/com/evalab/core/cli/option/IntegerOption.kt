package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public class IntegerOption : Option<Int> {
    constructor(
            longForm: String,
            isRequired: Boolean,
            shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, isRequired, shortForm, helpDesc)

    @Throws(IllegalOptionValueException::class)
    override fun parse(arg: String): Int {
        try {
            return arg.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalOptionValueException(this, arg)
        }
    }
}