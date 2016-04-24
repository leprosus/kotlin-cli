package com.evalab.core.cli.option

import com.evalab.core.cli.exception.IllegalOptionNameException
import com.evalab.core.cli.exception.IllegalOptionValueException
import java.util.regex.Pattern

public abstract class Option<T> private constructor(
        val longForm: String,
        val withValue: Boolean,
        val isRequired: Boolean = false,
        val shortForm: String? = null,
        val helpDesc: String? = null
) {
    private var value: T? = null

    val longFormPattern = Pattern.compile("^([a-z](?:[a-z0-9_\\-]*[a-z0-9])?)$", Pattern.CASE_INSENSITIVE)
    val shortFormPattern = Pattern.compile("^[a-z]$", Pattern.CASE_INSENSITIVE)

    init {
        val longFormMatcher = longFormPattern.matcher(longForm)
        if (!longFormMatcher.matches()) throw IllegalOptionNameException(this)

        if (shortForm != null) {
            val shortFormMatcher = shortFormPattern.matcher(shortForm)
            if (!shortFormMatcher.matches()) throw IllegalOptionNameException(this)
        }
    }

    protected constructor(
            longForm: String,
            withValue: Boolean,
            isRequired: Boolean,
            shortForm: Char? = null,
            helpDesc: String? = null) :
    this(longForm, withValue, isRequired, shortForm?.toString(), helpDesc)

    fun getHelp(): String? {
        if (helpDesc == null) return null
        else {
            val options = (if (shortForm != null) "-$shortForm, " else "") + "--" + longForm + ":";
            val tabs = 4 - (options.length / 4).toInt()


            return options + "\t".repeat(tabs) + helpDesc +
                    (if (isRequired) " (is required)" else "")
        }
    }

    @Throws(IllegalOptionValueException::class)
    fun setValue(arg: String) {
        if (this.withValue && arg == "") throw IllegalOptionValueException(this, "")

        value = parse(arg)
    }

    fun getValue(): T {
        return value!!
    }

    @Throws(IllegalOptionValueException::class)
    protected abstract fun parse(arg: String): T

    override fun toString(): String {
        return (if (shortForm != null) "-$shortForm, " else "") + "--" + longForm
    }
}