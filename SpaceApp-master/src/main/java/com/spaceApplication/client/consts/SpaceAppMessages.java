package com.spaceApplication.client.consts;

import com.google.gwt.i18n.client.Messages;

import java.util.Date;

/**
 * Created by Chris
 */
public interface SpaceAppMessages extends Messages {
    @DefaultMessage("''{0}'' is not a valid symbol.")
    String invalidSymbol(String symbol);

    @DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
    String lastUpdate(Date timestamp);

    @DefaultMessage("An error occured: ''{0}''")
    String getErrorMessage(String errorMessage);
}
