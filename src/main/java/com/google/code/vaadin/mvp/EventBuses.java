/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * EventBuses - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 28.01.13
 */
public interface EventBuses {

	/*===========================================[ INNER CLASSES ]================*/

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ViewEventBus {
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface GlobalViewEventBus {
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ModelEventBus {
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface GlobalModelEventBus {
    }
}
