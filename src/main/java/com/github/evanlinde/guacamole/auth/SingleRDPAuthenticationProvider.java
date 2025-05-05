package com.github.evanlinde.guacamole.auth;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

public class SingleRDPAuthenticationProvider extends SimpleAuthenticationProvider {

    @Override
    public String getIdentifier() {
        return "singlerdp";
    }

    private String extractHostFromRequest(HttpServletRequest request) {
        if (request == null) return null;

        String referer = request.getHeader("referer");
        if (referer == null) return "0.0.0.0";

        // Split by '/' or any non-IP characters
        String[] parts = referer.split("[^0-9.]");

        for (String part : parts) {
            if (part.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                return part;
            }
        }

        return "0.0.0.0";
    }

    @Override
    public Map<String, GuacamoleConfiguration>
        getAuthorizedConfigurations(Credentials credentials)
        throws GuacamoleException {

        // Get hostname from request
        HttpServletRequest request = credentials.getRequest();
        String hostname = extractHostFromRequest(request);
        // Create RDP configuration with hardcoded settings
        Map<String, GuacamoleConfiguration> configs = new HashMap<String, GuacamoleConfiguration>();
        GuacamoleConfiguration config = new GuacamoleConfiguration();

        if(!hostname.startsWith("11.") && !hostname.startsWith("12."))
        {
            throw new GuacamoleException("You lose! Try harder! = " + hostname);
        }

        // Set protocol to RDP
        config.setProtocol("rdp");

        // Basic connection parameters
        config.setParameter("hostname", hostname);
        config.setParameter("port", "3389");

        // Security settings
        config.setParameter("ignore-cert", "true");
        config.setParameter("security", "any");
        config.setParameter("disable-auth", "false");

        // Credentials
        config.setParameter("username", "USERID");
        config.setParameter("password", "PASSW0RD");
        config.setParameter("domain", "");
        config.setParameter("console", "true");

        config.setParameter("enable-wallpaper", "true");
        config.setParameter("enable-theming", "true");
        config.setParameter("enable-font-smoothing", "true");
        config.setParameter("enable-full-window-drag", "true");
        config.setParameter("enable-desktop-composition", "true");
        config.setParameter("enable-menu-animations", "true");

        config.setParameter("disable-bitmap-caching", "true");
        config.setParameter("resize-method", "display-update");
        config.setParameter("force-lossless", "true");

        config.setParameter("disable-audio", "true");

        // Add to configs map
        configs.put("RdpSpy_" + hostname, config);

        return configs;
    }
}