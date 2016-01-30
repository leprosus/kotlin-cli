package com.evalab.core.cli

import com.evalab.core.cli.exception.*
import com.evalab.core.cli.option.*
import com.sun.xml.internal.fastinfoset.util.StringArray
import java.util.*

public open class Command (val name: String, val desc: String) {

    private val options = HashMap<String, Option<*>>(10)
    private val required = HashMap<String, Option<*>>(10)
    private var values = HashMap<String, Option<*>>(10)
    private var help = StringArray(1, 10, true)

    private fun <T> addOption(option: Option<T>): Command {
        if (option.shortForm != null) options.put("-" + option.shortForm, option)

        if (!options.containsKey(option.longForm)) {
            val helpDesc = option.getHelp()

            if (helpDesc != null) help.add(helpDesc)
        }

        if (option.isRequired && !required.containsKey(option.longForm))
            required.put(option.longForm, option)

        options.put("--" + option.longForm, option)

        return this
    }

    @Throws(IllegalOptionValueException::class)
    private fun <T> addValue(option: Option<T>, valueArg: String) {
        val longForm = option.longForm

        option.setValue(valueArg)

        if (values.containsKey(longForm))
            values.remove(longForm)

        values.put(longForm, option)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(option: Option<T>, default: T? = null): T? {
        val found = values.get(option.longForm)
        if (found == null) return default
        else return found.getValue() as T ?: default

    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(shortName: Char, default: T? = null): T? {
        val option = options.get("-" + shortName.toString()) as Option<T>?

        return if (option == null) null else getValue(option, default)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(longName: String, default: T? = null): T? {
        val option = options.get("--" + longName) as Option<T>?

        return if (option == null) null else getValue(option, default)
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

    @Throws(UnknownOptionException::class,
            IllegalOptionValueException::class,
            UnknownSubOptionException::class,
            NotFlagException::class,
            RequiredOptionException::class)
    fun parse(args: Array<String>) {
        var position = 0
        values = HashMap<String, Option<*>>(10)

        while (position < args.size) {
            var arg = args[position]

            if (arg.startsWith("-")) {
                if (arg == "--") break
                else if (arg.startsWith("--")) handleLongOption(arg)
                else if (arg.startsWith("-")) {
                    val next = if (position < args.size - 1) args[position + 1] else null

                    if (arg.length == 2 && next != null && !next.startsWith("-")) handleShortOption(arg, next)
                    else handleFlags(arg)
                }
            }

            position++
        }

        for ((key: String, option: Option<*>) in required) {
            var longForm = option.longForm

            if (!values.containsKey(longForm)) throw RequiredOptionException(option)
        }
    }

    protected fun handleLongOption(line: String) {
        val index = line.indexOf("=")

        var key: String
        var value: String?

        if (index != -1) {
            key = line.substring(2, index)
            value = line.substring(index + 1)
        } else {
            key = line.substring(2)
            value = null
        }

        val option = options.get("--" + key)

        if (option == null) throw UnknownOptionException(key)
        else if (option.withValue && value != null) addValue(option, value)
        else addValue(option, "")
    }

    protected fun handleShortOption(key: String, value: String) {
        val option = options.get(key)

        if (option == null) throw UnknownOptionException(key.substring(1))
        else addValue(option, value)
    }

    protected fun handleFlags(line: String) {
        for (i in 1..line.length - 1) {
            val option = options.get("-" + line[i].toString())

            if (option == null) throw UnknownSubOptionException(line, line[i])
            else if (option.withValue) throw NotFlagException(line, option)

            addValue(option, "")
        }
    }

    protected fun getUsage(): String {
        val formats = StringArray(1, 10, true)

        var counter = 1
        for ((key: String, option: Option<*>) in options)
            if (key.startsWith("--")) {
                var format: String

                if (option.shortForm == null) format = "--" + option.longForm + ""
                else format = "{--" + option.longForm + ", -" + option.shortForm + "}"

                if (option.withValue) format += "=value" + counter++

                if (!option.isRequired) format = "[" + format + "]"

                formats.add(format)
            }

        return "Usage: ${this.name} " +
                if (formats.getSize() > 0) formats.join(" ") else ""
    }

    protected fun getDescription(): String {
        return "Description: ${this.desc}"
    }

    protected fun getOptionsDescription(): String {
        return if (help.getSize() > 0) "Options:" + "\n\t" + help.join("\n\t") else ""
    }

    public open fun getHelp(): String {
        return getUsage() + "\n" +
                getDescription() + "\n" +
                getOptionsDescription()
    }

    protected fun StringArray.join(seporator: String): String {
        var result = ""

        val size = this.getSize()

        if (size > 0) {
            result += this.get(0)

            if (size > 1)
                for (index in 1..(size - 1))
                    result += seporator + this.get(index)
        }

        return result
    }
}