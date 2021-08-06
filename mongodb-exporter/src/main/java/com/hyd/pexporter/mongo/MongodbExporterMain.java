package com.hyd.pexporter.mongo;

import com.hyd.pexporter.HydrogenPrometheusExporter;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Timer;

public class MongodbExporterMain {

    public static void main(String[] args) throws Exception {
        String configFile = "metrics.properties";
        if (args.length > 0 && args[0].length() > 0) {
            configFile = args[0];
        }

        Path configFilePath = Paths.get(configFile);
        if (!Files.exists(configFilePath)) {
            throw new IllegalStateException("File not found: " + configFilePath.toAbsolutePath());
        }

        Properties properties = new Properties();
        try (InputStream is = Files.newInputStream(configFilePath)) {
            properties.load(is);
        }

        String host = properties.getProperty("server.host", "0.0.0.0");
        int port = Integer.parseInt(properties.getProperty("server.port", "9001"));
        HydrogenPrometheusExporter exporter = new HydrogenPrometheusExporter(host, port);

        MongodbMonitorTask task = new MongodbMonitorTask(properties, exporter.getExportRepository());
        Timer timer = new Timer();
        timer.schedule(task, 10000, 1000);

        exporter.start(10000, false);
        System.out.println("MongoDB exporter started.");
    }
}
