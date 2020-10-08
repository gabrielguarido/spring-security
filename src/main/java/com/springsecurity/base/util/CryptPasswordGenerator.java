package com.springsecurity.base.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for password encryption.
 *
 * @author Gabriel Oliveira
 */
public class CryptPasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println(encoder.encode("your_password"));
    }

}
