fun main() {
    val text = readLine()!!
    val regexColors = "#[0-9a-fA-F]{6}\\b".toRegex()
    regexColors.findAll(text).forEach { println(it.value) }
    // write your code here
}