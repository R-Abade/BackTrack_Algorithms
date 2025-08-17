import kotlin.math.roundToInt

class CoinsProblem {
    var trade: Int = 0
    var coins: FloatArray = floatArrayOf()

    fun trade(trade: Float, coins: FloatArray): Pair<Int, IntArray> {
        var totalCoins: Int = 0
        var qtdePerCoin: IntArray = IntArray(coins.size)

        // converte moedas e trade para centavos
        val coinsCent: IntArray = IntArray(coins.size) { i -> (coins[i] * 100).roundToInt() }
        var tradeRestante = (trade * 100).roundToInt()

        // percorre do maior para o menor valor
        var i: Int = coins.size - 1
        while (i >= 0) {
            qtdePerCoin[i] = tradeRestante / coinsCent[i]
            tradeRestante -= qtdePerCoin[i] * coinsCent[i]
            totalCoins += qtdePerCoin[i]
            i--
        }

        var j: Int = 0
        while (j < coins.size) {
            println("Moeda ${coins[j]}: ${qtdePerCoin[j]} unidade(s)")
            j++
        }

        return Pair(totalCoins, qtdePerCoin)
    }
}

fun main() {
    var problem = CoinsProblem()
    var coins = floatArrayOf(0.01f, 0.05f, 0.10f, 0.25f, 0.5f, 1.0f)
    print("Digite o valor do troco: ")
    var tradeValue = readLine()!!.toFloat()
    val result = problem.trade(tradeValue, coins)

    println("Total de moedas: ${result.first}")
    println("Quantidade por moeda: ${result.second.joinToString()}")
}
