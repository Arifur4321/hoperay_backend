package com.mediatica.vouchain.dto;

public class InvoiceDTO extends SimpleResponseDTO{

	private byte[] invoice;


	public byte[] getInvoice() {
		return invoice;
	}

	public void setInvoice(byte[] invoice) {
		this.invoice = invoice;
	}

}
