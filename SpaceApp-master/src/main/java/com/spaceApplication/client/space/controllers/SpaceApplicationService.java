package com.spaceApplication.client.space.controllers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;

@RemoteServiceRelativePath("SpaceApplicationService")
public interface SpaceApplicationService extends RemoteService {

    // Sample interface method of remote interface
    String getMessage(String msg);

    OrbitalElementsClient getTestModelIntegrationResult() throws TetherSystemModelValueException;

    OrbitalElementsClient getCalculationResult(ElectrodynamicTetherSystemModelClient baseModel) throws TetherSystemModelValueException;

    /**
     * Utility/Convenience class.
     * Use SpaceApplicationService.App.getInstance() to access static instance of SpaceApplicationServiceAsync
     */
    class App {
        private static SpaceApplicationServiceAsync ourInstance = GWT.create(SpaceApplicationService.class);

        public static synchronized SpaceApplicationServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
