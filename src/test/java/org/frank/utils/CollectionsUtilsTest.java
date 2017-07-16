package org.frank.utils;

import org.frank.PrivateConstructor;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.frank.utils.CollectionsUtils.Pair.pair;
import static org.junit.Assert.*;

public class CollectionsUtilsTest extends PrivateConstructor {

    public CollectionsUtilsTest() { super(CollectionsUtils.class); }

    @Test
    public void hashMap() throws Exception {
        Map<String, String> map = CollectionsUtils.hashMap(
                pair(null, null),
                pair(null, "value"),
                pair("key", null),
                pair("key", "value"));

        assertEquals(1, map.size());
        assertEquals(null, map.get(null));
        assertEquals("value", map.get("key"));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(CollectionsUtils.isEmpty(null));
        assertTrue(CollectionsUtils.isEmpty(Collections.emptyMap()));

        assertFalse(CollectionsUtils.isEmpty(new HashMap<String, String>(){{ put("key", "value"); }}));
    }

}