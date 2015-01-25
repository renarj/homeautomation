package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.storage.model.DeviceItem;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author renarj
 */
public class DeviceEntityMapperTest {
    @Test
    public void testMapDeviceToEntity() {
        DeviceItem item = new DeviceItem(UUID.randomUUID().toString(), "localhost", "zwave", "1", "test", new HashMap<>(), new HashMap<>());

        assertThat(new DeviceEntityMapper().mapFrom(item).getProperty("deviceId").getFirstValueObject(), is("1"));
    }
}
