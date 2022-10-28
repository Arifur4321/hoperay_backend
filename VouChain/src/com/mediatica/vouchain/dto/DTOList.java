package com.mediatica.vouchain.dto;

import java.util.HashMap;
import java.util.List;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class DTOList<E> {

	private String status;
	private String errorDescription;
	private List<E> list;
	
	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
