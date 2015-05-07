package com.evalab.core.cli

import com.evalab.core.cli.exception.*
import com.evalab.core.cli.option.*
import java.util.HashMap

class Cli {
    private val options = HashMap<String, Option<*>>(10)
    private var values = HashMap<String, Option<*>>(10)

    private fun addOption<T>(option: Option<T>): Cli {
        if (option.shortForm != null) options.put(option.shortForm, option)

        options.put(option.longForm, option)

        return this
    }

    throws(javaClass<IllegalOptionValueException>())
    private fun addValue<T>(option: Option<T>, valueArg: String) {
        val longForm = option.longForm

        option.setValue(valueArg)

        if (values.containsKey(longForm))
            values.remove(longForm)

        values.put(longForm, option)
    }

    private fun getValue<T>(option: Option<T>, default: T? = null): T? {
        val option = values.get(option.longForm)
        if (option == null) {
            return default
        } else {
            val value = option.getValue()

            return if (value == null) default else value as T
        }
    }

    private fun getValue<T>(shortName: Char, default: T? = null): T? {
        return getValue(shortName.toString(), default)
    }

    private fun getValue<T>(longName: String, default: T? = null): T? {
        val option = options.get(longName)

        return if (option == null) null else getValue(option as Option<T>, default)
    }

    fun addStringOption(longForm: String, shortForm: Char): Cli {
        return addOption(StringOption(longForm, shortForm))
    }

    fun addStringOption(longForm: String): Cli {
        return addOption(StringOption(longForm))
    }

    fun addIntegerOption(longForm: String, shortForm: Char): Cli {
        return addOption(IntegerOption(longForm, shortForm))
    }

    fun addIntegerOption(longForm: String): Cli {
        return addOption(IntegerOption(longForm))
    }

    fun addLongOption(longForm: String, shortForm: Char): Cli {
        return addOption(LongOption(longForm, shortForm))
    }

    fun addLongOption(longForm: String): Cli {
        return addOption(LongOption(longForm))
    }

    fun addDoubleOption(longForm: String, shortForm: Char): Cli {
        return addOption(DoubleOption(longForm, shortForm))
    }

    fun addDoubleOption(longForm: String): Cli {
        return addOption(DoubleOption(longForm))
    }

    fun addBooleanOption(longForm: String, shortForm: Char): Cli {
        return addOption(BooleanOption(longForm, shortForm))
    }

    fun addBooleanOption(longForm: String): Cli {
        return addOption(BooleanOption(longForm))
    }

    fun getStringValue(longForm: String, default: String? = null): String? {
        return getValue(longForm, default)
    }

    fun getStringValue(shortForm: Char, default: String? = null): String? {
        return getValue(shortForm, default)
    }

    fun getIntegerValue(longForm: String, default: Int? = null): Int? {
        return getValue(longForm, default)
    }

    fun getIntegerValue(shortForm: Char, default: Int? = null): Int? {
        return getValue(shortForm, default)
    }

    fun getLongValue(longForm: String, default: Long? = null): Long? {
        return getValue(longForm, default)
    }

    fun getLongValue(shortForm: Char, default: Long? = null): Long? {
        return getValue(shortForm, default)
    }

    fun getDoubleValue(longForm: String, default: Double? = null): Double? {
        return getValue(longForm, default)
    }

    fun getDoubleValue(shortForm: Char, default: Double? = null): Double? {
        return getValue(shortForm, default)
    }

    fun getBooleanValue(longForm: String, default: Boolean? = null): Boolean? {
        return getValue(longForm, default)
    }

    fun getBooleanValue(shortForm: Char, default: Boolean? = null): Boolean? {
        return getValue(shortForm, default)
    }

    throws(javaClass<OptionException>())
    fun parse(args: Array<String>) {
        var position = 0
        values = HashMap<String, Option<*>>(10)

        val option: Option<*>?
        while (position < args.size()) {
            var arg = args[position]

            if (arg.startsWith("-")) {
                if (arg == "--") {
                    // Handle arguments end --
                    break
                } else if (arg.startsWith("--")) {
                    // Handle --arg=value
                    val index = arg.indexOf("=")
                    if (index != -1) {
                        var key = arg.substring(2, index)
                        var value = arg.substring(index + 1)

                        option = options.get(key)

                        if (option == null) {
                            throw UnknownOptionException(key)
                        } else if (option.withValue) {
                            if (value.length() == 0 )
                                throw IllegalOptionValueException(option, "")

                            addValue(option, value)
                        } else {
                            addValue(option, "")
                        }
                    }
                } else if (arg.startsWith("-")) {
                    // Handle -abcd
                    for (i in 1..arg.length() - 1) {
                        option = options.get(arg.charAt(i).toString())

                        if (option == null) {
                            throw UnknownSubOptionException(arg, arg.charAt(i))
                        } else if (option.withValue) {
                            throw NotFlagException(arg, arg.charAt(i))
                        }

                        addValue(option, "")
                    }
                }
            }

            position++
        }
    }
}