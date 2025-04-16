package com.milendyankov.demos.ai4jd;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NaiveEmbeddings {
    private static final Map<String, Double> animalMap = new HashMap<>();
    private static final Map<String, Double> scienceMap = new HashMap<>();
    private static final Map<String, Double> musicMap = new HashMap<>();

    static {
        animalMap.put("cat", 1.0);
        animalMap.put("dog", 1.0);
        animalMap.put("lion", 1.0);
        animalMap.put("bear", 1.0);
        animalMap.put("zoo", 0.8);
        animalMap.put("jungle", 0.75);
        animalMap.put("hunt", 0.45);

        scienceMap.put("science", 1.0);
        scienceMap.put("math", 1.0);
        scienceMap.put("computer", 0.9);
        scienceMap.put("method", 0.6);
        scienceMap.put("research", 0.5);
        scienceMap.put("theory", 0.4);
        scienceMap.put("experiment", 0.3);

        musicMap.put("music", 0.72);
        musicMap.put("piano", 0.95);
        musicMap.put("song", 0.35);
        musicMap.put("notes", 0.51);
        musicMap.put("rock", 0.42);
        musicMap.put("volume", 0.61);
        musicMap.put("sound", 0.18);
    }

    static float[] generateEmbedding(Set<String> tokens) {
        float isAnimal = classify(animalMap, tokens);
        float isScience = classify(scienceMap, tokens);
        float isMusic = classify(musicMap, tokens);

        return new float[]{isAnimal, isScience, isMusic};
    }

    private static float classify(Map<String, Double> map, Set<String> tokens) {
        return (float) map.entrySet().stream()
                .filter(entry -> tokens.contains(entry.getKey()))
                .mapToDouble(Map.Entry::getValue)
                .average()
                .orElse(0.0);
    }
}
