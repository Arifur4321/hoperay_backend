package com.mediatica.vouchain.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Pietro Napolitano
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseNewKeyPairDetailDTO  {
	

	
	private String privateKey;
	
	private String publicKey;
	
	private String address;
	
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "ResponseNewKeyPairDTO [privateKey=" + privateKey + ", publicKey=" + publicKey + ", address=" + address
				+ "]";
	}

	
	
	
}
