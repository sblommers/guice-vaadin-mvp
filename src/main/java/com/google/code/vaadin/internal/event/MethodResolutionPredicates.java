/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventType;
import com.google.code.vaadin.mvp.Observes;
import net.engio.mbassy.common.IPredicate;

import java.lang.reflect.Method;

/**
 * MethodResolutionPredicates - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
public interface MethodResolutionPredicates {
    /*===========================================[ INTERFACE METHODS ]==============*/

    IPredicate<Method> AllEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            return target.getAnnotation(Observes.class) != null;
        }
    };

    IPredicate<Method> ViewEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.VIEW);
        }
    };

    IPredicate<Method> ModelEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.MODEL);
        }
    };

    IPredicate<Method> SharedModelEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.SHARED_MODEL);
        }
    };


}
