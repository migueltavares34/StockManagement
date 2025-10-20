package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class StockMovement extends BaseEntity {

	private Date creationDate;

	@OneToOne
	private Item item;

	@Column(nullable = false)
	private Long quantity;

	public void addItems(Long quantity) {
		this.quantity += quantity;
	}

	public void subtractItems(Long quantity) {
		this.quantity -= quantity;		
	}
}
