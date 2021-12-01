fun main() {
    val text = readLine()!!
    println(text.replace("a+".toRegex(RegexOption.IGNORE_CASE), "a"))
}