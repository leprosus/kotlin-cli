# kotlin-cli

Kotlin-CLI - command line option parsing suite for Kotlin

## Usage

```kotlin

import com.evalab.core.cli.Command
import com.evalab.core.cli.exception.OptionException

fun main(args: Array<String>) {
    val command = Command("command", "Command just for testing")

    command.addBooleanOption("debug", false, 'd', "Flag of debug mode")
    command.addBooleanOption("verbose", false, 'v', "Returns detailed information")
    command.addIntegerOption("size", false, 's', "Sets size")
    command.addDoubleOption("fraction", false, 'f', "Sets fraction")
    command.addStringOption("name", true, 'n', "Sets name")

    try {
        command.parse(args)
    } catch (e: OptionException) {
        println(e.getMessage())
        println(command.getHelp())
        System.exit(2)
    }

    val debug = command.getBooleanValue("debug", false)
    val verbose = command.getBooleanValue("verbose", false)
    val size = command.getIntegerValue("size", 0)
    val fraction = command.getDoubleValue("fraction", 0.0)
    val name = command.getStringValue("name")

    println("debug: " + debug)
    println("verbose: " + verbose)
    println("size: " + size)
    println("fraction: " + fraction)
    println("name: " + name)
}
```

## Contact

Follow me [@leprosus](https://twitter.com/leprosus_ru)

## License

MIT License. See the `LICENSE` file.