package br.com.home.lab.softwaretesting.automation.util;

import org.testng.Assert;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class ScenarioContextData {
    private final Semaphore semaphore = new Semaphore(1);
    private final Map<String, Object> context = new Hashtable<>();

    public <T> void setContext(String key, T value) {
        try{
            semaphore.acquire();
            context.put(key, value);
            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while acquiring semaphore", e);
        }
    }

    public boolean exists(String key) {
        try{
            semaphore.acquire();
            boolean result =  context.containsKey(key);
            semaphore.release();
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while acquiring semaphore", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try{
            semaphore.acquire();
            T value = (T) context.get(key);
            Assert.assertNotNull(value, "The value to be recovered cannot be null");
            semaphore.release();
            return value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while acquiring semaphore", e);
        }
    }
}
