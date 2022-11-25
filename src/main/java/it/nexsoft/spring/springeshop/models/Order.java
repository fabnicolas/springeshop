package it.nexsoft.spring.springeshop.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Table(name = "purchase_items")
@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@ManyToOne
	@JoinColumn(name = "article_id")
	@JsonBackReference
	private Article article;

	@Column(name = "order_date")
	private LocalDate orderDate;

	@Column(name = "price")
	private Double price;

	public Order() {
	}

	public Order(final User user, final Article article, final LocalDate orderDate, final Double price) {
		this.user = user;
		this.article = article;
		this.orderDate = orderDate;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(final Article article) {
		this.article = article;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(final LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

}
