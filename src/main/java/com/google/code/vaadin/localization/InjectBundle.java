/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.localization;

import java.lang.annotation.*;

/**
 * BundleLocale - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectBundle {
    String baseName();
    String encoding() default "UTF-8";
}
