package calculator

import java.math.BigInteger

class CalculatorOPN {
    private val variableMap = mutableMapOf<String, String>()
    private var arrInp = mutableListOf<String>()
    private var arrOPN = mutableListOf<String>()
    private var flEnd = false
    private var arrOperator = mutableListOf<String>()

    private fun getOperator(str: String): String {
        val cntMinus = str.count { it == '-' }
        if (cntMinus == 0 || cntMinus % 2 == 0) return "+"
        return "-"
    }

    private fun setVariable(pStr: String): Boolean {
        val (str1, str2) = pStr.split("=", limit = 2)
        if (str2.matches("[+-]?\\d+".toRegex())) {
            variableMap[str1] = str2
            return true
        }
        val pGetVar = getVariable(str2)
        if (pGetVar.first) variableMap[str1] = pGetVar.second
        else return false
        return true
    }

    private fun getVariable(pStr: String): Pair<Boolean, String> {
        val vOper = if (pStr[0] == '-')  pStr[0] else '+'
        var tmpStr = pStr.replace("-".toRegex(), "")
        if (tmpStr in variableMap.keys) {
            tmpStr = ("${vOper}1".toBigInteger() * variableMap[pStr]!!.toBigInteger()).toString()
            return Pair(true, tmpStr)
        }
        println("Unknown variable")
        return Pair(false, "")
    }

    private fun normalase(inStr: String): Boolean {
        val setOper = setOf("(",")","*","/")
        var str = inStr.replace("(\\()([+-]?([\\d\\w]+))(\\))".toRegex(), "$1 $2 $4")
        str = str.replace("([()])".toRegex(), " $1 ")
        str = str.replace("([\\d\\w])([/*+-]+)(\\s\\()".toRegex(), "$1 $2 $3")
        str = str.replace("(\\)\\s)([/*+-]+)([\\d\\w])".toRegex(), "$1 $2 $3")
        str = str.replace("([\\d\\w])([/*+-]+)([\\d\\w])".toRegex(), "$1 $2 $3")
        str = str.replace("([\\d\\w])([/*+-]+)([\\d\\w])".toRegex(), "$1 $2 $3")
        arrInp = str.trim().split("\\s+".toRegex()).toMutableList()
        for (i in arrInp.indices) {
//            println(arrInp[i])
            when {
                arrInp[i] in setOper -> continue
                arrInp[i].matches("[+-]+".toRegex()) -> arrInp[i] = getOperator(arrInp[i])
                arrInp[i].matches("[+-]?\\d+".toRegex()) -> arrInp[i] = arrInp[i].toBigInteger().toString()
                arrInp[i].matches("[+-]?\\w+".toRegex()) -> {
                    val p = getVariable(arrInp[i])
                    if (p.first) arrInp[i] = p.second else return false
                }
                else -> return false
            }
        }
        return true
    }

    private fun makeArrOPN(): Boolean{
        val setPlusMinus = setOf("+", "-")
        val setMultDiv = setOf("*","/")
        arrOPN.clear()
        arrOperator.clear()
        for (a in arrInp) {
            when(a) {
                "(" -> arrOperator.add(a)
                ")" -> {
                    if (arrOperator.isEmpty()) return false
                    val vIndLast = arrOperator.lastIndexOf("(")
                    if ( vIndLast == -1) return false
                    while (arrOperator.lastIndex > vIndLast) {
                        arrOPN.add(arrOperator.removeLast())
                    }
                    arrOperator.removeLast()
                }
                in setMultDiv -> {
                    if (arrOperator.isEmpty()) arrOperator.add(a)
                    else if (arrOperator[arrOperator.lastIndex] in setMultDiv) {
                        arrOPN.add(arrOperator.removeLast())
                        arrOperator.add(a)
                    }
                    else if (arrOperator[arrOperator.lastIndex] in setPlusMinus || arrOperator[arrOperator.lastIndex] == "(") {
                        arrOperator.add(a)
                    }
                }
                in setPlusMinus -> {
                    if (arrOperator.isEmpty()) arrOperator.add(a)
                    else if (arrOperator[arrOperator.lastIndex] in setMultDiv || arrOperator[arrOperator.lastIndex] in setPlusMinus) {
                        arrOPN.add(arrOperator.removeLast())
                        arrOperator.add(a)
                    } else if (arrOperator[arrOperator.lastIndex] == "(") {
                        arrOperator.add(a)
                    }
                }
                else -> arrOPN.add(a)
            }
        }
        if (arrOperator.contains("(")) return false
        while (arrOperator.isNotEmpty()) arrOPN.add(arrOperator.removeLast())
        return true
    }

    private fun calcOPN(pStr: String): String{
        var indOper = 0
        var vOper: String
        var vOperand1: BigInteger
        var vOperand2: BigInteger
        val setPlusMinusMultDev = setOf("+", "-", "*","/")
        if (!normalase(pStr)) return "Invalid expression"
        if (!makeArrOPN()) return "Invalid expression"
        while (arrOPN.size > 1) {
            for (i in arrOPN.indices) {
                if (arrOPN[i] in setPlusMinusMultDev) {
                    indOper = i
                    break
                }
            }
            vOper = arrOPN.removeAt(indOper)
            vOperand2 = arrOPN.removeAt(indOper - 1).toBigInteger()
            vOperand1 = arrOPN[indOper - 2].toBigInteger()
            when (vOper) {
                "+" -> vOperand1 += vOperand2
                "-" -> vOperand1 -= vOperand2
                "*" -> vOperand1 *= vOperand2
                "/" -> vOperand1 /= vOperand2
            }
            arrOPN[indOper - 2] = vOperand1.toString()
        }
        return arrOPN[0]
    }

    fun run(inStr: String) {
        if (inStr.matches(".*\\w\\s+\\w.*".toRegex())) {
            println("Invalid expression")
            return
        }
        val str = inStr.replace("\\s+".toRegex(), "")
        when {
            str == "" -> return
            str.matches("[A-z]+=[+-]?\\d+".toRegex()) -> setVariable(str)
            str.matches("[A-z]+=[+-]?[A-z]+".toRegex()) -> setVariable(str)
            str.matches("[A-z]+".toRegex()) -> {
                val getV = getVariable(str)
                if (getV.first) println(getV.second)
            }
            str.matches("[+-]?\\d+".toRegex()) -> println(str)
            str.matches(".*=.*".toRegex()) -> println("Invalid assignment")
            str.matches("/[A-z]+".toRegex()) -> {
                when (str) {
                    "/exit" -> flEnd = true
                    "/help" -> println("The program calculates the sum of numbers")
                    else -> println("Unknown command")
                }
            }
            str.matches("[(+-]?\\w+([/*()+-]+[+-]?\\w+)*\\)?".toRegex()) -> println(calcOPN(str))
            else -> println("Invalid expression")
        }
    }

    fun checkEnd() = flEnd
}

fun main() {
    val objCalc = CalculatorOPN()
    do {
        objCalc.run(readLine().toString())
    } while (!objCalc.checkEnd())
    println("Bye!")
}