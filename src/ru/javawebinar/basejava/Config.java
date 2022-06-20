package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String PROPS = "/resumes.properties";
    private static final Config INSTANCE = new Config();

    private Properties properties = new Properties();
    private File storageDir;
    private Storage storage;

    private Config() {
        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
            storage = new SqlStorage(properties);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public static Config getINSTANCE() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Properties getProperties() {
        return properties;
    }

    public Storage getStorage() {
        return storage;
    }
}
