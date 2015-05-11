package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionValueException

public abstract class Option<T> private(val longForm: String, val withValue: Boolean, val isRequired: Boolean = false, val shortForm: String? = null, val helpDesc: String? = null) {
    private var value: T = null

    protected constructor(longForm: String, withValue: Boolean, isRequired: Boolean, shortForm: Char? = null, helpDesc: String? = null) : this(longForm, withValue, isRequired, shortForm?.toString(), helpDesc)

    fun getHelp(): String? {
        if (helpDesc == null) return null
        else {
            val options = (if (shortForm != null) "-" + shortForm + ", " else "") + "--" + longForm + ":";
            val tabs = 4 - (options.length() / 4).toInt()


            return  options + "\t".repeat(tabs) +
                    (helpDesc ?: "") +
                    (if (isRequired) " (is required)" else "")
        }
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