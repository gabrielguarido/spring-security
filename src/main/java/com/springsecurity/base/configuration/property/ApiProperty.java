package com.springsecurity.base.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * API constant properties and security details.
 *
 * @author Gabriel Oliveira
 */
@Service
@ConfigurationProperties("api")
public class ApiProperty {

    private static final String ALLOWED_ORIGIN = "http://localhost:3000";

    private final Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    public String getAllowedOrigin() {
        return ALLOWED_ORIGIN;
    }

    public static class Security {

        private boolean enableHttps;

        public boolean isEnableHttps() {
            return enableHttps;
        }

    }

}
