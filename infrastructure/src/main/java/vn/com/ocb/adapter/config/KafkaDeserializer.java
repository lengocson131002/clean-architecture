package vn.com.ocb.adapter.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class KafkaDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaDeserializer() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.readValue(data, new TypeReference<T>() {});
        } catch (IOException e) {
            e.printStackTrace();
            // Handle deserialization exception
        }
        return null;
    }

    @Override
    public void close() {
        // Clean-up resources if any
    }
}
