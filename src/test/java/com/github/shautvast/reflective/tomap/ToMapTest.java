package com.github.shautvast.reflective.tomap;

import com.github.shautvast.reflective.compare.Shop;
import com.github.shautvast.reflective.compare.Storage;
import com.github.shautvast.reflective.compare.PlumBean;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ToMapTest {
    @Test
    void testBeans() throws Exception {
        Map<String,Object> map = ToMap.map(new PlumBean("small", "red", true, 1, 1.0F, Storage.HIGH, (byte) 1, List.of(new Shop("tesco"))));
        assertEquals("small", map.get("core"));
        assertEquals("red", map.get("peel"));
        assertEquals(true, map.get("juicy"));
        assertEquals(1, map.get("number"));
        assertEquals(1.0F, map.get("price"));
        assertEquals(Storage.HIGH, map.get("storage"));
        assertEquals((byte)1, map.get("cores"));
        Object shops = map.get("shops");
        assertTrue(shops instanceof List);
        List<Shop> shopsList = (List<Shop>) shops;
        assertEquals(1, shopsList.size());
        assertEquals("tesco", shopsList.get(0).getName());
    }
}
