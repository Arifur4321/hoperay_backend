package com.mediatica.vouchain.dto;

import java.util.List;

public class RequestTransferVoucherFromMany {

	private String address_from;
	private List<DestinationDTO> destinations;

	public List<DestinationDTO> getDestinations() {
		return destinations;
	}
	public void setDestinations(List<DestinationDTO> destinations) {
		this.destinations = destinations;
	}
	public String getAddress_from() {
		return address_from;
	}
	public void setAddress_from(String address_from) {
		this.address_from = address_from;
	}
	
}
