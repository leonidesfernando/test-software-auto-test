package br.com.home.lab.softwaretesting.automation.util;

import org.testng.Assert;

import java.util.Hashtable;
import java.util.Map;

public class ScenarioContextData {
    private final Map<String, Object> context = new Hashtable<>();

    public <T> void setContext(String key, T value) {
        context.put(key, value);
    }

    public boolean exists(String key) {
        return context.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        T value = (T) context.get(key);
        Assert.assertNotNull(value, "The value to be recovered cannot be null");
        return value;
    }
}
