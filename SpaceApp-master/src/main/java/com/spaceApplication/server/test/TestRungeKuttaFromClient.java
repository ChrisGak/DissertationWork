package com.spaceApplication.server.test;

import com.spaceApplication.client.exception.TetherSystemModelValueException;
import com.spaceApplication.client.space.model.ElectrodynamicTetherSystemModelClient;
import com.spaceApplication.client.space.model.OrbitalElementsClient;
import com.spaceApplication.server.modeling.differentiation.OrbitalElements;
import com.spaceApplication.server.service.MainAppServiceImpl;

/**
 * Created by Chris
 */
public class TestRungeKuttaFromClient {
    public TestRungeKuttaFromClient() {

    }

    public static void main(String[] args) throws TetherSystemModelValueException {
        MainAppServiceImpl impl = new MainAppServiceImpl();

        OrbitalElementsClient results = impl.getTestModelIntegrationResult();

    }
}
