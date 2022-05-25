package edu.harvard.hms.ccb.reasoner.ontology.smores;

import py4j.GatewayServer;

public class PythonGateway {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new Smores());
        gatewayServer.start();
    }

}
