package com.evalab.core.cli.option

public class GreedyListOption : Option<List<String>> {
    constructor(longForm: String, isRequired: Boolean, shortForm: Char? = null, helpDesc: String? = null) : super(longForm, true, isRequired, shortForm, helpDesc)

    override fun parse(args: String): List<String> = args.split(",")
}