package com.yibing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JacksonTest {
    public static void main(String[] args) throws IOException {
        Map<Long, Long> map = new HashMap<Long, Long>();
        map.put(1L, 10L);
        map.put(2L, 20L);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(map);
        System.out.println(result);
    }
}
