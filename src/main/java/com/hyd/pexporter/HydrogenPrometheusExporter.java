package com.hyd.pexporter;

import fi.iki.elonen.NanoHTTPD;

public class HydrogenPrometheusExporter extends NanoHTTPD {

    public HydrogenPrometheusExporter(int port) {
        super(port);
    }

    public HydrogenPrometheusExporter(String hostname, int port) {
        super(hostname, port);
    }
}
