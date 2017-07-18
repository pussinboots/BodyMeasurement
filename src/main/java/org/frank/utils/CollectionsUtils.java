package org.frank.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

public class CollectionsUtils {

    private CollectionsUtils() {}

    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public static class Pair<K, V> {
        private K key;
        private V value;

        public static <K, V> Pair<K, V> pair(K key, V value) {
            return new Pair<>(key, value);
        }

        public boolean isNotNull() {
            return (key != null) && (value != null);
        }
    }

    @SafeVarargs
    public static <K, V> Map<K, V> hashMap(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            if (pair.isNotNull()) map.put(pair.key(), pair.value());
        }
        return map;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || (map.isEmpty());
    }

}

