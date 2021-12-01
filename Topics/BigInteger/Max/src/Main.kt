import java.math.BigInteger

fun main() {
    println(mymax(readLine()!!.toBigInteger(), readLine()!!.toBigInteger()))
}

fun mymax(a1: BigInteger, a2: BigInteger) = (a1 + a2 + (a1 - a2).abs()) / BigInteger.TWO
