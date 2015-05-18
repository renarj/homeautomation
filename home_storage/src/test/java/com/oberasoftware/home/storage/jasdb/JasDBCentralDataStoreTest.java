package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.UIItem;
import nl.renarj.jasdb.core.SimpleKernel;
import nl.renarj.jasdb.core.exceptions.JasDBException;
import nl.renarj.jasdb.core.platform.HomeLocatorUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JasDBConfiguration.class)
public class JasDBCentralDataStoreTest {
    private static final Logger LOG = getLogger(JasDBCentralDataStoreTest.class);

    @Autowired
    private JasDBCentralDatastore centralDatastore;

    @Autowired
    private JasDBDAO jasDBDAO;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        System.setProperty(HomeLocatorUtil.JASDB_HOME, folder.newFolder().toString());
    }

    @After
    public void tearDown() throws JasDBException {
        SimpleKernel.shutdown();
    }

    @Test
    public void storeAndUpdateDevice() throws JasDBException, DataStoreException {
        String id = UUID.randomUUID().toString();

        centralDatastore.store(new DeviceItem(id, "controller1", "plugin1", "device1", "test device", new HashMap<>(), new HashMap<>()));

        Optional<DeviceItem> item = jasDBDAO.findDevice("controller1", "plugin1", "device1");
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getName(), is("test device"));

        centralDatastore.store(new DeviceItem(id, "controller1", "plugin1", "device1", "updated name", new HashMap<>(), new HashMap<>()));

        assertThat(jasDBDAO.findDevices().size(), is(1));

        item = jasDBDAO.findDevice("controller1", "plugin1", "device1");
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getName(), is("updated name"));
    }

    @Test
    public void storeAndUpdateUIItem() throws JasDBException, DataStoreException {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        centralDatastore.store(new UIItem(id1, "UI Item 1", "container1","Special item", "switch", "jsdlfjsd", new HashMap<>()));
        centralDatastore.store(new UIItem(id2, "UI Item 2", "container1", "Special item", "switch", "jsdlfjsd", new HashMap<>()));

        assertThat(jasDBDAO.findUIItems("containe1").size(), is(2));
    }
}
