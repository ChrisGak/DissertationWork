package com.spaceApplication.client.space.controllers;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;

public interface SpaceApplicationServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);

    void getCalculationResult(ElectrodynamicTetherSystemModelClient baseModel, AsyncCallback<OrbitalElementsClient> async);

    void getTestModelIntegrationResult(AsyncCallback<OrbitalElementsClient> async);
}
