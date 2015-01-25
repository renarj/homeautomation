package com.oberasoftware;

import com.oberasoftware.exceptions.MonitorException;
import com.oberasoftware.youless.YoulessDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class MonitorSynchroniser {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorSynchroniser.class);

    @Autowired
    private YoulessDatasource datasource;

//    @Autowired
//    private DataEntryRepository dataEntryRepository;

//    @PostConstruct
    public void intialiseMonitors() throws MonitorException {
//        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");
//        influxDB.deleteSeries("sensor_data", "kwh_usage");
//
//        List<MonitorDataEntry> monitorEntries = datasource.synchronise();
//        monitorEntries.forEach(m -> LOG.info("Found entry: {}", m));
//        monitorEntries.forEach(m -> sendInfluxDBPower(influxDB, m));
//
//        monitorEntries.stream().map(MonitorDataEntry::toDataEntry).forEach(this::save);
//
//        Page<DataEntry> entries = dataEntryRepository.findByYearAndMonth(new PageRequest(0, 100), 2014, 1);
//        entries.forEach(e -> LOG.info(e.getValue()));

    }

//    private void save(DataEntry e) {
////        dataEntryRepository.save(e);
//    }

//    private void sendInfluxDBPower(InfluxDB influxDB, MonitorDataEntry dataEntry) {
//        try {
//            double powerValue = Double.parseDouble(dataEntry.getValue());
//
//            long time = LocalDate.of(dataEntry.getYear(), dataEntry.getMonth(), dataEntry.getDay()).atStartOfDay().atZone(ZoneId.of("Europe/Paris")).toInstant().toEpochMilli();
//
//            Serie powerSerie = new Serie.Builder("kwh_usage")
//                    .columns("time", "usage")
//                    .values(time, powerValue)
//                    .build();
//
//            influxDB.write("sensor_data", TimeUnit.MILLISECONDS, powerSerie);
//        } catch(NumberFormatException e) {
//            LOG.error("Invalid value: {}", dataEntry.getValue());
//        }
//    }
}
