package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Data
@ToString(callSuper=true)
@NoArgsConstructor
public class Item extends BaseEntity {

	@Column(unique=true)
	String name;
	
}
