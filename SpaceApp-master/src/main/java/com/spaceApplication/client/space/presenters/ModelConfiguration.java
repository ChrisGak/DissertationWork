package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Created by Chris on 02.04.2018.
 */
public class ModelConfiguration {
    interface ModelConfigurationUiBinder extends UiBinder<DivElement, ModelConfiguration> {
    }

    private static ModelConfigurationUiBinder ourUiBinder = GWT.create(ModelConfigurationUiBinder.class);

    public ModelConfiguration() {
        DivElement rootElement = ourUiBinder.createAndBindUi(this);
    }
}