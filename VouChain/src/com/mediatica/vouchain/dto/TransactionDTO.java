package com.mediatica.vouchain.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;

public class TransactionDTO extends DTO {
	
	private String usrIdDa;
	private String usrIdA;
	private String usrIdDaString;
	private String usrIdAString;
	private String trcId;
	private String trcTxId;
	private String trcType;
	private String trcDate;
	private String trcState; //confirmed - pending
	private String trcIban;
	private String trcPayed; //payed - not_payed
	private String trcMailSent;
	private String trcCancDate;
	private String trcQrValue;
	private String trcQrCausale;
    private byte[] trcInvoice;
    private byte[] trcInvoiceXml;
    private byte[] transactionListExcel;
    private String vchValue;
    private String trcAccountHolder;


    
    //transaction detail 
    private String trcValue; //transaction value sum(vchValue*trdQuantity)
	private List<VoucherDTO> voucherList;


	
    private static final long serialVersionUID = 1L;
    
    
    

	public String getTrcId() {
		return trcId;
	}

	public void setTrcId(String trcId) {
		this.trcId = trcId;
	}

	public String getTrcTxId() {
		return trcTxId;
	}

	public void setTrcTxId(String trcTxId) {
		this.trcTxId = trcTxId;
	}

	public String getTrcType() {
		return trcType;
	}

	public void setTrcType(String trcType) {
		this.trcType = trcType;
	}

	public String getTrcDate() {
		return trcDate;
	}

	public void setTrcDate(String trcDate) {
		this.trcDate = trcDate;
	}

	public String getTrcState() {
		return trcState;
	}

	public void setTrcState(String trcState) {
		this.trcState = trcState;
	}

	public String getTrcIban() {
		return trcIban;
	}

	public void setTrcIban(String trcIban) {
		this.trcIban = trcIban;
	}

	public String getTrcPayed() {
		return trcPayed;
	}

	public void setTrcPayed(String trcPayed) {
		this.trcPayed = trcPayed;
	}

	public String getTrcMailSent() {
		return trcMailSent;
	}

	public void setTrcMailSent(String trcMailSent) {
		this.trcMailSent = trcMailSent;
	}

	public String getTrcCancDate() {
		return trcCancDate;
	}

	public void setTrcCancDate(String trcCancDate) {
		this.trcCancDate = trcCancDate;
	}

	public byte[] getTrcInvoice() {
		return trcInvoice;
	}

	public void setTrcInvoice(byte[] trcInvoice) {
		this.trcInvoice = trcInvoice;
	}

	/**
	 * @return the usrIdDa
	 */
	public String getUsrIdDa() {
		return usrIdDa;
	}

	/**
	 * @param usrIdDa the usrIdDa to set
	 */
	public void setUsrIdDa(String usrIdDa) {
		this.usrIdDa = usrIdDa;
	}

	/**
	 * @return the usrIdA
	 */
	public String getUsrIdA() {
		return usrIdA;
	}

	/**
	 * @param usrIdA the usrIdA to set
	 */
	public void setUsrIdA(String usrIdA) {
		this.usrIdA = usrIdA;
	}
	



	public String getUsrIdDaString() {
		return usrIdDaString;
	}

	public void setUsrIdDaString(String usrIdDaString) {
		this.usrIdDaString = usrIdDaString;
	}

	public String getUsrIdAString() {
		return usrIdAString;
	}

	public void setUsrIdAString(String usrIdAString) {
		this.usrIdAString = usrIdAString;
	}

	public String getTrcQrValue() {
		return trcQrValue;
	}

	public void setTrcQrValue(String trcQrValue) {
		this.trcQrValue = trcQrValue;
	}

	public String getTrcQrCausale() {
		return trcQrCausale;
	}

	public void setTrcQrCausale(String trcQrCausale) {
		this.trcQrCausale = trcQrCausale;
	}

	@Override
	public Object unwrap(Object entity) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public Object wrap(Object entity, boolean listForExport) {
    	Transaction transaction = (Transaction)entity;
    	TransactionDTO dto = new TransactionDTO();
    	List<VoucherDTO> voucherList = new ArrayList<VoucherDTO>();
    	dto.setVoucherList(voucherList);
    	double trcValue = 0.0;
    	
		if(transaction!=null) {
			if(transaction.getTrcId()!=null) {
				dto.setTrcId(transaction.getTrcId().toString());
			}
			dto.setTrcTxId(transaction.getTrcTxId());
			
			if(listForExport) {
				//set data for User (DA)
				if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getCompany()!=null) {
					dto.setUsrIdDa(transaction.getUsrIdDa().getCompany().getCpyRagioneSociale());
				}
				else if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getMerchant()!=null) {
					dto.setUsrIdDa(transaction.getUsrIdDa().getMerchant().getMrcRagioneSociale());
				}
				else if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getEmployee()!=null) {
					dto.setUsrIdDa(
							transaction.getUsrIdDa().getEmployee().getEmpFirstName() + " "
							+ transaction.getUsrIdDa().getEmployee().getEmpLastName());
				}		
				
				//set data for User (A)
				if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getCompany()!=null) {
					dto.setUsrIdA(transaction.getUsrIdA().getCompany().getCpyRagioneSociale());
				}
				else if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getMerchant()!=null) {
					dto.setUsrIdA(transaction.getUsrIdA().getMerchant().getMrcRagioneSociale());
				}
				else if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getEmployee()!=null) {
					dto.setUsrIdA(
							transaction.getUsrIdA().getEmployee().getEmpFirstName() + " "
							+ transaction.getUsrIdA().getEmployee().getEmpLastName());
				}	
			}
			else {
				if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getUsrId()!=null) {
					dto.setUsrIdDa(transaction.getUsrIdDa().getUsrId().toString());
				}
				if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getUsrId()!=null) {
					dto.setUsrIdA(transaction.getUsrIdA().getUsrId().toString());
				}

				//Added for new Requirements
				if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getCompany()!=null) {
					dto.setUsrIdDaString(transaction.getUsrIdDa().getCompany().getCpyRagioneSociale());
				}
				else if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getMerchant()!=null) {
					dto.setUsrIdDaString(transaction.getUsrIdDa().getMerchant().getMrcRagioneSociale());
				}
				else if(transaction.getUsrIdDa()!=null && transaction.getUsrIdDa().getEmployee()!=null) {
					dto.setUsrIdDaString(
							transaction.getUsrIdDa().getEmployee().getEmpFirstName() + " "
							+ transaction.getUsrIdDa().getEmployee().getEmpLastName());
				}		
				
				//set data for User (A)
				if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getCompany()!=null) {
					dto.setUsrIdAString(transaction.getUsrIdA().getCompany().getCpyRagioneSociale());
				}
				else if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getMerchant()!=null) {
					dto.setUsrIdAString(transaction.getUsrIdA().getMerchant().getMrcRagioneSociale());
				}
				else if(transaction.getUsrIdA()!=null && transaction.getUsrIdA().getEmployee()!=null) {
					dto.setUsrIdAString(
							transaction.getUsrIdA().getEmployee().getEmpFirstName() + " "
							+ transaction.getUsrIdA().getEmployee().getEmpLastName());
				}
				
			}

			if(transaction.getTrcState()!=null) {
				dto.setTrcState(transaction.getTrcState()?Constants.TRANSACTION_STATUS_CONFIRMED_STRING:Constants.TRANSACTION_STATUS_PENDING_STRING);
			}
			if(transaction.getTrcPayed()!=null) {
				dto.setTrcPayed(transaction.getTrcPayed()?Constants.TRANSACTION_PAYED_STRING:Constants.TRANSACTION_NOT_PAYED_STRING);
			}
			dto.setTrcType(transaction.getTrcType());
			if(transaction.getTrcDate()!=null) {
				dto.setTrcDate(Constants.FORMATTER_DD_MM_YYYY.format(transaction.getTrcDate()));
			}
			dto.setTrcIban(transaction.getTrcIban());
			dto.setTrcAccountHolder(transaction.getTrcAccountHolder());
			if(transaction.getTrcCancDate()!=null) {
				dto.setTrcCancDate(Constants.FORMATTER_DD_MM_YYYY.format(transaction.getTrcCancDate()));
			}
//			dto.setTrcInvoice(transaction.getTrcInvoice());
//			dto.setTrcInvoiceXml(transaction.getTrcInvoiceXml());
			if(transaction.getTrcMailSent()!=null) {
				dto.setTrcMailSent(transaction.getTrcMailSent()?"1":"0");
			}
			if(transaction.getTransactionDetailCollection()!=null && transaction.getTransactionDetailCollection().size()>0) {
				for(TransactionDetail trxDetail : transaction.getTransactionDetailCollection()) {
					Long trdQuantity = trxDetail.getTrdQuantity();
					if(trxDetail.getVoucher()!=null) {
						BigDecimal vchValue = trxDetail.getVoucher().getVchValue();
						if(trdQuantity!=null && vchValue!=null) {
							trcValue+=trdQuantity*vchValue.doubleValue();	
						}
						VoucherDTO voucher = new VoucherDTO();
						voucher.setVchName(trxDetail.getVoucher().getVchName());
						voucher.setVchQuantity(String.valueOf(trdQuantity));
						voucher.setVchValue(String.valueOf(vchValue));
						dto.getVoucherList().add(voucher);
					}
					
				}				
			
				trcValue = Math.floor(trcValue * 100) / 100;				
				dto.setTrcValue(String.valueOf(trcValue));	
			}
			if(transaction.getTrcQrValue()!=null) {
				dto.setTrcQrValue(transaction.getTrcQrValue());
			}
			if(transaction.getTrcQrCausale()!=null) {
				dto.setTrcQrCausale(transaction.getTrcQrCausale());
			}
		
		}
    	
    	return dto; 
    }

    /**
	 * @return the transactionListExcel
	 */
	public byte[] getTransactionListExcel() {
		return transactionListExcel;
	}

	/**
	 * @param transactionListExcel the transactionListExcel to set
	 */
	public void setTransactionListExcel(byte[] transactionListExcel) {
		this.transactionListExcel = transactionListExcel;
	}

	/**
	 * @return the trcValue
	 */
	public String getTrcValue() {
		return trcValue;
	}

	/**
	 * @param trcValue the trcValue to set
	 */
	public void setTrcValue(String trcValue) {
		this.trcValue = trcValue;
	}

	/**
	 * @return the voucherList
	 */
	public List<VoucherDTO> getVoucherList() {
		return voucherList;
	}

	/**
	 * @param voucherList the voucherList to set
	 */
	public void setVoucherList(List<VoucherDTO> voucherList) {
		this.voucherList = voucherList;
	}

	public String getVchValue() {
		return vchValue;
	}

	public void setVchValue(String vchValue) {
		this.vchValue = vchValue;
	}

	public String getTrcAccountHolder() {
		return trcAccountHolder;
	}

	public void setTrcAccountHolder(String trcAccountHolder) {
		this.trcAccountHolder = trcAccountHolder;
	}

	public byte[] getTrcInvoiceXml() {
		return trcInvoiceXml;
	}

	public void setTrcInvoiceXml(byte[] trcInvoiceXml) {
		this.trcInvoiceXml = trcInvoiceXml;
	}




}
