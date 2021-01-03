package com.civuniverse.ip;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.salt.SaltGenerator;

import java.security.Provider;

class EncryptionManager {
    private static PBEConfig config;

    public static void main(String[] args) {
        config = new PBEConfig() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getPassword() {
                return args[0];
            }

            @Override
            public Integer getKeyObtentionIterations() {
                return null;
            }

            @Override
            public SaltGenerator getSaltGenerator() {
                return null;
            }

            @Override
            public String getProviderName() {
                return null;
            }

            @Override
            public Provider getProvider() {
                return null;
            }

            @Override
            public Integer getPoolSize() {
                return null;
            }
        };
    }

    protected static String encrypt(String string) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);

        return encryptor.encrypt(string);
    }

    protected static String decrypt(String string) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);

        return encryptor.decrypt(string);
    }
}
