package com.google.code.vaadin.junit;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.inject.Injector;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;

/**
 * Extend this class to test anything related to a Vaadin UI (or in the case of V7, as {@link ScopedUI}. Note that the
 * {@link UIScope} is not prepared until the {@link #uiSetup()} method is called, so subclasses must use providers if
 * they want to inject UIScoped objects - otherwise the injection happens before the UIScope context is ready.
 * <p/>
 * A number of providers are made available by the class
 *
 * @author David Sowerby 18 Jan 2013
 */
@RunWith(MycilaJunitRunner.class)
public abstract class MVPTestBase {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected VaadinRequest mockedRequest = mock(VaadinRequest.class);

    protected Logger logger;
    protected ScopedUIProvider uiProvider;
    protected Injector injector;

    protected UI ui;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    void init(Logger logger, ScopedUIProvider uiProvider, Injector injector) {
        this.logger = logger;
        this.uiProvider = uiProvider;
        this.injector = injector;
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Before
    public void uiSetup() {
        //logger.info("initialising test");

       /* ui = createTestUI(getTestUIClass());
        CurrentInstance.set(UI.class, ui);
        //when(mockedRequest.getParameter("v-loc")).thenReturn(baseUri + "/");
        ui.doInit(mockedRequest, 1);*/
    }

    /**
     * Use this method to create TestUI instances, rather than the UIProvider It simulates the creation of a new
     * CurrentInstance (which happens for each request)
     *
     * @return
     */
    protected ScopedUI createTestUI(Class<? extends ScopedUI> uiClass) {
        CurrentInstance.set(UI.class, null);
        CurrentInstance.set(UIKey.class, null);
        return (ScopedUI) uiProvider.createInstance(uiClass);
    }

    //@After
    public void cleanup() {
        ui.detach();
    }

    protected abstract Class<? extends ScopedUI> getTestUIClass();
}
