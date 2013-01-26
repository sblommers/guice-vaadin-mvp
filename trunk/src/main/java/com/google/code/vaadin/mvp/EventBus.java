/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import javax.validation.constraints.NotNull;

/**
 * EventBus - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public interface EventBus extends EventPublisher{

    /*===========================================[ INTERFACE METHODS ]==============*/

    void subscribe(@NotNull Object subscriber);

    void unsubscribe(@NotNull Object subscriber);
}
