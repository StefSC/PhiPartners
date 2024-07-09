package com.phipartners.mizuho.data_store.utils;

import com.phipartners.mizuho.data_store.model.business.InstrumentPrice;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheUtils {

    public static Map<String, List<InstrumentPrice>> addPrice(Map<String, List<InstrumentPrice>> map, InstrumentPrice instrumentPrice, String key, Instant oldDate) {
        List<InstrumentPrice> existing = map.get(key);
        if (!instrumentPrice.getTimestamp().isBefore(oldDate)) {
            if (existing != null) {
                existing.add(instrumentPrice);
                map.put(key, existing);
            } else {
                List<InstrumentPrice> addition = new CopyOnWriteArrayList<>();
                addition.add(instrumentPrice);
                map.put(key, addition);
            }
        }
        return map;
    }

    public static void removeOldData(Map<String, List<InstrumentPrice>> map, Instant oldestDate) {
        if (map != null && !map.isEmpty()) {
            map.keySet().forEach(key -> {
                List<InstrumentPrice> list = map.get(key);
                list.removeIf(element -> element.getTimestamp().isBefore(oldestDate));
                map.replace(key, list);
            });
        }
    }
}
