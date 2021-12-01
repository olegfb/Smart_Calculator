fun main() {
    val a = readLine()!!.toBigInteger()
    val b = readLine()!!.toBigInteger()
    val d = a.divideAndRemainder(b)
    println("$a = $b * ${d[0]} + ${d[1]}")
}