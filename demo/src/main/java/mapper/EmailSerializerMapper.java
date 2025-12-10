package mapper;

import org.apache.kafka.common.serialization.Serializer;

import com.example.demo.dto.EmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailSerializerMapper implements Serializer<EmailDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, EmailDTO emailDto) {
        try {
            return objectMapper.writeValueAsBytes(emailDto);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing object", e);
        }
    }

}
