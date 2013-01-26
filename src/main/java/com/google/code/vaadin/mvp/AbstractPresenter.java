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

package com.google.code.vaadin.mvp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Abstract implementation of CDI Utils MVP-pattern presenter. Associated
 * {@link View} interface extension is
 * declared for each extended AbstractPresenter using
 * ViewInterface} annotation.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
//@SessionScoped
public abstract class AbstractPresenter<T extends View> implements Serializable {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = -690166254266524606L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected transient Logger logger;
    protected T view;

    /*===========================================[ CLASS METHODS ]================*/

    @Inject
    protected void init() {
        logger = LoggerFactory.getLogger(getClass());
    }
    //TODO simplify?? hide publicity, rename
    //TODO идея - упростить схему с viewInitialized через InjectionListener'ов -  в hear инициализировать presenter
    public void setView(T view) {
        this.view = view;
        initPresenter();
        logger.debug(String.format("Presenter initialized: [%s#%d], view class: [%s#%d]", getClass().getName(), hashCode(), view.getClass().getName(), view.hashCode()));
    }

    /**
     * Initializes the presenter.
     */
    protected abstract void initPresenter();

    /**
     * Performs view actions called each time the view is opened.
     */
    public abstract void viewOpened();
}
