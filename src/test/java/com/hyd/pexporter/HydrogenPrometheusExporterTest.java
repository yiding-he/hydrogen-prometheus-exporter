package com.hyd.pexporter;

class HydrogenPrometheusExporterTest {

    public static void main(String[] args) throws Exception {
        HydrogenPrometheusExporter exporter = new HydrogenPrometheusExporter(8080);
        ExportRepository repository = exporter.getExportRepository();

        ExportItem item1 = new ExportItem("cpu_load", "1").setAttribute("core", "0");
        item1.setType(ExportItem.TYPE_GAUGE);
        item1.setHelp("中央处理器的负载情况");
        repository.addExportItem(item1);

        ExportItem item2 = new ExportItem("bytes_written", "234567").setAttribute("device", "/dev/sda");
        item2.setType(ExportItem.TYPE_COUNTER);
        item2.setHelp("磁盘的写入字节数");
        repository.addExportItem(item2);

        exporter.start(1000, false);
        System.out.println("Server started.");
    }
}