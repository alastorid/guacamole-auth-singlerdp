package com.github.evanlinde.guacamole.auth;

import org.apache.guacamole.properties.StringGuacamoleProperty;

/**
 * Utility class containing all properties used by the custom authentication
 * tutorial. The properties defined here must be specified within
 * guacamole.properties to configure the tutorial authentication provider.
 */
public class SingleRDPProperties {

    /**
     * This class should not be instantiated.
     */
    private SingleRDPProperties() {}

    public static final StringGuacamoleProperty SINGLERDP_TITLE = 
        new StringGuacamoleProperty() {
        @Override
        public String getName() { return "singlerdp-title"; }
    };

    public static final StringGuacamoleProperty SINGLERDP_HOSTNAME = 
        new StringGuacamoleProperty() {
        @Override
        public String getName() { return "singlerdp-hostname"; }
    };

    public static final StringGuacamoleProperty SINGLERDP_PORT = 
        new StringGuacamoleProperty() {
        @Override
        public String getName() { return "singlerdp-port"; }
    };

    public static final StringGuacamoleProperty SINGLERDP_IGNORE_CERT = 
        new StringGuacamoleProperty() {
        @Override
        public String getName() { return "singlerdp-ignore-cert"; }
    };

}
