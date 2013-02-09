/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.logger;

import com.google.code.vaadin.junit.MVPTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * LoggerInjectionTest - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 09.02.13
 */
@RunWith(MVPTestRunner.class)
public class LoggerInjectionTest {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Logger logger;

	/*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testLoggerInjection() {
        Assert.assertNotNull(logger);
        Assert.assertEquals(LoggerInjectionTest.class.getName(), logger.getName());
    }
}
