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

data class Results( // serve para organizar o retorno da função
    val best: IntArray,
    val next: IntArray,
    val comparisons: Int,
    val moviments: Int
)

fun otimaSubsequence(nums: IntArray, best: IntArray, next: IntArray): IntArray {
    val start = best.indices.maxByOrNull { best[it] } ?: -1 // vai buscar a sequência ótima pelo índice de início
    val subsequence = mutableListOf<Int>()
    var i = start
    while(i != -1){
        subsequence.add(nums[i])
        i = next[i]

    }
    return subsequence.toIntArray()
}


fun main() {
    val nums = intArrayOf(32, 19, 32, 17, 31, 43, 30, 29, 54, 16, 28, 66, 15, 41, 65, 14, 50) // sequência do enunciado
    val results = increasingSubsequence(nums)
    val subsequence = otimaSubsequence(nums, results.best, results.next)

    println("num[i]: ${nums.joinToString(", ")}")
    println("best[i]: ${results.best.joinToString(", ")}")
    println("next[i]: ${results.next.joinToString(", ")}")
    println("Subsequência ótima:  ${subsequence.joinToString(", ")}")
    println("Qtde comparações: ${results.comparisons}")
    println("Qtde movimentos: ${results.moviments}")
}
