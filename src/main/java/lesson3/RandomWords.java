/**
 * Copyright © 2014, Oracle and/or its affiliates. All rights reserved.
 * <p>
 * JDK 8 MOOC Lesson 3 homework
 */
package lesson3;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class to generate a list of random words
 *
 * @author Simon Ritter (@speakjava)
 */
public class RandomWords {
    private List<String> sourceWords = Collections.EMPTY_LIST;

    /**
     * Constructor
     *
     * @throws IOException If the source words file cannot be read
     */
    public RandomWords() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Nadezhda_Petelimova\\jmh\\src\\JMH\\resources\\lesson3\\words"))) {
            sourceWords = reader.lines().collect(Collectors.toList());
            System.out.println("Loaded " + sourceWords.size() + " words");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a list of a given size containing random words
     *
     * @param listSize The size of the list to create
     * @return The created list
     */
    public List<String> createList(int listSize) {
        Random rand = new Random();
        return rand.ints(listSize, 0, sourceWords.size())
                .mapToObj(sourceWords::get)
                .collect(Collectors.toList());
    }

    /**
     * Return the list of all source words, which cannot be modified
     *
     * @return The unmodifiable list of all source words
     */
    public List<String> allWords() {
        return Collections.unmodifiableList(sourceWords);
    }
}