package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import py4j.GatewayServer;

public class PythonGateway {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new SmoorReasoner());
        gatewayServer.start();
    }

}
