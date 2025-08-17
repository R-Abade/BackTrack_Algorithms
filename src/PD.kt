fun longestSubsequence(nums: IntArray): IntArray {
    val n = nums.size
    val best = IntArray(n) { 1 } // Inicializa todos os elementos com 1

    // Preenchimento de trás para frente
    for (i in n - 2 downTo 0) {
        for (j in i + 1 until n) {
            if (nums[j] > nums[i] && 1 + best[j] > best[i]) {
                best[i] = 1 + best[j]
            }
        }
    }

    return best
}

fun main() {
    val nums = intArrayOf(7, 6, 10, 3, 4, 1, 8, 9, 5, 2)
    val best = longestSubsequence(nums)

    println("num[i]: ${nums.joinToString(", ")}")
    println("best[i]: ${best.joinToString(", ")}")
}
