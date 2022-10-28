package com.mediatica.vouchain.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * @author Pietro Napolitano
 *
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseNewKeyPairDTO {

	private ResponseNewKeyPairDetailDTO result;
	
	private String error;
	
	private String id;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "BlockChainDTO [result=" + result + ", error=" + error + ", id=" + id + "]";
	}

	public void setId(String id) {
		this.id = id;
	}

	public ResponseNewKeyPairDetailDTO getResult() {
		return result;
	}

	public void setResult(ResponseNewKeyPairDetailDTO result) {
		this.result = result;
	}
	
	
	
	
	
}
