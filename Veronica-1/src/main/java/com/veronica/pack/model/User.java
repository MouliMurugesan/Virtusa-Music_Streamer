package com.veronica.pack.model;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import org.springframework.lang.NonNull;

@Entity
@Table(name="user", uniqueConstraints=@UniqueConstraint(columnNames="emailid"))
public class User {
 
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String emailid;
	@NonNull
	private String password;
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(name = "user_roles",
				joinColumns=@JoinColumn(name="user_id",referencedColumnName="id"),
				inverseJoinColumns=@JoinColumn(name="role_id",referencedColumnName="id"))
	private Collection<Role> roles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	public User(String name, String emailid, String password, Collection<Role> roles) {
		super();
		this.name = name;
		this.emailid = emailid;
		this.password = password;
		this.roles = roles;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}
