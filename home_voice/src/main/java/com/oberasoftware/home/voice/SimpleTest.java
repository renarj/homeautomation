package com.oberasoftware.home.voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class SimpleTest {
    private static final Logger LOG = getLogger(SimpleTest.class);

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

// Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
// Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
// Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");


        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
// Start recognition process pruning previously cached data.
        recognizer.startRecognition(true);

        SpeechResult result;
        while((result = recognizer.getResult()) != null) {
            LOG.info("Primary Hypothesis: {}", result.getHypothesis());
            result.getNbest(3).forEach(r -> LOG.debug("Hypo: {}", r));
        }
    }
}
