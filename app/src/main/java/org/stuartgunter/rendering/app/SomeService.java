package org.stuartgunter.rendering.app;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SomeService {

    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("id", "some-id");
        data.put("text", "some-text");

        return data;
    }
}
