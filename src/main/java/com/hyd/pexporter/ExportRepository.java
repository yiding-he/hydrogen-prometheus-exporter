package com.hyd.pexporter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ExportRepository {

    private final Map<String, ExportItem> exportItems = new ConcurrentHashMap<>();

    public void addExportItem(ExportItem exportItem) {
        String mapKey = exportItem.toCacheKey();
        this.exportItems.put(mapKey, exportItem);
    }

    public String toResponseString() {
        return this.exportItems.values()
            .stream()
            .map(ExportItem::toExportLine)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));
    }
}
