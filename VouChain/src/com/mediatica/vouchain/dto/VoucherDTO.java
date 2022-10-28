package com.mediatica.vouchain.dto;

import java.math.BigDecimal;
 
import java.util.Comparator;
 

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.entities.Voucher;
 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoucherDTO extends DTO implements Comparable<VoucherDTO>{

    private String vchName;
    private String vchCreationDate;
    private String vchEndDate;
    private String vchValue;
    private String vchQuantity;
    private String companyId;
    private String vchState;
  
      
	
 

public Object wrap(Object entity) {
    	Voucher voucher = (Voucher)entity;
		VoucherDTO dto = new VoucherDTO();
    	dto.setCompanyId("");//TODO
    	if(voucher.getVchCreationDate()!=null) {
				System.out.println("wrong wrap start   : "  );
    		dto.setVchCreationDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchCreationDate()));
				System.out.println("wrong wrap end   : "  );
    	}
        

    	if( voucher.getVchEndDate()!=null) {
    		dto.setVchEndDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchEndDate()));
    	}
    	dto.setVchName(voucher.getVchName());
    	dto.setVchQuantity(""); //TODO
    	dto.setVchValue(voucher.getVchValue().toEngineeringString());
    	if(voucher.getVchState()!=null) {
    		dto.setVchState(""+voucher.getVchState());
    	}
    	System.out.println("dto list :" + dto );
    	return dto; 
    }


	
public Object wrapNotExpired(Object entity) {
    	Voucher voucher = (Voucher)entity;
		VoucherDTO dto = new VoucherDTO();
    	dto.setCompanyId("");//TODO
    	if(voucher.getVchCreationDate()!=null) {
				System.out.println("wrong wrap start   : "  );
    		dto.setVchCreationDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchCreationDate()));
				System.out.println("wrong wrap end   : "  );
    	}
        

    	if( voucher.getVchEndDate()!=null) {
    		dto.setVchEndDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchEndDate()));
    	}
    	dto.setVchName(voucher.getVchName());
    	dto.setVchQuantity(""); //TODO
    	dto.setVchValue(voucher.getVchValue().toEngineeringString());
    	if(voucher.getVchState()!=null) {
    		dto.setVchState(""+voucher.getVchState());
    	}
    	System.out.println("dto list :" + dto );
    	return dto; 
    }
	
	
  /* 
    public Object wrapNotExpired(Object entity) {
    	Voucher voucher = (Voucher)entity;
		VoucherDTO dto = new VoucherDTO();
    	dto.setCompanyId("");//TODO
    
		try {

        	if(voucher.getVchCreationDate()!=null) {
			 System.out.println("wrong start : " +voucher.getVchCreationDate() );

    		dto.setVchCreationDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchCreationDate()));
			System.out.println("wrong end  : "  );
    	}  

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
        String dataStr =Constants.FORMATTER_YYYY_MM_DD.format(new Date());
		
           System.out.println("wrong 1 : " + dataStr );

		int result =    Constants.FORMATTER_YYYY_MM_DD.format( voucher.getVchEndDate()).compareTo( dataStr);

		  System.out.println("wrong 2 : " +result );
    	
		if ( Constants.FORMATTER_YYYY_MM_DD.format( voucher.getVchEndDate()).compareTo( dataStr) > 0  ) {

    		dto.setVchEndDate(Constants.FORMATTER_YYYY_MM_DD.format(voucher.getVchEndDate()));
		     System.out.println("wrong 3 "  + voucher.getVchEndDate()  )   ;
		}
    	}
		catch (Exception e){

           System.out.println("Something went wrong.");
		}

           dto.setVchName(voucher.getVchName());
    	    dto.setVchQuantity(""); //TODO
    	    dto.setVchValue(voucher.getVchValue().toEngineeringString());
    	    if(voucher.getVchState()!=null) {
    		dto.setVchState(""+voucher.getVchState());

		}
		
    	
    	System.out.println("dto list :" + dto );
    	return dto; 
    }
	*/
	@Override
	public Object wrap(Object entity, boolean includePassword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object unwrap(Object dto) throws ParseException {
		Voucher voucher = new Voucher();
		VoucherDTO voucherDTO = (VoucherDTO)dto;
		if(voucherDTO.getVchCreationDate()!=null) {
			voucher.setVchCreationDate(Constants.FORMATTER_YYYY_MM_DD.parse(voucherDTO.getVchCreationDate()));
		}
		if(voucherDTO.getVchEndDate()!=null) {

            

			voucher.setVchEndDate(Constants.FORMATTER_YYYY_MM_DD.parse(voucherDTO.getVchEndDate()));
		}
		
		voucher.setVchName(voucherDTO.getVchName());
		if(voucherDTO.getVchValue()!=null && !voucherDTO.getVchValue().isEmpty()) {
			voucher.setVchValue(new BigDecimal(voucherDTO.getVchValue()));
		}
//		if(voucherDTO.getVchQuantity()!=null && !voucherDTO.getVchQuantity().isEmpty()) {
//			voucher.setVchQuantity(Long.parseLong(voucherDTO.getVchQuantity()));
//		}
		return voucher;
	}

	public String getVchCreationDate() {
		return vchCreationDate;
	}

	public void setVchCreationDate(String vchCreationDate) {
		this.vchCreationDate = vchCreationDate;
	}

	public String getVchEndDate() {
		return vchEndDate;
	}

	public void setVchEndDate(String vchEndDate) {
		this.vchEndDate = vchEndDate;
	}

	public String getVchValue() {
		return vchValue;
	}

	public void setVchValue(String vchValue) {
		this.vchValue = vchValue;
	}

	public String getVchName() {
		return vchName;
	}

	public void setVchName(String vchName) {
		this.vchName = vchName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getVchQuantity() {
		return vchQuantity;
	}

	public void setVchQuantity(String vchQuantity) {
		this.vchQuantity = vchQuantity;
	}



	@Override
	public String toString() {
		return "VoucherDTO [vchName=" + vchName + ", vchCreationDate=" + vchCreationDate + ", vchEndDate=" + vchEndDate
				+ ", vchValue=" + vchValue + ", vchQuantity=" + vchQuantity + ", companyId=" + companyId
				+  "]";
	}

	public String getVchState() {
		return vchState;
	}

	public void setVchState(String vchState) {
		this.vchState = vchState;
	}

	@Override
	public int compareTo(VoucherDTO o) {
	    return Double.valueOf(this.getVchValue()).compareTo(Double.valueOf(o.getVchValue()));
	}
	
	
	
	

}
