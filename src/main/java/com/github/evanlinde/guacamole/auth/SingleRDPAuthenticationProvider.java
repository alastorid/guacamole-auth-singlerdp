package com.github.evanlinde.guacamole.auth;

import java.util.HashMap;
import java.util.Map;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;

/**
 * Authentication provider implementation intended to demonstrate basic use
 * of Guacamole's extension API. The credentials and connection information for
 * a single user are stored directly in guacamole.properties.
 */
public class SingleRDPAuthenticationProvider extends SimpleAuthenticationProvider {

    @Override
    public String getIdentifier() {
        return "singlerdp";
    }

    @Override
    public Map<String, GuacamoleConfiguration>
        getAuthorizedConfigurations(Credentials credentials)
        throws GuacamoleException {

        // Get the Guacamole server environment
        Environment env = new LocalEnvironment();

        // Get RDP hostname, port, and connection title from guacamole.properties
        String hostname = env.getRequiredProperty(SingleRDPProperties.SINGLERDP_HOSTNAME);
        String port = env.getRequiredProperty(SingleRDPProperties.SINGLERDP_PORT);      
        String title = env.getRequiredProperty(SingleRDPProperties.SINGLERDP_TITLE);
        String ignore_cert = env.getProperty(SingleRDPProperties.SINGLERDP_IGNORE_CERT, "false");

        // Return a single RDP connection with the given hostname, port, and title
        Map<String, GuacamoleConfiguration> configs = new HashMap<String, GuacamoleConfiguration>();
        GuacamoleConfiguration config = new GuacamoleConfiguration();
        config.setProtocol("rdp");
        config.setParameter("hostname", hostname);
        config.setParameter("port", port);
        config.setParameter("ignore-cert", ignore_cert);
        configs.put(title, config);

        return configs;

    }

}
