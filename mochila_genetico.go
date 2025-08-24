package main

import (
	"bufio"
	"fmt"
	"math"
	"math/rand"
	"os"
	"strconv"
	"strings"
	"time"
)

type AlgoritmoGenetico struct {
	profits        []int
	pesos          []int
	capacidade     int
	populacao      [][]int
	tamPopulacao   int
	numGeracoes    int
	cont           int
	movimentoCont  int
}

func (ag *AlgoritmoGenetico) setPopulacaoInicial() {
	ag.populacao = make([][]int, ag.tamPopulacao)
	for i := 0; i < ag.tamPopulacao; i++ {
		cromossomo := make([]int, len(ag.profits))
		for j := 0; j < len(ag.profits); j++ {
			cromossomo[j] = rand.Intn(2)
		}
		ag.populacao[i] = cromossomo
	}
}

func (ag *AlgoritmoGenetico) calFitness() []int {
	ag.cont += ag.tamPopulacao
	somaLucro := make([]int, ag.tamPopulacao)
	somaPeso := make([]int, ag.tamPopulacao)
	popExcedida := []int{}

	for i, cromossomo := range ag.populacao {
		for j, gene := range cromossomo {
			somaLucro[i] += gene * ag.profits[j]
			somaPeso[i] += gene * ag.pesos[j]
		}
		if somaPeso[i] > ag.capacidade {
			popExcedida = append(popExcedida, i)
		}
	}

	for len(popExcedida) > int(0.3*float64(ag.tamPopulacao)) {
		ag.movimentoCont += len(popExcedida)
		for _, idx := range popExcedida {
			for k := 0; k < int(0.3*float64(len(ag.profits))); k++ {
				pos := rand.Intn(len(ag.profits))
				ag.populacao[idx][pos] = 0
			}
		}
		popExcedida = []int{}
		for i := range ag.populacao {
			somaPeso[i] = 0
			for j, gene := range ag.populacao[i] {
				somaPeso[i] += gene * ag.pesos[j]
			}
			if somaPeso[i] > ag.capacidade {
				popExcedida = append(popExcedida, i)
			}
		}
	}

	for _, idx := range popExcedida {
		somaLucro[idx] = 0
	}

	return somaLucro
}

func (ag *AlgoritmoGenetico) selection(fitness []int, numPais int) [][]int {
	novaGeracao := make([][]int, numPais)
	fitnessCopy := append([]int(nil), fitness...)
	for i := 0; i < numPais; i++ {
		maxIdx := maxIndex(fitnessCopy)
		novaGeracao[i] = append([]int(nil), ag.populacao[maxIdx]...)
		fitnessCopy[maxIdx] = -1
	}
	return novaGeracao
}

func maxIndex(slice []int) int {
	maxVal := math.MinInt
	maxIdx := -1
	for i, v := range slice {
		if v > maxVal {
			maxVal = v
			maxIdx = i
		}
	}
	return maxIdx
}

func (ag *AlgoritmoGenetico) crossover(pais [][]int, numOffsprings int) [][]int {
	offsprings := [][]int{}
	crossoverPoint := len(pais[0]) / 2
	crossoverRate := 0.8
	i := 0
	for len(offsprings) < numOffsprings {
		if rand.Float64() > crossoverRate {
			i++
			continue
		}
		parent1 := pais[i%len(pais)]
		parent2 := pais[(i+1)%len(pais)]
		filho := append([]int(nil), parent1[:crossoverPoint]...)
		filho = append(filho, parent2[crossoverPoint:]...)
		offsprings = append(offsprings, filho)
		i++
	}
	ag.movimentoCont += len(offsprings)
	return offsprings
}

func (ag *AlgoritmoGenetico) mutacao(offsprings [][]int) [][]int {
	mutants := make([][]int, len(offsprings))
	mutacaoRate := 0.6
	for i, filho := range offsprings {
		mutants[i] = append([]int(nil), filho...)
		if rand.Float64() <= mutacaoRate {
			pos := rand.Intn(len(filho))
			if mutants[i][pos] == 0 {
				mutants[i][pos] = 1
			} else {
				mutants[i][pos] = 0
			}
		}
	}
	ag.movimentoCont += len(mutants)
	return mutants
}

func (ag *AlgoritmoGenetico) solve() map[string]interface{} {
	start := time.Now()
	ag.setPopulacaoInicial()
	numPais := ag.tamPopulacao / 2
	numOffsprings := ag.tamPopulacao - numPais

	for g := 0; g < ag.numGeracoes; g++ {
		fitness := ag.calFitness()
		novaGeracao := ag.selection(fitness, numPais)
		offsprings := ag.crossover(novaGeracao, numOffsprings)
		mutants := ag.mutacao(offsprings)

		copy(ag.populacao[:numPais], novaGeracao)
		copy(ag.populacao[numPais:], mutants)
	}

	fitnessFinal := ag.calFitness()
	melhorIdx := maxIndex(fitnessFinal)
	resultado := ag.populacao[melhorIdx]

	valorMax := 0
	pesoTotal := 0
	itens := []int{}
	for i, gene := range resultado {
		if gene == 1 {
			itens = append(itens, i)
			valorMax += ag.profits[i]
			pesoTotal += ag.pesos[i]
		}
	}

	return map[string]interface{}{
		"itens_selecionados": itens,
		"valor_maximo":       valorMax,
		"peso_total":         pesoTotal,
		"cont":               ag.cont,
		"movimento_cont":     ag.movimentoCont,
		"tempo_execucao":     time.Since(start).Seconds(),
	}
}

func loadKnapsackData(caminho string) ([]int, []int, int) {
	file, err := os.Open(caminho)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	scanner.Scan()
	header := strings.Fields(scanner.Text())
	n, _ := strconv.Atoi(header[0])
	capacidade, _ := strconv.Atoi(header[1])

	profits := make([]int, 0, n)
	pesos := make([]int, 0, n)

	for scanner.Scan() {
		parts := strings.Fields(scanner.Text())
		if len(parts) == 2 {
			p, _ := strconv.Atoi(parts[0])
			w, _ := strconv.Atoi(parts[1])
			profits = append(profits, p)
			pesos = append(pesos, w)
		}
	}

	return profits, pesos, capacidade
}

func main() {
	rand.Seed(time.Now().UnixNano())

	caminhoArquivo := "knapPI_1_500_1000_1"
	profits, pesos, capacidade := loadKnapsackData(caminhoArquivo)

	ag := AlgoritmoGenetico{
		profits:       profits,
		pesos:         pesos,
		capacidade:    capacidade,
		tamPopulacao:  50,
		numGeracoes:   200,
		cont:          0,
		movimentoCont: 0,
	}

	result := ag.solve()

	fmt.Println("Valor total na mochila:", result["valor_maximo"])
	fmt.Println("Número de comparações:", result["cont"])
	fmt.Println("Número de movimentações:", result["movimento_cont"])
	fmt.Printf("Tempo de execução (s): %.4f\n", result["tempo_execucao"])
}
