class HorseRide(val sizeX: Int, val sizeY: Int) {
    val tabuleiro = Array(sizeY) { IntArray(sizeX) { 0 } }

    private val moveX = intArrayOf(2, 1, -1, -2, -2, -1, 1, 2)
    private val moveY = intArrayOf(1, 2, 2, 1, -1, -2, -2, -1)

    fun tryNextMove(i: Int, x: Int, y: Int): Boolean {
        tabuleiro[y][x] = i

        if (i == sizeX * sizeY) return true

        // Lista de movimentos válidos
        val moves = mutableListOf<Pair<Int, Int>>()
        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                moves.add(Pair(nextX, nextY))
            }
        }

        // Ordena os movimentos pela heurística de Warnsdorff
        moves.sortBy { countNextMoves(it.first, it.second) }

        for ((nextX, nextY) in moves) {
            if (tryNextMove(i + 1, nextX, nextY)) return true
        }

        tabuleiro[y][x] = 0
        return false
    }

    // Conta quantos movimentos válidos a partir da posição (x,y)
    private fun countNextMoves(x: Int, y: Int): Int {
        var count = 0
        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                count++
            }
        }
        return count
    }

    fun printTabuleiro() {
        for (linha in tabuleiro) {
            println(linha.joinToString(" ") { it.toString().padStart(2, '0') })
        }
    }

    fun startGame() {
        print("Digite um número de entrada horizontal do tabuleiro: ")
        val row = readLine()!!.toInt()
        print("Digite um número de entrada vertical do tabuleiro: ")
        val column = readLine()!!.toInt()

        if (column in 0 until sizeX && row in 0 until sizeY) {
            if (tryNextMove(1, column, row)) {
                println("Solução encontrada:")
            } else {
                println("Não há solução a partir dessa posição.")
            }
            printTabuleiro()
        } else {
            println("Coordenadas fora do tabuleiro.")
        }
    }
}

fun main() {
    val game = HorseRide(8, 8)
    game.startGame()
}
