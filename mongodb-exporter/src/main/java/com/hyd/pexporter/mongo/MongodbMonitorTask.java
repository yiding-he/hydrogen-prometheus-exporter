package com.hyd.pexporter.mongo;

import com.hyd.pexporter.ExportItem;
import com.hyd.pexporter.ExportRepository;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Properties;
import java.util.TimerTask;

public class MongodbMonitorTask extends TimerTask {

    private final Properties properties;

    private final ExportRepository exportRepository;

    private final MongoClient mongoClient;

    private final MongoDatabase mongoDatabase;

    public MongodbMonitorTask(Properties properties, ExportRepository exportRepository) {
        this.properties = properties;
        this.exportRepository = exportRepository;

        ConnectionString cs = new ConnectionString(properties.getProperty("mongodb.uri"));
        if (cs.getDatabase() == null) {
            throw new IllegalStateException("Database is empty in mongo.uri");
        }

        this.mongoClient = MongoClients.create(cs);
        this.mongoDatabase = this.mongoClient.getDatabase(cs.getDatabase());
    }

    @Override
    public void run() {
        properties.stringPropertyNames().forEach(propName -> {
            if (propName.startsWith("metrics.")) {
                fetchMetric(propName.substring("metrics.".length()), properties.getProperty(propName));
            }
        });
    }

    private void fetchMetric(String metricName, String metricProperty) {
        ExportItem exportItem = new ExportItem();
        exportItem.setName(metricName);
        exportItem.setValue(calculate(metricProperty));
        exportRepository.addExportItem(exportItem);
    }

    private String calculate(String metricProperty) {
        String collectionName;
        String matcher;

        int splitIndex = metricProperty.indexOf("|");
        if (splitIndex != -1) {
            collectionName = metricProperty.substring(0, splitIndex);
            matcher = metricProperty.substring(splitIndex + 1);
        } else {
            collectionName = metricProperty;
            matcher = null;
        }

        long count;
        if (matcher == null) {
            count = mongoDatabase.getCollection(collectionName).estimatedDocumentCount();
        } else {
            count = mongoDatabase.getCollection(collectionName).countDocuments(Document.parse(matcher));
        }

        return String.valueOf(count);
    }
}
