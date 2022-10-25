package com.luffy.testautomation.prjhelpers;

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/** Used to store data passed between scenario's steps. */
@Singleton
public class TestStorage {

    private static ThreadLocal<Map<String, Object>> storage = ThreadLocal.withInitial(HashMap::new);

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(storage.get().get(key));
    }

    public void put(String key, Object value) {
        storage.get().put(key, value);
    }

    public void clear() {
        storage.get().clear();
    }
}
