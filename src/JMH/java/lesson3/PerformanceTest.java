package lesson3;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PerformanceTest {

    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            RandomWords fullWordList = new RandomWords();
            wordList = fullWordList.createList(1000);
        }

        List<String> wordList;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int[][] testComputeLevenshtein2(MyState state) {
        List<String> wordList = state.wordList;
        final int LIST_SIZE = wordList.size();
        int[][] distances = new int[LIST_SIZE][LIST_SIZE];
        IntStream.range(0, LIST_SIZE)
                .parallel()
                .forEach(i -> IntStream.range(i, LIST_SIZE)
                        .parallel()
                        .forEach(j -> {
                            distances[i][j] = Levenshtein.lev(wordList.get(i), wordList.get(j));
                            distances[j][i] = distances[i][j];
                        }));
        return distances;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int[][] testComputeLevenshtein3(MyState state) {
        List<String> wordList = state.wordList;
        final int LIST_SIZE = wordList.size();
        int[][] distances = new int[LIST_SIZE][LIST_SIZE];
        IntStream.range(0, LIST_SIZE).parallel()
                .mapToObj(int1 -> IntStream.range(int1, LIST_SIZE).mapToObj(int2 -> Arrays.asList(int1, int2)))
                .flatMap(lists -> lists)
                .forEach(lists -> {
                    int i = lists.get(0);
                    int j = lists.get(1);
                    distances[i][j] = Levenshtein.lev(wordList.get(i), wordList.get(j));
                    distances[j][i] = distances[i][j];
                });
        return distances;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int[][] testComputeLevenshtein4(MyState state) {
        List<String> wordList = state.wordList;
        final int LIST_SIZE = wordList.size();
        return IntStream.range(0, LIST_SIZE).parallel()
                .mapToObj(int1 -> IntStream.range(0, LIST_SIZE).mapToObj(int2 -> new Pair(int1, int2)))
                .flatMap(Function.identity())
                .collect(() -> new int[LIST_SIZE][LIST_SIZE],
                        (intArray, pair) ->
                                intArray[pair.getFirstIndex()][pair.getSecondIndex()] = Levenshtein.lev(wordList.get(pair.getFirstIndex()), wordList.get(pair.getSecondIndex())),
                        (a, b) -> {
                            for (int i = 0; i < a.length; i++) {
                                for (int j = 0; j < b.length; j++) {
                                    a[i][j] = a[i][j] + b[i][j];
                                }
                            }
                        });
    }
}
