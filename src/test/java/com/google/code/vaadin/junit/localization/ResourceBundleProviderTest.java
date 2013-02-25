/*
 * Copyright (C) 2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.vaadin.junit.localization;

import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.localization.ResourceBundleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Locale;

/**
 * @author Alexey Krylov
 * @since 07.02.13
 */
public class ResourceBundleProviderTest extends AbstractMVPTest{

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ResourceBundleProvider resourceBundleProvider;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testResourceBundleProvider() {
        Locale englishLocale = new Locale("en", "US");
        Locale russianLocale = new Locale("ru", "RU");
        Assert.assertNotNull("ResourceBundleProvider is null", resourceBundleProvider);
        Assert.assertEquals("ResourceBundleProvider is not singleton", resourceBundleProvider,  injector.getInstance(ResourceBundleProvider.class));

        Assert.assertEquals("Invalid value from EN_US locale", "value", resourceBundleProvider.getBundle("TestBundle", englishLocale).getString("key"));
        Assert.assertEquals("Invalid value from EN_US locale with encoding", "value", resourceBundleProvider.getBundle("TestBundle", englishLocale, "UTF-8").getString("key"));
        Assert.assertEquals("Invalid value from RU_RU locale", "value_RU", resourceBundleProvider.getBundle("TestBundle", russianLocale).getString("key"));
        Assert.assertEquals("Invalid value from RU_RU locale with encoding", "value_RU", resourceBundleProvider.getBundle("TestBundle", russianLocale, "UTF-8").getString("key"));
    }
}
