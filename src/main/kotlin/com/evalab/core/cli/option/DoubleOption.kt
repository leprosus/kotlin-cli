package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException
import java.text.ParseException

public class DoubleOption : Option<Double> {
    constructor(
            longForm: String,
            isRequired: Boolean,
            shortForm: Char? = null,
            helpDesc: String? = null
    ) : super(longForm, true, isRequired, shortForm, helpDesc)

    @Throws(IllegalOptionValueException::class)
    override fun parse(arg: String): Double {
        try {
            return arg.toDouble()
        } catch (e: ParseException) {
            throw IllegalOptionValueException(this, arg)
        }
    }
}