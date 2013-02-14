package com.google.code.vaadin.junit;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.inject.Injector;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractMVPTestBase {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected VaadinRequest mockedRequest;
    protected VaadinSession mockedSession;

    protected Logger logger;
    protected ScopedUIProvider uiProvider;
    protected Injector injector;

    protected UI ui;
    private boolean isFirstTest;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    void init(ScopedUIProvider uiProvider, Injector injector) {
        logger = LoggerFactory.getLogger(getClass());
        this.uiProvider = uiProvider;
        this.injector = injector;
        isFirstTest = true;
        mockedRequest = mock(VaadinRequest.class);
        mockedSession = mock(VaadinSession.class);

        ui = CurrentInstance.get(UI.class);
        ui.setSession(mockedSession);
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Before
    public void uiSetup() {
        logger.info("initialising test");
        if (!isFirstTest) {
            ui = createTestUI(getTestUIClass());
            CurrentInstance.set(UI.class, ui);
            ui.doInit(mockedRequest, 1);
            ui.setSession(mockedSession);
        }
    }

    /**
     * Use this method to create TestUI instances, rather than the UIProvider It simulates the creation of a new
     * CurrentInstance (which happens for each request)
     *
     * @return
     */
    protected ScopedUI createTestUI(Class<? extends ScopedUI> uiClass) {
        return (ScopedUI) uiProvider.createInstance(uiClass);
    }

    @After
    public void cleanup() {
        ui.detach();

        if (isFirstTest) {
            isFirstTest = false;
        }
    }

    protected abstract Class<? extends ScopedUI> getTestUIClass();
}
