package com.mediatica.vouchain.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.VoucherAllocationDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.VoucherYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.VoucherServiceImpl;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")  
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class VoucherRestController {
	
	@Autowired
	VoucherServiceImpl service;
	
	@Value("${generic_error}")
	private String genericError;

	@Value("${voucher_name_duplicated}")
	private String voucherNameDuplicated;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(VoucherRestController.class);
	
	

	
	
	
	//OK_BE_20 lista dei voucher a disposizione di una azienda (Wallet)
	@GetMapping("/api/vouchers/getActiveVoucherList/{companyId}")
	public DTOList<VoucherDTO>getActiveVoucherList(@PathVariable("companyId") String companyId){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getActiveVoucherList(companyId);
			response.setList(list);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in getPurchasableVouchersList",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response;
	}


	
	//OK_BE_19 lista dei voucher acquistabili
	@GetMapping("/api/vouchers/getPurchasableVouchersList")
	public DTOList<VoucherDTO> getPurchasableVouchersList(){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getPurchasableVouchersList();
			response.setList(list);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in getPurchasableVouchersList",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response;
	} 





	
	//OK_ BE_21 un azienda acquista i voucher
	@PostMapping("/api/vouchers/purchaseVouchers/{usrId}")
	public SimpleResponseDTO purchaseVouchers(@PathVariable("usrId") String usrId, @RequestBody List<VoucherDTO> voucherList) {
		SimpleResponseDTO response = new SimpleResponseDTO();
		try {
			service.purchaseVoucherList(voucherList,usrId);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in purchaseVouchers",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		       
		
		return response;
	}	
	
	
	
	//OK BE_22 l azienda inserisce una nuova tipologia di voucher
	@PostMapping("/api/vouchers/newVoucherType")
	public VoucherDTO newVoucherType(@RequestBody VoucherDTO voucherDTO) {
		VoucherDTO response = new VoucherDTO();
		try {
			response=service.addNewVoucherType(voucherDTO);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(PersistenceException | VoucherYetInTheSystemException ex) {
			log.error("Error in newVoucherType",ex);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(voucherNameDuplicated);			
		}catch(Exception e) {
			log.error("Error in newVoucherType",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response;
	}
	
	//OK BE_23/BE_25/BE_27 l azienda alloca i vocuher verso i beneficiari/ i beneficiari allocano vouchers verso i merchants/I merchants richiedono il redeem dei vouchers
	@PostMapping("/api/vouchers/voucherAllocation")
	public SimpleResponseDTO voucherAllocation( @RequestBody List<VoucherAllocationDTO> allocationList) {
		
		Calendar cal = Calendar.getInstance();
		String timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		if(allocationList != null && !allocationList.isEmpty()) {
			log.info("INIZIO REST VOUCHER ALLOCATION"+ timeString + "ID: "+ allocationList.get(0).getFromId());
		}else {
			log.info("INIZIO REST VOUCHER ALLOCATION"+ timeString + "ID: NULL");
		}
		
		SimpleResponseDTO response = new SimpleResponseDTO();
		try {
		//	allocationList = service.saveInDBAllocationVoucher(allocationList);
			log.info("MAX MEMORY TRANSACTION______"+Runtime.getRuntime().maxMemory());
			log.info("FREE MEMORY TRANSACTION______"+Runtime.getRuntime().freeMemory());
			log.info("TOTAL MEMORY TRANSACTION______"+Runtime.getRuntime().totalMemory());
			service.allocateVouchers(allocationList);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in voucherAllocation",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		
		cal = Calendar.getInstance();
		timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		if(allocationList != null && !allocationList.isEmpty()) {
			log.info("FINE REST VOUCHER ALLOCATION"+ timeString + "ID: "+ allocationList.get(0).getFromId());
		}else {
			log.info("FINE REST VOUCHER ALLOCATION"+ timeString + "ID: NULL");
		}
		
		return response;
	}
	
	//BE_24 la lista dei voucher a disposizione di un beneficiario (Wallet ) come metodo di service viene richiamato lo stesso della BE_20
	@GetMapping("/api/vouchers/getExpendableVouchersList/{employeeId}")
	public DTOList<VoucherDTO> getExpendableVouchersList(@PathVariable("employeeId") String employeeId){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getExpendableVouchersList(employeeId);
			response.setList(list);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in getExpendableVouchersList",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response; 
	}

	//OK_BE_20 lista dei voucher a disposizione di una azienda (Wallet)
	@GetMapping("/api/vouchers/getNotExpiredVouchers/{employeeId}")
	public DTOList<VoucherDTO>getNotExpiredVouchers(@PathVariable("employeeId") String employeeId){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getNotExpiredVouchersList(employeeId);;
			response.setList(list);
			System.out.println("response.setList(list)" + list );
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in getNotExpiredVouchers",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response;
	}


@GetMapping("/api/vouchers/getExpendableVouchersListnotExpired/{employeeId}")
	public DTOList<VoucherDTO>getExpendableVouchersListnotExpired(@PathVariable("employeeId") String employeeId){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getNotExpiredVouchersList(employeeId);;
			response.setList(list);
			System.out.println("response.setList(list)" + list );
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in getNotExpiredVouchers",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response;
	}
	
	//BE_26 la lista dei voucher a disposizione di un merchant (Wallet ) come metodo di service viene richiamato lo stesso della BE_20
	@GetMapping("/api/vouchers/getExpendedVoucherList/{merchantId}")
	public DTOList<VoucherDTO> getExpendedVoucherList(@PathVariable("merchantId") String merchantId){
		DTOList<VoucherDTO> response = new DTOList<VoucherDTO>();
		try {
			List<VoucherDTO> list = service.getExpendedVouchersList(merchantId);
			response.setList(list);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		}catch(Exception e) {
			log.error("Error in newVoucherType",e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}
		return response; 
	}
	

	

	
}


