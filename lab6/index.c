#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

const int ELEMENTS_PER_PROCESS = 5;
const int ROOT_RANK = 0;

/**
 * Create int array
 * 
 * @param {int} length - length of the array
 * @returns {int *} allocated int array
 **/
int *new_array(int length) {
  return (int *)malloc(length * sizeof(int));
}


/**
 * Create array filled with random ints
 * 
 * @param {int} length - length of the array
 * @returns {int *} craeted array
 **/
int *random_array(int length) {
  srand(time(0));
  
  int *arr = new_array(length);
  for (int i = 0; i < length; ++i) {
    arr[i] = rand() % 100;
  }
  
  return arr;
}

/**
 * Sum array elements in parallel using OpenMP
 * 
 * @param {int *} array
 * @param {int} length - array length
 * @returns {int} sum of array elements
 **/
int sum_in_parallel(int *array, int length) {
  int sum = 0;

  #pragma omp parallel for reduction(+: sum)
  for (int i = 0; i < length; ++i) {
    sum += array[i];
  }

  return sum;
}

int main(int argc, char **argv) {
  int rank, size;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD,&rank);
	MPI_Comm_size(MPI_COMM_WORLD,&size);

  /**
   * arr is sample array 
   **/
  int *arr;
  int arr_length = ELEMENTS_PER_PROCESS * size;

  /**
   * If it is root process then initialize sample array
   **/
  if (rank == ROOT_RANK) {
    arr = random_array(arr_length);
    for (int i = 0; i < arr_length; ++i) {
      printf("%d ", arr[i]);
    }
    printf("\n");
  }

  /**
   * MPI splits sample array into chunks for each process
   * In each process its chunk stores in sub_arr variable
   **/
  int *sub_arr = new_array(ELEMENTS_PER_PROCESS);
  
  /**
   * Split arr into chunks (sub_arr's) for each process
   * Each sub_arr will have length ELELEMTS_PER_PROCESS
   **/
  MPI_Scatter(arr, ELEMENTS_PER_PROCESS, MPI_INT,
    sub_arr, ELEMENTS_PER_PROCESS, MPI_INT, ROOT_RANK, MPI_COMM_WORLD);

  int sub_sum = sum_in_parallel(sub_arr, ELEMENTS_PER_PROCESS);
  printf("Rank %d, SubSum %d\n", rank, sub_sum);

  /**
   * Root process receives results into results array 
   **/
  int *results = NULL;
  if (rank == ROOT_RANK) {
    results = (int *)malloc(size * sizeof(int));
  }

  /**
   * Each process gathers 1 element from sub_sum array to a root rank
   **/
  MPI_Gather(&sub_sum, 1, MPI_INT,
    results, 1, MPI_INT, ROOT_RANK, MPI_COMM_WORLD);

  /**
   * Root process count sum of results
   **/
  int sum = 0;
  if (rank == ROOT_RANK) {
    for (int i = 0; i < size; ++i) {
      sum += results[i];
    }

    printf("Sum %d\n", sum);
  }

  MPI_Finalize();
  return 0;
}