import java.io.File

fun increasingSubsequence(nums: IntArray): Results {
    val n = nums.size
    val best = IntArray(n) { 1 }
    val next = IntArray(n) { -1 }
    var comparisons = 0
    var moviments = 0

    for (i in n - 2 downTo 0) {
        for (j in i + 1 until n) {
            comparisons += 2 // conta a comparação nums[j] > nums[i] e 1 + best[j] > best[i]
            if (nums[j] > nums[i] && 1 + best[j] > best[i]) {
                best[i] = 1 + best[j]
                next[i] = j
                moviments += 2
            }
        }
    }

    return Results(best, next, comparisons, moviments)
}

data class Results( // apenas a formatação da saída
    val best: IntArray,
    val next: IntArray,
    val comparisons: Int,
    val moviments: Int
)

fun greatSubsequence(nums: IntArray, best: IntArray, next: IntArray): IntArray {
    val start = best.indices.maxByOrNull { best[it] } ?: -1 // vai buscar a sequência ótima pelo índice de início
    val subsequence = mutableListOf<Int>()
    var i = start
    while (i != -1) {
        subsequence.add(nums[i])
        i = next[i]
    }
    return subsequence.toIntArray()
}

fun randomSequence(len: Int): IntArray { // gera uma sequência aleatória
    val length = mutableListOf<Int>()
    for (i in 0..len) {
        length.add((0..len).random())
    }
    return length.toIntArray()
}

fun main() {
    val file = File("resultadosPD.txt")
    file.writeText(String.format("%-10s %-15s %-15s\n", "Tamanho", "Comparações", "Movimentos"))

    var nums = intArrayOf(32, 19, 32, 17, 31, 43, 30, 29, 54, 16, 28, 66, 15, 41, 65, 14, 50)
    var results = increasingSubsequence(nums)
    var subsequence = greatSubsequence(nums, results.best, results.next)

    println("==== Sequência do enunciado ====")
    println("Sequência: ${nums.joinToString(", ")}")
    println("best[i]: ${results.best.joinToString(", ")}")
    println("Subsequência ótima: ${subsequence.joinToString(", ")}")
    println("Qtde comparações: ${results.comparisons}")
    println("Qtde movimentos: ${results.moviments}")
    println()

    file.appendText(String.format("%-10d %-15d %-15d\n", nums.size, results.comparisons, results.moviments))

    val length = intArrayOf(20, 25, 50, 100, 150, 200, 250, 500)
    for (i in length) {
        var randomSeq = randomSequence(i)
        var nums = randomSeq
        var results = increasingSubsequence(nums)
        var subsequence = greatSubsequence(nums, results.best, results.next)

        println("==== Sequência tamanho $i ====")
        println("Sequência: ${nums.joinToString(", ")}")
        println("best[i] ${results.best.joinToString(", ")}")
        println("Subsequência ótima: ${subsequence.joinToString(", ")}")
        println("Qtde comparações: ${results.comparisons}")
        println("Qtde movimentos: ${results.moviments}")
        println()

        file.appendText(String.format("%-10d %-15d %-15d\n", nums.size, results.comparisons, results.moviments))
    }

}
