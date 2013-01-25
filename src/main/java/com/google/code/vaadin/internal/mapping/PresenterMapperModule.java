/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.inject.AbstractModule;

/**
 * PresenterMappingModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class PresenterMapperModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(PresenterMapper.class).asEagerSingleton();
    }
}
