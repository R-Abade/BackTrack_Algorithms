package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

func progDinamica(n, capacidadeMaxima int, valores, pesos []int) (int, float64, int, int) {
	inicioTempo := time.Now()

	comparacoes := 0
	movimentacoes := 0

	tabelaMelhores := make([][]int, n+1)
	for i := range tabelaMelhores {
		tabelaMelhores[i] = make([]int, capacidadeMaxima+1)
	}

	for i := 1; i <= n; i++ {
		for capacidadeAtual := 0; capacidadeAtual <= capacidadeMaxima; capacidadeAtual++ {

			melhor := tabelaMelhores[i-1][capacidadeAtual]
			movimentacoes++

			var proximoValor int
			if pesos[i-1] <= capacidadeAtual {
				proximoValor = tabelaMelhores[i-1][capacidadeAtual-pesos[i-1]] + valores[i-1]
				movimentacoes += 2
			} else {
				proximoValor = 0
				movimentacoes++
			}

			comparacoes++
			if melhor > proximoValor {
				tabelaMelhores[i][capacidadeAtual] = melhor
			} else {
				tabelaMelhores[i][capacidadeAtual] = proximoValor
			}
			movimentacoes++
		}
	}

	tempoExecucao := time.Since(inicioTempo).Seconds()

	return tabelaMelhores[n][capacidadeMaxima], tempoExecucao, comparacoes, movimentacoes
}

func lerArquivoEntrada(nomeArquivo string) (int, int, []int, []int, error) {
	arquivo, err := os.Open(nomeArquivo)
	if err != nil {
		return 0, 0, nil, nil, err
	}
	defer arquivo.Close()

	scanner := bufio.NewScanner(arquivo)

	scanner.Scan()
	linha := scanner.Text()
	primeiraLinha := strings.Fields(linha)
	n, _ := strconv.Atoi(primeiraLinha[0])
	capacidadeMaxima, _ := strconv.Atoi(primeiraLinha[1])

	valores := []int{}
	pesos := []int{}

	contador := 0
	for scanner.Scan() {
		if contador >= n {
			break
		}
		partes := strings.Fields(scanner.Text())
		valor, _ := strconv.Atoi(partes[0])
		peso, _ := strconv.Atoi(partes[1])
		valores = append(valores, valor)
		pesos = append(pesos, peso)
		contador++
	}

	return n, capacidadeMaxima, valores, pesos, nil
}

func main() {
	caminhoArquivo := "knapPI_1_500_1000_1"

	n, capacidadeMaxima, valores, pesos, err := lerArquivoEntrada(caminhoArquivo)
	if err != nil {
		fmt.Println("Erro ao ler arquivo:", err)
		return
	}

	valorMaximo, tempoExecucao, comparacoes, movimentacoes :=
		progDinamica(n, capacidadeMaxima, valores, pesos)

	fmt.Printf("Valor total na mochila: %d\n", valorMaximo)
	fmt.Printf("Total de comparações:   %d\n", comparacoes)
	fmt.Printf("Total de movimentações: %d\n", movimentacoes)
	fmt.Printf("Tempo de execução:      %.6f segundos\n", tempoExecucao)
}
