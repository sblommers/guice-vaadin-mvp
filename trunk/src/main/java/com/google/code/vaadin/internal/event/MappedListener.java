/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import net.engio.mbassy.listener.Filter;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.Mode;

import java.lang.annotation.Annotation;

/**
 * MappedListener - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class MappedListener implements Listener {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public Filter[] filters() {
        return new Filter[0];
    }

    @Override
    public Mode dispatch() {
        return Mode.Synchronous;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean rejectSubtypes() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Listener.class;
    }
}
