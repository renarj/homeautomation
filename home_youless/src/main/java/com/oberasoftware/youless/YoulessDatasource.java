package com.oberasoftware.youless;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class YoulessDatasource {
    private static final Logger LOG = LoggerFactory.getLogger(YoulessDatasource.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${youless.location}")
    private String youlessIP;


//    public List<MonitorDataEntry> synchronise() throws MonitorException {
//        LOG.info("Connecting to ip: {}", youlessIP);
//
//        List<MonitorDataEntry> e = new ArrayList<>();
//        IntStream.rangeClosed(1, 12).boxed().map(this::loadMonth).collect(Collectors.toList()).forEach(e::addAll);
//
//        return e;
//    }
//
//    public List<MonitorDataEntry> loadMonth(int month) {
//        try {
//            HttpURLConnection urlConnection = (HttpURLConnection) new URL(getMonthUrl(month)).openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            String response = getResponseAsString(urlConnection);
//            LOG.debug("Response: {}", response);
//
//            return decodeEntry(response);
//        } catch (IOException | MonitorException e) {
//            LOG.error("", e);
//            return new ArrayList<>();
//        }
//    }
//
//    private List<MonitorDataEntry> decodeEntry(String response) {
//        List<MonitorDataEntry> entries = new ArrayList<>();
//        try {
//            JsonNode rootNode = OBJECT_MAPPER.readTree(response);
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//            String date = rootNode.findValue("tm").asText();
//            LocalDate localDate = LocalDate.parse(date, formatter);
//
//            int day = localDate.getDayOfMonth();
//            for(JsonNode n : rootNode.findValue("val")) {
//                String val = n.asText().trim();
//                val = val.replace(",", ".");
//                if(!val.equals("*")) {
//                    try {
//                        Double d = Double.parseDouble(val);
//
//                        entries.add(new MonitorDataEntry(day, localDate.getMonthValue(), localDate.getYear(), "kwh", d.toString()));
//                    } catch(NumberFormatException e) {
//                        LOG.warn("Incorrect KWH value found: {}", val);
//                    }
//                }
//
//                day++;
//            }
//        } catch (IOException e) {
//            LOG.warn("Could not parse JSON response from Youless device", e);
//        }
//
//        return entries;
//    }
//
//    public String getResponseAsString(HttpURLConnection urlConnection) throws MonitorException {
//        try {
//            if(urlConnection.getInputStream() != null) {
//                BufferedReader read = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String r = read.readLine();
//                read.close();
//
//                return r;
//            } else {
//                return null;
//            }
//        } catch(IOException e) {
//            throw new MonitorException("Unable to load monitoring data from youless device", e);
//        }
//    }
//
//    private String getMonthUrl(int month) {
//        return getUrl(String.format("V?m=%d", month));
//    }
//
//    private String getUrl(String resource) {
//        return String.format("http://%s/%s&f=j", youlessIP, resource);
//    }
}
