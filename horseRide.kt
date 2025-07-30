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

        // Condição de parada: se visitamos todas as casas
        if (i == sizeX * sizeY) {
            return true
        }

        do {
            nextX = x + moveX[k]
            nextY = y + moveY[k]

            if (nextX in 0 until sizeX && nextY in 0 until sizeY && tabuleiro[nextY][nextX] == 0) {
                nice = tryNextMove(i + 1, nextX, nextY)
                if (!nice) {
                    tabuleiro[nextY][nextX] = 0
                }
            }
            k++
        } while (!nice && k < 8)

        if (!nice) {
            tabuleiro[y][x] = 0
        }

        return nice
    }

    fun printTabuleiro() {
        for (linha in tabuleiro) {
            println(linha.joinToString(" ") { it.toString().padStart(2, '0') })
        }
    }

    fun startGame(){
        print("Digite um número de entrada horizontal do tabuleiro: ")
        var row = readLine()!!.toInt()
        print("Digite um número de entrada vertical do tabuleiro: ")
        var column = readLine()!!.toInt()

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

fun main(){
    var game = horseRide(8,8)
    game.startGame()
}