package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "appOrder")
public class Order extends BaseEntity {

	private Date creationDate;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Item item;

	@Column(nullable = false)
	private Long quantity;

	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;

}
