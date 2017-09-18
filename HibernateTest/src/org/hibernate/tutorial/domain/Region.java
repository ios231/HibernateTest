package org.hibernate.tutorial.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_region")
public class Region implements Comparable<Region>,Serializable{

	//private Integer id;
	private String id;
	private String name;
	private String districtCode;
	private Region parent;
	private Set<Region> children = new HashSet<Region>();
	
	/*@Id
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	@Column(name="id",nullable=false,unique=true)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	
	@ManyToOne
	@JoinColumn(name="pid",insertable=true, updatable=false)
	public Region getParent() {
		return parent;
	}
	public void setParent(Region parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy="parent",cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<Region> getChildren() {
		return children;
	}
	public void setChildren(Set<Region> children) {
		this.children = children;
	}

	@Override
	public int compareTo(Region o) {
		// TODO Auto-generated method stub
		return districtCode.compareTo(o.districtCode);
	}
	
	@Id @GeneratedValue(generator="system-uuid")  
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
}
