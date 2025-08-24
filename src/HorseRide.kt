class HorseRide(val sizeX: Int, val sizeY: Int) {
    val tabuleiro = Array(sizeY) { IntArray(sizeX) { 0 } }

    private val moveX = intArrayOf(2, 1, -1, -2, -2, -1, 1, 2)
    private val moveY = intArrayOf(1, 2, 2, 1, -1, -2, -2, -1)

    var comparisons = 0
    var moviments = 0
    var time: Long = 0

    fun resetBoard() {
        for (i in 0 until sizeY) {
            for (j in 0 until sizeX) {
                tabuleiro[i][j] = 0
            }
        }
        comparisons = 0
        moviments = 0
        time = 0
    }

    fun tryNextMove(i: Int, x: Int, y: Int): Boolean {
        tabuleiro[y][x] = i
        if (i > 1) {
            moviments++ // conta movimento só após sair da posição inicial
        }

        if (i == sizeX * sizeY) {
            return true
        }

        // lista um par (x,y) de movimentos válidos
        val moves = mutableListOf<Pair<Int, Int>>()
        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            comparisons++
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                moves.add(Pair(nextX, nextY))
            }
        }

        // ordena os movimentos por algorítmo de Warnsdorff
        moves.sortBy {
            countNextMoves(it.first, it.second)
        }

        for ((nextX, nextY) in moves) {
            if (tryNextMove(i + 1, nextX, nextY)) {
                return true
            }
        }

        tabuleiro[y][x] = 0
        return false
    }

    // a partir de determinada casa, conta os movimentos válidos
    private fun countNextMoves(x: Int, y: Int): Int {
        var count = 0
        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            comparisons++
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                count++
            }
        }
        return count
    }

    fun printTabuleiro() {
        for (linha in tabuleiro) {
            println(linha.joinToString(" ") {
                it.toString().padStart(2, '0') })
        }
    }

    fun solvePosition(startX: Int, startY: Int): Boolean { // inicia numa posição e vai contando o tempo
        resetBoard()
        val startTime = System.nanoTime()
        val result = tryNextMove(1, startX, startY)
        time = System.nanoTime() - startTime
        return result
    }

    fun startGame() {
        print("Digite um número de entrada horizontal do tabuleiro: ")
        val row = readLine()!!.toInt()
        print("Digite um número de entrada vertical do tabuleiro: ")
        val column = readLine()!!.toInt()

        if (column in 0 until sizeX && row in 0 until sizeY) {
            if (solvePosition(column, row)) {
                println("Solução encontrada:")
            }
            else {
                println("Não há solução a partir dessa posição.")
            }
            printTabuleiro()
            println("Movimentos: ${moviments}")
            println("Comparações: ${comparisons}")
            println("Tempo (ms): ${time/1_000_000.0}")
        } else {
            println("Coordenadas fora do tabuleiro.")
        }
    }

    // testa diferentes posições iniciais
    fun testAllPositions() {
        println("Posição\t\tSolução\t\tMovimentos\t\tComparações\t\tTempo(ms)")
        println("-------------------------------------------------------------------------------")

        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                val solutionExist = solvePosition(x, y)
                var status = ""
                if (solutionExist){
                    status = "Sim"
                } else {
                    status = "Não"
                }
                println("($x,$y)\t\t${String.format("%-5s", status)}\t\t${String.format("%-9d", moviments)}\t\t${String.format("%-11d", comparisons)}\t\t${String.format("%.5f", time/1_000_000.0)}")
            }
        }
    }

    // gera dados para gráficos variando n
    fun generateInfos() {
        println("n\t\tMovimentos\t\tComparações\t\tTempo(ms)")
        println("-------------------------------------------------------")
        for (n in 5..8) {
            val testGame = HorseRide(n, n)
            val success = testGame.solvePosition(0, 0)
            if (success) {
                println("$n\t\t${String.format("%-9d", testGame.moviments)}\t\t${String.format("%-11d", testGame.comparisons)}\t\t${String.format("%.5f", testGame.time/1_000_000.0)}")
            } else {
                println("$n\t\t${String.format("%-9s", "-")}\t\t${String.format("%-11s", "-")}\t\t-")
            }
        }
    }
}

fun main() {
    val game = HorseRide(8, 8)
    game.startGame()

    println("==============================================================")
    game.testAllPositions()
    println("==============================================================")

    println("==============================================================")
    println("Variando n:")
    game.generateInfos()
}