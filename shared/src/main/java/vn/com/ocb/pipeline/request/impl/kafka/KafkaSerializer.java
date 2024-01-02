package vn.com.ocb.pipeline.request.impl.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class KafkaSerializer<T> implements Serializer<T> {
    private final ObjectMapper objectMapper = JsonMapper
            .builder()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .build();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }
}
