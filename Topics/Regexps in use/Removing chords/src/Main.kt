fun main() {
    val text = readLine()!!
    val rx = "(Am|A|E|Em|D|Dm|G|C)\\s".toRegex()
    println(text.replace(rx, ""))
}