package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import nl.renarj.jasdb.LocalDBSession;
import nl.renarj.jasdb.api.DBSession;
import nl.renarj.jasdb.core.SimpleKernel;
import nl.renarj.jasdb.core.exceptions.JasDBException;
import nl.renarj.jasdb.core.platform.HomeLocatorUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class JasDBCentralDataStoreTest {
    private static final Logger LOG = getLogger(JasDBCentralDataStoreTest.class);


    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        System.setProperty(HomeLocatorUtil.JASDB_HOME, folder.newFolder().toString());
    }

    @After
    public void tearDown() throws JasDBException {
        SimpleKernel.shutdown();
    }

    @Test
    public void storeAndUpdateDevice() throws JasDBException, DataStoreException {
        DBSession session = new LocalDBSession();

        LOG.debug("Items stored now: {}", session.createOrGetBag("items").getSize());

        JasDBCentralDatastore centralDatastore = new JasDBCentralDatastore();
        centralDatastore.createIndexOnStartup();

        String id = UUID.randomUUID().toString();

        centralDatastore.store(new DeviceItem(id, "controller1", "plugin1", "device1", "test device", new HashMap<>(), new HashMap<>()));

        Optional<DeviceItem> item = centralDatastore.findDevice("controller1", "plugin1", "device1");
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getName(), is("test device"));

        centralDatastore.store(new DeviceItem(id, "controller1", "plugin1", "device1", "updated name", new HashMap<>(), new HashMap<>()));

        assertThat(session.getBag("items").getSize(), is(1l));

        item = centralDatastore.findDevice("controller1", "plugin1", "device1");
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getName(), is("updated name"));
    }
}
