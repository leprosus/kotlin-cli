package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public class IntegerOption : Option<Int> {
    constructor(longForm: String, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, shortForm, helpDesc)

    throws(javaClass<IllegalOptionValueException>())
    override fun parse(arg: String): Int {
        try {
            return arg.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalOptionValueException(this, arg)
        }
    }
}