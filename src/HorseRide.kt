import java.io.File

class HorseRideWarnsdorff(val sizeX: Int, val sizeY: Int) {
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
            moviments++
        }

        if (i == sizeX * sizeY) {
            return true
        }

        val moves = mutableListOf<Pair<Int, Int>>()
        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            comparisons++
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                moves.add(Pair(nextX, nextY))
            }
        }

        // ordena os movimentos por algoritmo de Warnsdorff
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

    fun solvePosition(startX: Int, startY: Int): Boolean { // começa numa posição e conta o tempo de resolução
        resetBoard()
        val startTime = System.nanoTime()
        val result = tryNextMove(1, startX, startY)
        time = System.nanoTime() - startTime
        return result
    }

    // salva resultados em arquivo
    fun testAllPositions(filename: String) {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("Posição\t\tSolução\t\tMovimentos\t\tComparações\t\tTempo(ms)")
            out.println("-------------------------------------------------------------------------------")

            for (y in 0 until sizeY) {
                for (x in 0 until sizeX) {
                    val solutionExist = solvePosition(x, y)
                    var status = ""
                    if (solutionExist){
                        status = "Sim"
                    } else {
                        status = "Não"
                    }
                    out.println("($x,$y)\t\t${String.format("%-5s", status)}\t\t${String.format("%-9d", moviments)}\t\t${String.format("%-11d", comparisons)}\t\t${String.format("%.5f", time/1_000_000.0)}")
                }
            }
        }
    }

    fun generateInfos(filename: String) {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("n\t\tMovimentos\t\tComparações\t\tTempo(ms)")
            out.println("-------------------------------------------------------")
            for (n in 5..10) {
                val testGame = HorseRideWarnsdorff(n, n)
                val success = testGame.solvePosition(0, 0)
                if (success) {
                    out.println("$n\t\t${String.format("%-9d", testGame.moviments)}\t\t${String.format("%-11d", testGame.comparisons)}\t\t${String.format("%.5f", testGame.time/1_000_000.0)}")
                } else {
                    out.println("$n\t\t${String.format("%-9s", "-")}\t\t${String.format("%-11s", "-")}\t\t-")
                }
            }
        }
    }

    fun startGame() {
        print("Digite um número de entrada horizontal do tabuleiro (0-${sizeX-1}): ")
        val row = readLine()!!.toInt()
        print("Digite um número de entrada vertical do tabuleiro (0-${sizeY-1}): ")
        val column = readLine()!!.toInt()

        if (column in 0 until sizeX && row in 0 until sizeY) {
            if (solvePosition(column, row)) {
                println("Solução encontrada:")
            } else {
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

    private fun printTabuleiro() {
        println("\nTabuleiro:")
        for (i in 0 until sizeY) {
            for (j in 0 until sizeX) {
                print("${String.format("%3d", tabuleiro[i][j])} ")
            }
            println()
        }
        println()
    }
}

// mesmo código, só retirando o algorítmo de Wansdorff e adicionando limite de tempo
class HorseRideNoWarnsdorff(val sizeX: Int, val sizeY: Int) {
    val tabuleiro = Array(sizeY) { IntArray(sizeX) { 0 } }
    private val moveX = intArrayOf(2, 1, -1, -2, -2, -1, 1, 2)
    private val moveY = intArrayOf(1, 2, 2, 1, -1, -2, -2, -1)
    var comparisons = 0
    var moviments = 0
    var time: Long = 0
    private var timeout: Long = Long.MAX_VALUE
    fun resetBoard() {
        for (i in 0 until sizeY) {
            for (j in 0 until sizeX) {
                tabuleiro[i][j] = 0
            }
        }
        comparisons = 0
        moviments = 0
        time = 0
        timeout = Long.MAX_VALUE
    }
    // define um limite de tempo
    fun setTimeLimitMillis(limitMs: Long) {
        if (limitMs > 0) {
            timeout = System.nanoTime() + limitMs * 1_000_000
        } else {
            timeout = Long.MAX_VALUE
        }
    }

    fun tryNextMove(i: Int, x: Int, y: Int): Boolean {
        // cancela se estourar o tempo
        if (System.nanoTime() > timeout) {
            return false
        }
        tabuleiro[y][x] = i
        if (i > 1) {
            moviments++
        }
        if (i == sizeX * sizeY) {
            return true
        }

        for (k in 0 until 8) {
            val nextX = x + moveX[k]
            val nextY = y + moveY[k]
            comparisons++
            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                if (tryNextMove(i + 1, nextX, nextY)) {
                    return true
                }
                // checa tempo entre recursões também
                if (System.nanoTime() > timeout) {
                    tabuleiro[y][x] = 0
                    return false
                }
            }
        }
        tabuleiro[y][x] = 0
        return false
    }

    fun solvePosition(startX: Int, startY: Int): Boolean { // inicia numa posição e vai contando o tempo
        val startTime = System.nanoTime()
        val result = tryNextMove(1, startX, startY)
        time = System.nanoTime() - startTime
        return result
    }

    // testa diferentes posições iniciais e salva resultados em arquivo
    fun testAllPositions(filename: String) {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("Posição\t\tSolução\t\tMovimentos\t\tComparações\t\tTempo(ms)")
            out.println("-------------------------------------------------------------------------------")

            for (y in 0 until sizeY) {
                for (x in 0 until sizeX) {
                    // limite de tempo por posição (ajuste conforme desejar)
                    resetBoard()
                    setTimeLimitMillis(200) // 200 ms por posição no 8x8 costuma fechar rápido a rodada

                    val solutionExist = solvePosition(x, y)
                    var status = ""
                    if (solutionExist){
                        status = "Sim"
                    } else {
                        status = "Não"
                    }
                    out.println("($x,$y)\t\t${String.format("%-5s", status)}\t\t${String.format("%-9d", moviments)}\t\t${String.format("%-11d", comparisons)}\t\t${String.format("%.5f", time/1_000_000.0)}")
                    out.flush()
                }
            }
        }
    }

    // gera dados para gráficos variando n e salva em arquivo
    fun generateInfos(filename: String) {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("n\t\tMovimentos\t\tComparações\t\tTempo(ms)")
            out.println("-------------------------------------------------------")
            for (n in 5..8) {
                val testGame = HorseRideNoWarnsdorff(n, n)
                testGame.setTimeLimitMillis(3000) // 3 s por n
                val success = testGame.solvePosition(0, 0)
                if (success) {
                    out.println("$n\t\t${String.format("%-9d", testGame.moviments)}\t\t${String.format("%-11d", testGame.comparisons)}\t\t${String.format("%.5f", testGame.time/1_000_000.0)}")
                } else {
                    out.println("$n\t\t${String.format("%-9s", "-")}\t\t${String.format("%-11s", "-")}\t\t-")
                }
                out.flush()
            }
        }
    }
}

fun main() {
    println("Passeio do Cavalo com Warnsdorff")
    val game1 = HorseRideWarnsdorff(8, 8)
    game1.startGame()
    game1.testAllPositions("warnsdorff_todas_posicoes.txt")
    game1.generateInfos("warnsdorff_variando_n.txt")

    println("=====================================================================")
    println("Passeio do Cavalo sem Warnsdorff")
    val game2 = HorseRideNoWarnsdorff(8, 8)
    game2.testAllPositions("semWarnsdorff_todas_posicoes.txt")
    game2.generateInfos("semWarnsdorff_variando_n.txt")

}