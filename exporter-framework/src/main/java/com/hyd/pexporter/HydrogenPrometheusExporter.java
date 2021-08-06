package com.hyd.pexporter;

import fi.iki.elonen.NanoHTTPD;

public class HydrogenPrometheusExporter extends NanoHTTPD {

    private final ExportRepository exportRepository = new ExportRepository();

    public HydrogenPrometheusExporter(int port) {
        super(port);
    }

    public HydrogenPrometheusExporter(String hostname, int port) {
        super(hostname, port);
    }

    public ExportRepository getExportRepository() {
        return exportRepository;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri().substring(1);  // remove leading '/'
        if (!uri.equals("metrics") && !uri.equals("metrics/")) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 no content");
        }

        return newFixedLengthResponse(Response.Status.OK, "text/plain", exportRepository.toResponseString());
    }
}
