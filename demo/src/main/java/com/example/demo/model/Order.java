package com.example.demo.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Entity
@Data
@ToString(callSuper=true)
@NoArgsConstructor
@Table(name = "appOrder")
public class Order extends BaseEntity {
	
	private Date creationDate;

	@ManyToOne
	private Item item;

	@Column(nullable=false)
	private Long quantity;
	
	@ManyToOne
	private User user;
	
}
