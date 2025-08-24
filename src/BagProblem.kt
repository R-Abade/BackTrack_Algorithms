class BagProblem {
    // Solução Gulosa (rápida, mas não garante ótimo para mochila 0/1)
    fun greedySolution(capacity: Int, weights: IntArray, values: IntArray): Pair<Int, IntArray> {
        val n = weights.size
        val selected = IntArray(n)
        var remainingCapacity = capacity
        var totalValue = 0

        // Ordena os itens pela razão valor/peso (decrescente)
        val items = (0 until n).sortedByDescending { i -> values[i].toDouble() / weights[i] }

        for (i in items) {
            if (weights[i] <= remainingCapacity) {
                selected[i] = 1
                totalValue += values[i]
                remainingCapacity -= weights[i]
            }
        }

        return Pair(totalValue, selected)
    }

    // Solução por Programação Dinâmica (ótima)
    fun dpSolution(capacity: Int, weights: IntArray, values: IntArray): Pair<Int, IntArray> {
        val n = weights.size
        val dp = Array(n + 1) { IntArray(capacity + 1) }
        val selected = IntArray(n)

        // Preenche a tabela DP
        for (i in 1..n) {
            for (w in 1..capacity) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = maxOf(
                        dp[i - 1][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    )
                } else {
                    dp[i][w] = dp[i - 1][w]
                }
            }
        }

        // Recupera os itens selecionados
        var w = capacity
        for (i in n downTo 1) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected[i - 1] = 1
                w -= weights[i - 1]
            }
        }

        return Pair(dp[n][capacity], selected)
    }
}

fun main() {
    val problem = BagProblem()
    val weights = intArrayOf(1, 2, 3, 4, 5, 6)
    val values = intArrayOf(10, 20, 30, 40, 50, 60)
    val capacity = 10

    println("Itens: ${(1..weights.size).joinToString()}")
    println("Pesos: ${weights.joinToString()}")
    println("Valores: ${values.joinToString()}")
    println("Capacidade da mochila: $capacity")

    // Solução Gulosa
    val (greedyValue, greedySelected) = problem.greedySolution(capacity, weights, values)
    println("\n--- Solução Gulosa ---")
    println("Valor total: $greedyValue")
    println("Itens selecionados: ${greedySelected.withIndex().filter { it.value == 1 }.map { it.index + 1 }}")

    // Solução por Programação Dinâmica (Ótima)
    val (dpValue, dpSelected) = problem.dpSolution(capacity, weights, values)
    println("\n--- Solução DP (Ótima) ---")
    println("Valor total: $dpValue")
    println("Itens selecionados: ${dpSelected.withIndex().filter { it.value == 1 }.map { it.index + 1 }}")
}
