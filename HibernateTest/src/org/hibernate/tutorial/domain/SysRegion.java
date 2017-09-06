package org.hibernate.tutorial.domain;

import java.util.HashSet;
import java.util.Set;

public class SysRegion implements Comparable<SysRegion>{

	private Integer id;
	private String name;
	private String districtCode;
	private SysRegion parent;
	private Set<SysRegion> children = new HashSet<SysRegion>();
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public SysRegion getParent() {
		return parent;
	}
	public void setParent(SysRegion parent) {
		this.parent = parent;
	}
	public Set<SysRegion> getChildren() {
		return children;
	}
	public void setChildren(Set<SysRegion> children) {
		this.children = children;
	}
	@Override
	public int compareTo(SysRegion o) {
		// TODO Auto-generated method stub
		return districtCode.compareTo(o.districtCode);
	}
	
	
}
