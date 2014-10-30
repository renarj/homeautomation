package com.oberasoftware;

import com.oberasoftware.data.DataEntry;
import com.oberasoftware.data.DataEntryRepository;
import com.oberasoftware.exceptions.MonitorException;
import com.oberasoftware.youless.MonitorDataEntry;
import com.oberasoftware.youless.YoulessDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author renarj
 */
@Component
public class MonitorSynchroniser {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorSynchroniser.class);

    @Autowired
    private YoulessDatasource datasource;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @PostConstruct
    public void intialiseMonitors() throws MonitorException {
        List<MonitorDataEntry> monitorEntries = datasource.synchronise();
        monitorEntries.forEach(m -> LOG.info("Found entry: {}", m));

        monitorEntries.stream().map(MonitorDataEntry::toDataEntry).forEach(this::save);

        Page<DataEntry> entries = dataEntryRepository.findByYearAndMonth(new PageRequest(0, 100), 2014, 1);
        entries.forEach(e -> LOG.info(e.getValue()));

    }

    private void save(DataEntry e) {
        dataEntryRepository.save(e);
    }
}
