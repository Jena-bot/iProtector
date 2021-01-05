package com.github.Hafixion.managers;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.salt.SaltGenerator;

import java.security.Provider;

public class EncryptionManager {
    private final PBEConfig config;

    public EncryptionManager(String key) {
        config = new PBEConfig() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getPassword() {
                return key;
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

    public String encrypt(String string) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);

        return encryptor.encrypt(string);
    }

    public String decrypt(String string) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);

        return encryptor.decrypt(string);
    }
}
