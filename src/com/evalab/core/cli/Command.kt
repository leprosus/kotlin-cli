package com.evalab.core.cli

import com.evalab.core.cli.exception.*
import com.evalab.core.cli.option.*
import com.sun.xml.internal.fastinfoset.util.StringArray
import java.util.HashMap

public class Command (val name: String, val desc: String) {

    private val options = HashMap<String, Option<*>>(10)
    private val required = HashMap<String, Option<*>>(10)
    private var values = HashMap<String, Option<*>>(10)
    private var help = StringArray(1, 10, true)

    private fun addOption<T>(option: Option<T>): Command {
        if (option.shortForm != null) options.put(option.shortForm, option)

        if (!options.containsKey(option.longForm)) {
            val helpDesc = option.getHelp()

            if (helpDesc != null) help.add(helpDesc)
        }

        if (option.isRequired && !required.containsKey(option.longForm))
            required.put(option.longForm, option)

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

        if (option == null) return default
        else {
            val value = option.getValue()

            return if (value == null) default else value as T
        }
    }

    private fun getValue<T>(shortName: Char, default: T? = null): T? = getValue(shortName.toString(), default)

    private fun getValue<T>(longName: String, default: T? = null): T? {
        val option = options.get(longName)

        return if (option == null) null else getValue(option as Option<T>, default)
    }

    fun addStringOption(longForm: String, isRequired: Boolean, shortForm: Char? = null, help: String? = null): Command = addOption(StringOption(longForm, isRequired, shortForm, help))

    fun addIntegerOption(longForm: String, isRequired: Boolean, shortForm: Char? = null, help: String? = null): Command = addOption(IntegerOption(longForm, isRequired, shortForm, help))

    fun addLongOption(longForm: String, isRequired: Boolean, shortForm: Char? = null, help: String? = null): Command = addOption(LongOption(longForm, isRequired, shortForm, help))

    fun addDoubleOption(longForm: String, isRequired: Boolean, shortForm: Char? = null, help: String? = null): Command = addOption(DoubleOption(longForm, isRequired, shortForm, help))

    fun addBooleanOption(longForm: String, isRequired: Boolean, shortForm: Char? = null, help: String? = null): Command = addOption(BooleanOption(longForm, isRequired, shortForm, help))

    fun getStringValue(longForm: String, default: String? = null): String? = getValue(longForm, default)

    fun getStringValue(shortForm: Char, default: String? = null): String? = getValue(shortForm, default)

    fun getIntegerValue(longForm: String, default: Int? = null): Int? = getValue(longForm, default)

    fun getIntegerValue(shortForm: Char, default: Int? = null): Int? = getValue(shortForm, default)

    fun getLongValue(longForm: String, default: Long? = null): Long? = getValue(longForm, default)

    fun getLongValue(shortForm: Char, default: Long? = null): Long? = getValue(shortForm, default)

    fun getDoubleValue(longForm: String, default: Double? = null): Double? = getValue(longForm, default)

    fun getDoubleValue(shortForm: Char, default: Double? = null): Double? = getValue(shortForm, default)

    fun getBooleanValue(longForm: String, default: Boolean? = null): Boolean? = getValue(longForm, default)

    fun getBooleanValue(shortForm: Char, default: Boolean? = null): Boolean? = getValue(shortForm, default)

    throws(javaClass<UnknownOptionException>())
    throws(javaClass<IllegalOptionValueException>())
    throws(javaClass<UnknownSubOptionException>())
    throws(javaClass<NotFlagException>())
    throws(javaClass<RequiredOptionException>())
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

                        if (option == null) throw UnknownOptionException(key)
                        else if (option.withValue) {
                            if (value.length() == 0 ) throw IllegalOptionValueException(option, "")

                            addValue(option, value)
                        } else addValue(option, "")
                    }
                } else if (arg.startsWith("-")) {
                    // Handle -abcd

                    for (i in 1..arg.length() - 1) {
                        option = options.get(arg.charAt(i).toString())

                        if (option == null) throw UnknownSubOptionException(arg, arg.charAt(i))
                        else if (option.withValue) throw NotFlagException(arg, arg.charAt(i))

                        addValue(option, "")
                    }
                }
            }

            position++
        }

        for ((key, option) in required) {
            var longForm = option.longForm

            if (!values.containsKey(longForm)) throw RequiredOptionException(longForm)
        }
    }

    public open fun printHelp(): String {
        return "Description: ${this.desc}\n" +
                "Usage: ${this.name}" + (if (help.getSize() > 0) " [options]" else "") +
                "\n\t" + help.getArray().join("\n\t")
    }
}