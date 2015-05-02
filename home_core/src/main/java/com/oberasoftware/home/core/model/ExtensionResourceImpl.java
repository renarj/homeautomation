package com.oberasoftware.home.core.model;

import com.oberasoftware.home.api.model.ExtensionResource;

import java.io.InputStream;

/**
 * @author renarj
 */
public class ExtensionResourceImpl implements ExtensionResource {

    private final String mediaType;
    private final InputStream stream;

    public ExtensionResourceImpl(String mediaType, InputStream stream) {
        this.mediaType = mediaType;
        this.stream = stream;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }
}
