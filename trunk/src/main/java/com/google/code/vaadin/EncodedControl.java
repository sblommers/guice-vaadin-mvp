/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * EncodedControl - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 01.02.13
 */
public class EncodedControl extends ResourceBundle.Control {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private String encoding;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public EncodedControl(String encoding) {
        this.encoding = encoding;
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        if (!format.equals("java.properties")) {
            return super.newBundle(baseName, locale, format, loader, reload);
        }
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        final String resourceName = toResourceName(bundleName, "properties");
        final ClassLoader classLoader = loader;
        final boolean reloadFlag = reload;
        InputStream stream;
        try {
            stream = AccessController.doPrivileged(
                    new PrivilegedExceptionAction<InputStream>() {
                        @Override
                        public InputStream run() throws IOException {
                            InputStream is = null;
                            if (reloadFlag) {
                                URL url = classLoader.getResource(resourceName);
                                if (url != null) {
                                    URLConnection connection = url.openConnection();
                                    if (connection != null) {
                                        // Disable caches to get fresh data for
                                        // reloading.
                                        connection.setUseCaches(false);
                                        is = connection.getInputStream();
                                    }
                                }
                            } else {
                                is = classLoader.getResourceAsStream(resourceName);
                            }
                            return is;
                        }
                    });
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getException();
        }

        if (stream != null) {
            Reader reader = null;
            try {
                reader = new InputStreamReader(stream, encoding);
                bundle = new PropertyResourceBundle(reader);
            } finally {
                if (reader != null) {
                    reader.close();
                }
                stream.close();
            }
        }
        return bundle;
    }
}
