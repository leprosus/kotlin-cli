package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public class LongOption : Option<Long> {
    constructor(longForm: String, isRequired: Boolean, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, isRequired, shortForm, helpDesc)

    @Throws(IllegalOptionValueException::class)
    override fun parse(arg: String): Long {
        try {
            return arg.toLong()
        } catch (e: NumberFormatException) {
            throw IllegalOptionValueException(this, arg)
        }
    }
}