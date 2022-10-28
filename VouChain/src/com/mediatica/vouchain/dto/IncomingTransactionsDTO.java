package com.mediatica.vouchain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingTransactionsDTO {
	
	private String txid;
	private String sender;
	private String receiver;
	private List<AssetDTO> assets;
	
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public List<AssetDTO> getAssets() {
		return assets;
	}
	public void setAssets(List<AssetDTO> assets) {
		this.assets = assets;
	}
	
	
}
