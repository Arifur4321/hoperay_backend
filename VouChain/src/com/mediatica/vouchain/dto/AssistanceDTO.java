package com.mediatica.vouchain.dto;

import java.util.List;

public class AssistanceDTO {
	
	
	private String title;
	private String content;
	private List<ContactDTO> contacts;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<ContactDTO> getContacts() {
		return contacts;
	}
	
	public void setContacts(List<ContactDTO> contacts) {
		this.contacts = contacts;
	}
	
	public void addContact(ContactDTO contact) {
		this.contacts.add(contact);
	}
	
	
	
	
	

}
