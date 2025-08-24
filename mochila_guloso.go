package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
	"time"
)

// Estrutura para armazenar valor e peso
type Item struct {
	valor int
	peso  int
}

func lerArquivo(caminho string) (int, int, []Item, error) {
	arquivo, err := os.Open(caminho)
	if err != nil {
		return 0, 0, nil, err
	}
	defer arquivo.Close()

	scanner := bufio.NewScanner(arquivo)

	// Lê primeira linha -> n e capacidade máxima
	scanner.Scan()
	primeiraLinha := strings.Fields(scanner.Text())
	n, _ := strconv.Atoi(primeiraLinha[0])
	capacidadeMax, _ := strconv.Atoi(primeiraLinha[1])

	itens := make([]Item, 0, n)

	contador := 0
	for scanner.Scan() {
		if contador >= n {
			break
		}
		partes := strings.Fields(scanner.Text())
		valor, _ := strconv.Atoi(partes[0])
		peso, _ := strconv.Atoi(partes[1])
		itens = append(itens, Item{valor: valor, peso: peso})
		contador++
	}

	return n, capacidadeMax, itens, nil
}

func algoritmoGuloso(n, capacidadeMax int, itens []Item) (int, int, int, float64) {
	comparacoes := 0
	movimentacoes := 0

	inicioTempo := time.Now()

	// movimentação inicial (criação lista)
	movimentacoes += n

	// Ordena por valor/peso decrescente
	sort.Slice(itens, func(i, j int) bool {
		// Evitar divisão por zero
		ratioI := float64(itens[i].valor) / float64(itens[i].peso)
		ratioJ := float64(itens[j].valor) / float64(itens[j].peso)
		return ratioI > ratioJ
	})
	movimentacoes += n

	valorMaximo := 0
	pesoTotal := 0

	for i := 0; i < n; i++ {
		valor := itens[i].valor
		peso := itens[i].peso
		comparacoes++
		if pesoTotal+peso <= capacidadeMax {
			pesoTotal += peso
			valorMaximo += valor
			movimentacoes++
		} else {
			movimentacoes++
		}
	}

	tempoExecucao := time.Since(inicioTempo).Seconds()

	return valorMaximo, comparacoes, movimentacoes, tempoExecucao
}

func main() {
	caminhoArquivo := "knapPI_1_500_1000_1"

	n, capacidadeMax, itens, err := lerArquivo(caminhoArquivo)
	if err != nil {
		fmt.Println("Erro ao ler arquivo:", err)
		return
	}

	valorMaximo, comparacoes, movimentacoes, tempoExecucao := algoritmoGuloso(n, capacidadeMax, itens)

	fmt.Printf("Valor total na mochila: %d\n", valorMaximo)
	fmt.Printf("Total de comparações:   %d\n", comparacoes)
	fmt.Printf("Total de movimentações: %d\n", movimentacoes)
	fmt.Printf("Tempo de execução:      %.6f segundos\n", tempoExecucao)
}
