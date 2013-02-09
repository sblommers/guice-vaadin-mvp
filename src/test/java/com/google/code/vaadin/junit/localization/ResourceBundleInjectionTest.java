/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.localization;

import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.localization.InjectBundle;
import org.junit.Assert;
import org.junit.Test;

import java.util.ResourceBundle;

/**
 * ResourceBundleInjectionTest - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
public class ResourceBundleInjectionTest extends AbstractMVPTest{

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @InjectBundle(baseName = "TestBundle_ru_RU")
    private ResourceBundle resourceBundle;

	/*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testBundleInjection() {
        Assert.assertNotNull(resourceBundle);
        Assert.assertEquals("value", resourceBundle.getString("key"));
    }
}
