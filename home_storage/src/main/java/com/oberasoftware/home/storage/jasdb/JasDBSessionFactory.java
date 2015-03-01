package com.oberasoftware.home.storage.jasdb;

import nl.renarj.jasdb.LocalDBSession;
import nl.renarj.jasdb.api.DBSession;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import nl.renarj.jasdb.rest.client.RestDBSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static nl.renarj.core.utilities.StringUtils.stringNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBSessionFactory {
    private static final Logger LOG = getLogger(JasDBSessionFactory.class);

    @Value("${jasdb.mode}")
    private String jasdbMode;

    @Value("${jasdb.host:}")
    private String jasdbHost;

    @Value("${jasdb.post:7050}")
    private int jasdbPort;

    @Value("${jasdb.instance}")
    private String jasdbInstance;


    public DBSession createSession() throws JasDBStorageException {
        if(stringNotEmpty(jasdbMode) && jasdbMode.equals("rest")) {
            LOG.info("Creating JasDB REST session to host: {} port: {} instance: {}", jasdbHost, jasdbPort, jasdbInstance);
            return new RestDBSession(jasdbInstance, jasdbHost, jasdbPort);
        } else {
            LOG.info("Creating JasDB Local session to instance: {}", jasdbInstance);
            return new LocalDBSession(jasdbInstance);
        }
    }
}
