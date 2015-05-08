package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public abstract class Option<T> private(val longForm: String, val withValue: Boolean, val shortForm: String? = null, val helpDesc: String? = null) {
    private var value: T = null

    protected constructor(longForm: String, withValue: Boolean, shortForm: Char? = null, helpDesc: String? = null) : this(longForm, withValue, shortForm?.toString(), helpDesc)

    fun getHelp(): String? {
        if (helpDesc == null) return null
        else return (if (shortForm != null) "-" + shortForm + " / " else "") + "--" + longForm + ": " + helpDesc
    }

    throws(javaClass<IllegalOptionValueException>())
    fun setValue(arg: String) {
        if (this.withValue && arg == "") throw IllegalOptionValueException(this, "")

        value = parse(arg)
    }

    fun getValue(): T {
        return value
    }

    throws(javaClass<IllegalOptionValueException>())
    protected open fun parse(arg: String): T {
        return null
    }
}