package com.spaceApplication.client.space.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Created by Chris on 02.04.2018.
 */
public class GraphResult {
    interface GraphResultUiBinder extends UiBinder<DivElement, GraphResult> {
    }

    private static GraphResultUiBinder ourUiBinder = GWT.create(GraphResultUiBinder.class);

    public GraphResult() {
        DivElement rootElement = ourUiBinder.createAndBindUi(this);
    }
}