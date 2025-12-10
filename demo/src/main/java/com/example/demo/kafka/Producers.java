package com.example.demo.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.example.demo.dto.EmailDTO;

import lombok.extern.slf4j.Slf4j;
import mapper.EmailSerializerMapper;

@Slf4j
public class Producers {

	private static final Properties PRODUCER_PROPERTIES;

	private Producers() {
	}

	static {
		PRODUCER_PROPERTIES = new Properties();
		PRODUCER_PROPERTIES.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		PRODUCER_PROPERTIES.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		PRODUCER_PROPERTIES.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EmailSerializerMapper.class);
	}

	public static void sendMessage(ProducerRecord<String, EmailDTO> record) {
		try (Producer<String, EmailDTO> producer = new KafkaProducer<>(PRODUCER_PROPERTIES)) {
			producer.send(record, new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (exception != null) {
						log.error("Error sending record: " + exception.getMessage());
						exception.printStackTrace();
					} else {
						log.info("Record sent successfully to topic " + metadata.topic() + " partition "
								+ metadata.partition() + " at offset " + metadata.offset());
					}
				}
			});

		} catch (Exception e) {
			log.error("Exception: " , e);
		}
	}

}
