package com.hyd.pexporter;

class HydrogenPrometheusExporterTest {

    public static void main(String[] args) throws Exception {
        HydrogenPrometheusExporter exporter = new HydrogenPrometheusExporter(8080);
        exporter.start(1000, false);
        System.out.println("Server started.");
    }
}