package com.oberasoftware.home.api.model;

import java.io.InputStream;

/**
 * @author renarj
 */
public interface ExtensionResource {

    String getMediaType();

    InputStream getStream();
}
