package Prova_3

class horseRide(val sizeX: Int, val sizeY: Int) {
    val tabuleiro = Array(sizeY) { IntArray(sizeX) { 0 } }

    fun tryNextMove(i: Int, x: Int, y: Int): Boolean {
        val moveX = intArrayOf(2, 1, -1, -2, -2, -1, 1, 2)
        val moveY = intArrayOf(1, 2, 2, 1, -1, -2, -2, -1)
        var nextX: Int
        var nextY: Int
        var k: Int = 0
        var nice: Boolean = false

        tabuleiro[y][x] = i

        do {
            nextX = x + moveX[k]
            nextY = y + moveY[k]

            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                if (i < sizeX * sizeY) {
                    nice = tryNextMove(i + 1, nextX, nextY)
                    if (!nice) {
                        tabuleiro[nextY][nextX] = 0
                    }
                } else {
                    nice = true
                }
            }
            k++
        } while (!nice && k < 8)

        return nice
    }

    fun printTabuleiro() {
        for (linha in tabuleiro) {
            println(linha.joinToString(" ") { it.toString().padStart(2, '0') })
        }
    }
}
