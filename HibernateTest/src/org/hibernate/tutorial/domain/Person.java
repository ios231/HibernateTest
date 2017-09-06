package org.hibernate.tutorial.domain;

import java.util.HashSet;
import java.util.Set;

public class Person {

	 private Long id;
     private int age;
     private String firstname;
     private String lastname;
     
     private Set<Event> events = new HashSet<Event>();
     private Set<String> emailAddresses = new HashSet<String>();

     public Person() {}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public Set getEvents() {
        return events;
    }

    protected void setEvents(Set events) {
        this.events = events;
    }
    
    protected void addToEvent(Event event) {
        this.getEvents().add(event);
        event.getParticipants().add(this);
    }

    public void removeFromEvent(Event event) {
        this.getEvents().remove(event);
        event.getParticipants().remove(this);
    }

	public Set getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(Set emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

    // Accessor methods for all properties, private setter for 'id'
     
}
