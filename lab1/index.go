package main

import (
	"fmt"
	"math/rand"
	"os"
	"strconv"
	"time"
)

const (
	maxElementValue = 1000
)

func createMatrix(n, m int) [][]int64 {
	matrix := make([][]int64, n)

	rand.Seed(time.Now().UnixNano())

	for i := 0; i < n; i++ {
		matrix[i] = make([]int64, m)
		for j := 0; j < m; j++ {
			matrix[i][j] = rand.Int63n(maxElementValue)
		}
	}

	return matrix
}

func worker(jobs <-chan []int64, results chan<- int64) {
	for row := range jobs {
		var subsum int64 = 0
		for _, value := range row {
			subsum += value
		}
		results <- subsum
	}
}

func parallel(matrix [][]int64, n, m, threadsCount int) (int64, time.Duration) {
	start := time.Now()

	jobs := make(chan []int64, n)
	results := make(chan int64, n)

	for i := 0; i < threadsCount; i++ {
		go worker(jobs, results)
	}

	for _, row := range matrix {
		jobs <- row
	}
	close(jobs)

	var sum int64 = 0

	for j := 0; j < n; j++ {
		sum += <-results
	}

	return sum, time.Since(start)
}

func sequential(matrix [][]int64, n, m int) (int64, time.Duration) {
	start := time.Now()

	var sum int64 = 0
	for i := 0; i < n; i++ {
		row := matrix[i]
		for _, value := range row {
			sum += value
		}
	}

	return sum, time.Since(start)
}

func main() {
	n, err := strconv.Atoi(os.Args[1])

	if err != nil {
		fmt.Println("N must be integer")
		os.Exit(1)
	}

	m, err := strconv.Atoi(os.Args[2])

	if err != nil {
		fmt.Println("M must be integer")
		os.Exit(1)
	}

	threadsCount, err := strconv.Atoi(os.Args[3])

	if err != nil {
		fmt.Println("THREADS_COUNT must be integer")
		os.Exit(1)
	}

	fmt.Printf("Rows: %v, Columns: %v\nThreads count: %v\n", n, m, threadsCount)

	matrix := createMatrix(n, m)

	sequentialResult, sequentialTime := sequential(matrix, n, m)
	parallelResult, parallelTime := parallel(matrix, n, m, threadsCount)

	acceleration := float32(sequentialTime) / float32(parallelTime)
	efficiency := acceleration / float32(threadsCount)

	fmt.Printf("Sequential result: %v, Execution time: %v\n", sequentialResult, sequentialTime)
	fmt.Printf("Parallel result: %v, Execution time: %v\n", parallelResult, parallelTime)

	fmt.Printf("Acceleration: %v\n", acceleration)
	fmt.Printf("Efficiency: %v\n", efficiency)
}
