package com.example.demo.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
public class NotificationDTO {

	private String errorMessage;
	private String notificationType;
}
