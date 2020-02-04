package com.claire.qanda.common;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;

public class IO {
    public static String inputStreamToString(InputStream inputStream)
            throws IOException {

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    public static Properties getPropertiesFromResource(String resourcePath){
        final InputStream textAsStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        Properties properties = new Properties();
        loadProperties(textAsStream, properties);
        return properties;
    }

    private static void loadProperties(InputStream textAsStream, Properties properties) {
        try {
            properties.load(textAsStream);
        } catch (IOException e) {
            throw new PropertiesLoadException("Failed while attempting to load properties", e);
        }
    }
}
