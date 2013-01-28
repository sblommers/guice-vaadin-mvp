/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * IMVPApplicationContext - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 28.01.13
 */
public interface MVPApplicationContext {

    /*===========================================[ INTERFACE METHODS ]==============*/

    Collection getAndRemoveSessionScopedSubscribers(@NotNull String sessionID);

    Collection getSessionScopedSubscribers(@NotNull String sessionID);
}