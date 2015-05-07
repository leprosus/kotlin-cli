package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public abstract class Option<T> private(val longForm: String, val withValue: Boolean, val shortForm: String?) {
    private var value: T = null

    protected constructor(longForm: String, withValue: Boolean) : this(longForm, withValue, null)

    protected constructor(longForm: String, withValue: Boolean, shortForm: Char) : this(longForm, withValue, shortForm.toString())

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