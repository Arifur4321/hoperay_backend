package com.mediatica.vouchain.servicesImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.QrCodeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.TransactionDetailDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dao.VoucherDaoImpl;
import com.mediatica.vouchain.dto.DestinationDTO;
import com.mediatica.vouchain.dto.RequestBroadCastRawTxDTO;
import com.mediatica.vouchain.dto.RequestBurnVouchersDTO;
import com.mediatica.vouchain.dto.RequestGetVoucherBalancesDTO;
import com.mediatica.vouchain.dto.RequestSignRawTxDTO;
import com.mediatica.vouchain.dto.RequestTransferVoucherFromMany;
import com.mediatica.vouchain.dto.ResponseBroadcastRawTxDTO;
import com.mediatica.vouchain.dto.ResponseGetVoucherBalancesDTO;
import com.mediatica.vouchain.dto.ResponseSignRawTxDTO;
import com.mediatica.vouchain.dto.ResponseTransferVoucherFromManyDTO;
import com.mediatica.vouchain.dto.TransferDTO;
import com.mediatica.vouchain.dto.VoucherAllocationDTO;
import com.mediatica.vouchain.dto.VoucherBlckChainDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.TransactionDetailPK;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.VoucherYetInTheSystemException;
import com.mediatica.vouchain.export.InvoiceServiceImpl;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.EmailServiceInterface;
import com.mediatica.vouchain.servicesInterface.VoucherServiceInterface;

@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class VoucherServiceImpl implements VoucherServiceInterface {

	@Autowired
	VoucherDaoImpl dao;
	
	@Autowired
	TransactionDaoImpl transactionDao;

	@Autowired
	TransactionDetailDaoImpl transactionDetailDao;
	
	@Autowired
	QrCodeDaoImpl qrDao;
	
	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Autowired
	EmailServiceInterface emailServiceInterface;
	
	@Autowired
	InvoiceServiceImpl invoiceService;

	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Value("${voucher_prefix}")
	private String voucherPrefix;
	
	@Value("${voucher_yet_in_blockchain}")
	private String voucherYetInTheSystem;

	@Value("${elapsed_days_number}")
	private int elapsedDaysNumber;
	
	@Value("${simula_crash}")
	private String simula_crash;
	
	@Value("${qrcode_causale_base}")
	private String qr_causale_base;


 
	
	@Override
	public VoucherDTO addNewVoucherType(VoucherDTO voucherDTO) throws ParseException,VoucherYetInTheSystemException, Exception {
		     
		VoucherDTO response= new VoucherDTO();
		Voucher voucher = (Voucher)voucherDTO.unwrap(voucherDTO);
		voucher.setVchEndDate(calculateEndDate());
		voucher.setVchCreationDate(new Date());
		String voucherName = buildVoucherName(voucher);
		voucher.setVchName(voucherName);
		voucher.setVchState(false);
		voucherDTO.setVchName(voucherName);
	

		dao.insert(voucher);
		Transaction transaction = new Transaction();
		transaction.setTrcDate(new Date());
	//	transaction.setTrcState(TransactionStatus.PENDING.getValue());
		transaction.setTrcType(TransactionType.COMPANY_BUY_VOUCHER.getDescription());
		transaction.setTrcPayed(Constants.TRANSACTION_NOT_PAYED);
		User user = new User();
		if(voucherDTO.getCompanyId()!=null) {
			user = userDao.findByPrimaryKey(user, Integer.parseInt(voucherDTO.getCompanyId()));
		}
		transaction.setUsrIdA(user);
		transactionDao.insert(transaction);
		
		TransactionDetail td = new TransactionDetail();
		td.setTransaction(transaction);
		td.setVoucher(voucher);
		TransactionDetailPK pk = new TransactionDetailPK();
		pk.setTrcId(transaction.getTrcId());
		pk.setVchName(voucher.getVchName());
		td.setTransactionDetailPK(pk);
		if(voucherDTO.getVchQuantity()!=null) {
			td.setTrdQuantity(Long.parseLong(voucherDTO.getVchQuantity()));
		}
		transactionDetailDao.insert(td);
		List<TransactionDetail> detailList = new ArrayList<TransactionDetail>();
		detailList.add(td);
		File xmlFile= invoiceService.generateInvoiceXML(transaction,detailList);
		byte[] xmlContent= Files.readAllBytes(xmlFile.toPath());
		transaction.setTrcInvoiceXml(xmlContent);
		File xslFile = new File(Constants.VOUCHAIN_HOME+Constants.XSL_TEMPLATE_SUBDIR_PATH+Constants.XSL_TEMPLATE);
		byte[] pdfContent=invoiceService.generatePdfFromXmlXslt(xmlFile,xslFile,user.getUsrEmail());
		transaction.setTrcInvoice(pdfContent);
		transactionDao.update(transaction);
		
		response = (VoucherDTO)response.wrap(voucher);
		return response;
	}
	
	
		private Date calculateEndDate() {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, elapsedDaysNumber);  
			return cal.getTime();
		}


		private String buildVoucherName(Voucher voucher) {
			String date = Constants.FORMATTER_YYYY_MM_DD_UNDERSCORE.format(voucher.getVchEndDate());
			String voucherName = voucherPrefix+voucher.getVchValue()+"_"+date;
			return voucherName;
		}




	@Override
	public List<VoucherDTO> getPurchasableVouchersList() {
		List<VoucherDTO> result = new ArrayList<VoucherDTO>();
		List<Voucher> list = dao.getPurchasableVoucherList(new Date());
		if(list!=null) {
			for(Voucher item:list) {
				VoucherDTO dto = new VoucherDTO();
				result.add((VoucherDTO)dto.wrap(item));
			}
		}
		return result;
		
	}


 

	@Override
	public List<VoucherDTO> purchaseVoucherList(List<VoucherDTO> voucherList, String usrId) throws ParseException, Exception {
		List<VoucherDTO> resp = new ArrayList<VoucherDTO>();
	
    	Transaction transaction = new Transaction();
    	transaction.setTrcDate(new Date());
    	transaction.setTrcType(TransactionType.COMPANY_BUY_VOUCHER.getDescription());
    	transaction.setTrcPayed(Constants.TRANSACTION_NOT_PAYED);
    //	transaction.setTrcState(TransactionStatus.PENDING.getValue());
    	User company = new User();
    	if(usrId!=null) {
        	company= userDao.findByPrimaryKey(company,Integer.parseInt(usrId));
        	transaction.setUsrIdA(company);
        	transactionDao.insert(transaction);
        	List<TransactionDetail> detailList = new ArrayList<TransactionDetail>();
    		for(VoucherDTO item:voucherList) {
    	    	TransactionDetail trxDetail = new TransactionDetail();
    	    	Voucher voucher = dao.findByName(item.getVchName());
    	    	trxDetail.setVoucher(voucher); 
    	    	trxDetail.setTransaction(transaction);
    	    	if(item.getVchQuantity()!=null) {
        	    	trxDetail.setTrdQuantity(Long.parseLong(item.getVchQuantity()));
    	    	}
    	    	TransactionDetailPK pk= new TransactionDetailPK();
    	    	pk.setTrcId(transaction.getTrcId());
    	    	pk.setVchName(item.getVchName());
    	    	trxDetail.setTransactionDetailPK(pk);
    	    	transactionDetailDao.insert(trxDetail);
    	    	detailList.add(trxDetail);
    	    	item.setCompanyId(usrId);
    	    	resp.add(item);
    	    }

    		File xmlFile= invoiceService.generateInvoiceXML(transaction,detailList);
    		byte[] xmlContent= Files.readAllBytes(xmlFile.toPath());
    		transaction.setTrcInvoiceXml(xmlContent);
    		File xslFile = new File(Constants.VOUCHAIN_HOME+Constants.XSL_TEMPLATE_SUBDIR_PATH+Constants.XSL_TEMPLATE);
    		byte[] pdfContent=invoiceService.generatePdfFromXmlXslt(xmlFile,xslFile,company.getUsrEmail());
    		transaction.setTrcInvoice(pdfContent);
    		transactionDao.update(transaction);
    	}
		return resp;
	}
	
	@Override
	public List<VoucherDTO> getActiveVoucherList(String companyId) throws Exception {
		return getExpendableVouchersList(companyId);
	}
	
	@Override
	public List<VoucherDTO> getExpendedVouchersList(String merchantId) throws Exception {
		return getExpendableVouchersList(merchantId);
	}

	public List<VoucherDTO> getNotExpiredVouchers(String companyId) throws Exception {
		return getNotExpiredVouchersList(companyId);
	}
	


    
	public List<VoucherDTO> getNotExpiredVouchersList(String employeeId) throws Exception {
		List<VoucherDTO> result = new ArrayList<VoucherDTO>();
		//VoucherDTO voucherDTO = new VoucherDTO();
		User employee = new User();
		employee = userDao.findByPrimaryKey(employee, Integer.parseInt(employeeId));
		if(employee!=null) {
			String bckAddress = employee.getUsrBchAddress();
			log.info("Calling BlockChain Services in getNotExpiredVouchersList: {}",new Date());
			RequestGetVoucherBalancesDTO requestGetVoucherBalances = new RequestGetVoucherBalancesDTO();
			requestGetVoucherBalances.setAddress(bckAddress);
	
			ResponseGetVoucherBalancesDTO responseGetVoucherBalances = blockChainClient.invokeGetVoucherBalances(requestGetVoucherBalances);
			if(responseGetVoucherBalances.getError()==null || responseGetVoucherBalances.getError().isEmpty()) {
			 List<VoucherBlckChainDTO> list = responseGetVoucherBalances.getResult();
			 if(list!=null && !list.isEmpty()) {
                  
  
				   System.out.println("**************************"+ list.size());
				 			 
				 for(VoucherBlckChainDTO item:list) {
						VoucherDTO voucherDTO = new VoucherDTO();
					
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							String dataStr =Constants.FORMATTER_YYYY_MM_DD.format(new Date());
                   

                            
		         	voucherDTO = (VoucherDTO)voucherDTO.wrapNotExpired(dao.findByName(item.getName()));
					 	System.out.println("  voucherDTO.getVchEndDate()"  + voucherDTO.getVchEndDate());
					   	System.out.println("  dataStr" + dataStr);
					 	if (voucherDTO.getVchEndDate().compareTo(dataStr) > 0  )  {

							 	  	System.out.println("  *********inside the method ");
                       Locale locale = Locale.ITALY;        
				 
						voucherDTO.setVchQuantity(""+item.getQty());
								result.add(voucherDTO); 


						 }
				 }
                   
                //  Collections.sort(result, (o1, o2) -> o2.getVchEndDate().compareTo(o1.getVchEndDate()));

				 //Collections.sort(result);
                 

			 //Collections.reverse(result);
			 
			 }
	
			}else{
				throw new Exception("Error Invoking blockchain: "+responseGetVoucherBalances.getError());
			}
		}
		System.out.println("result list: "  + result);
		return result;
	}
	


	@Override
	public List<VoucherDTO> getExpendableVouchersList(String employeeId) throws Exception {
		List<VoucherDTO> result = new ArrayList<VoucherDTO>();
		//VoucherDTO voucherDTO = new VoucherDTO();
		User employee = new User();
		employee = userDao.findByPrimaryKey(employee, Integer.parseInt(employeeId));
		if(employee!=null) {
			String bckAddress = employee.getUsrBchAddress();
			log.info("Calling BlockChain Services in getExpendableVouchersList: {}",new Date());
			RequestGetVoucherBalancesDTO requestGetVoucherBalances = new RequestGetVoucherBalancesDTO();
			requestGetVoucherBalances.setAddress(bckAddress);
	
			ResponseGetVoucherBalancesDTO responseGetVoucherBalances = blockChainClient.invokeGetVoucherBalances(requestGetVoucherBalances);
			if(responseGetVoucherBalances.getError()==null || responseGetVoucherBalances.getError().isEmpty()) {
			 List<VoucherBlckChainDTO> list = responseGetVoucherBalances.getResult();
			 if(list!=null && !list.isEmpty()) {
             

				 for(VoucherBlckChainDTO item:list) {
						VoucherDTO voucherDTO = new VoucherDTO();
						voucherDTO = (VoucherDTO)voucherDTO.wrap(dao.findByName(item.getName()));
						Locale locale = Locale.ITALY;
						
                        String string = NumberFormat.getNumberInstance(locale).format(item.getQty());
					 
                        voucherDTO.setVchQuantity(""+ string  );
						result.add(voucherDTO);
				 }

                  Collections.sort(result, (o1, o2) -> o2.getVchEndDate().compareTo(o1.getVchEndDate()));

				 //Collections.sort(result);
                 

			 //Collections.reverse(result);
			 
			 }
	
			}else{
				throw new Exception("Error Invoking blockchain: "+responseGetVoucherBalances.getError());
			}
		}
		System.out.println("result list" + result);
		
		return result;
	}
	
	
	public List<VoucherAllocationDTO> saveInDBAllocationVoucher(List<VoucherAllocationDTO> allocationList)throws Exception{
		log.debug("allocateVouchers started: {}",new Date());
		String bckChainFromAddress=null;
		String fromPrivateKey=null;
		
		for(VoucherAllocationDTO item:allocationList){
			log.debug("evaluating voucherAllocationItem: {}",item);
			String fromId= item.getFromId();
			log.debug("fromId: {}",fromId);
			User fromUser = new User();
			fromUser = userDao.findByPrimaryKey(fromUser, Integer.parseInt(fromId));
			item.setBckChainFromAddress(fromUser.getUsrBchAddress());
			
			//I set only the first time the fromAddress is always the same
			bckChainFromAddress = fromUser.getUsrBchAddress();
			fromPrivateKey =fromUser.getUsrPrivateKey();
			item.setBckChainFromAddress(bckChainFromAddress);
			item.setPrivateKey(fromPrivateKey);
		
			String toId=item.getToId();
			User toUser = null;
			if(toId!=null && !toId.isEmpty()) {
				log.debug("toId: {}",toId);
				toUser = new User();
				toUser= userDao.findByPrimaryKey(toUser, Integer.parseInt(toId));
				item.setBckChainToAddress(toUser.getUsrBchAddress());
			}
			List<VoucherDTO> voucherList = item.getVoucherList();
			

			Transaction transaction = new Transaction();
			
			transaction.setUsrIdDa(fromUser);
			transaction.setUsrIdA(toUser);
			transaction.setTrcDate(new Date());
			transaction.setTrcState(TransactionStatus.PENDING.getValue());
			
			if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.COMPANY.name())) {
				transaction.setTrcType(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription());				
			}else if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.EMPLOYEE.name())) {
				transaction.setTrcType(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription());				
			}else if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.MERCHANT.name())) {
				transaction.setTrcType(TransactionType.REDEEM_VOUCHER.getDescription());	
				transaction.setTrcIban(item.getIban());
				transaction.setTrcAccountHolder(item.getAccountHolder());
				transaction.setTrcPayed(Constants.TRANSACTION_NOT_PAYED);
			}else {
				log.error("No profile found for profile :{}",item.getProfile());
				throw new Exception("No profile found");
			}

			
			transactionDao.insert(transaction);
			log.debug("inserted transaction with id: {}",transaction.getTrcId());
			item.setTransactionId(String.valueOf(transaction.getTrcId()));
			
			for(VoucherDTO voucherDTO:voucherList) {
				TransactionDetail td = new TransactionDetail();
				log.debug("Evaluating VoucherDTO: {}",voucherDTO);
				Voucher voucher = dao.findByName(voucherDTO.getVchName());
				td.setVoucher(voucher);
				td.setTransaction(transaction);
				td.setTrdQuantity(Long.parseLong(voucherDTO.getVchQuantity()));
				TransactionDetailPK pk = new TransactionDetailPK();
				pk.setVchName(voucherDTO.getVchName());
				pk.setTrcId(transaction.getTrcId());
				td.setTransactionDetailPK(pk);
				transactionDetailDao.insert(td);
				
				log.debug("transaction detail inserted");
			}



			emailServiceInterface.voucherConfeMail2(transaction);
		}
		
		log.debug("Calling the blockchain functions ");
		return allocationList;
	}
	
	

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void allocateVouchers(List<VoucherAllocationDTO> allocationList) throws Exception {

		log.debug("allocateVouchers started: {}",new Date());
		String bckChainFromAddress=null;
		String fromPrivateKey=null;
		
		Calendar cal = Calendar.getInstance();
		String timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		if(allocationList != null && !allocationList.isEmpty()) {
			log.info("INIZIO SERVICE TRANSACTION"+ timeString + "ID: "+ allocationList.get(0).getFromId());
		}else {
			log.info("INIZIO SERVICE TRANSACTION"+ timeString + "ID: NULL");
		}
		
		for(VoucherAllocationDTO item:allocationList){
			log.debug("evaluating voucherAllocationItem: {}",item);
			String fromId= item.getFromId();
			log.debug("fromId: {}",fromId);
			User fromUser = new User();
			fromUser = userDao.findByPrimaryKey(fromUser, Integer.parseInt(fromId));
			item.setBckChainFromAddress(fromUser.getUsrBchAddress());
			
			//I set only the first time the fromAddress is always the same
			bckChainFromAddress = fromUser.getUsrBchAddress();
			fromPrivateKey =fromUser.getUsrPrivateKey();
			item.setBckChainFromAddress(bckChainFromAddress);
			item.setPrivateKey(fromPrivateKey);
		
			String toId=item.getToId();
			User toUser = null;
			if(toId!=null && !toId.isEmpty()) {
				log.debug("toId: {}",toId);
				toUser = new User();
				toUser= userDao.findByPrimaryKey(toUser, Integer.parseInt(toId));
				item.setBckChainToAddress(toUser.getUsrBchAddress());
			}
			List<VoucherDTO> voucherList = item.getVoucherList();
			
			Transaction transaction = new Transaction();
			
			transaction.setUsrIdDa(fromUser);
			transaction.setUsrIdA(toUser);
			transaction.setTrcDate(new Date());
			transaction.setTrcState(TransactionStatus.PENDING.getValue());
			
			QrCode qrCode = null;
			
			if(item.getQrValue()!=null) {
				qrCode = qrDao.findQrCodeByValue(item.getQrValue());
				transaction.setTrcQrValue(qrCode.getQrValue());
				
				String causale;
				
				if(qrCode.getQrCash()!=null) {
					causale = qr_causale_base+ " a cassa #"+qrCode.getQrCash();
				}
				else {
					causale = qr_causale_base;
				}
				
				transaction.setTrcQrCausale(causale);
			}
			
			if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.COMPANY.name())) {
				transaction.setTrcType(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription());				
			}else if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.EMPLOYEE.name())) {
				transaction.setTrcType(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription());				
			}else if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.MERCHANT.name())) {
				transaction.setTrcType(TransactionType.REDEEM_VOUCHER.getDescription());	
				transaction.setTrcIban(item.getIban());
				transaction.setTrcAccountHolder(item.getAccountHolder());
				transaction.setTrcPayed(Constants.TRANSACTION_NOT_PAYED);
			}else {
				log.error("No profile found for profile :{}",item.getProfile());
				throw new Exception("No profile found");
			}
			cal = Calendar.getInstance();
			timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
			
			log.info("PRIMA DI INSERIMENTO DB TRANSACTION"+timeString + "ID: "+fromId);

			
			transactionDao.insert(transaction);
			
			cal = Calendar.getInstance();
			timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
			log.info("DOPO INSERIMENTO DB TRANSACTION"+timeString + "ID: "+fromId);
			
			log.debug("inserted transaction with id: {}",transaction.getTrcId());
			item.setTransactionId(String.valueOf(transaction.getTrcId()));
			
			for(VoucherDTO voucherDTO:voucherList) {
				TransactionDetail td = new TransactionDetail();
				log.debug("Evaluating VoucherDTO: {}",voucherDTO);
				Voucher voucher = dao.findByName(voucherDTO.getVchName());
				td.setVoucher(voucher);
				td.setTransaction(transaction);
				td.setTrdQuantity(Long.parseLong(voucherDTO.getVchQuantity()));
				TransactionDetailPK pk = new TransactionDetailPK();
				pk.setVchName(voucherDTO.getVchName());
				pk.setTrcId(transaction.getTrcId());
				td.setTransactionDetailPK(pk);
				transactionDetailDao.insert(td);
				
				log.debug("transaction detail inserted");
			}



			
		}
		
		log.debug("Calling the blockchain functions ");
		
		cal = Calendar.getInstance();
		timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		if(allocationList != null && !allocationList.isEmpty()) {
			log.info("PRIMA DI BLOCKCHAIN TRANSACTION"+ timeString + "ID: "+ allocationList.get(0).getFromId());
		}else {
			log.info("PRIMA DI BLOCKCHAIN TRANSACTION"+ timeString + "ID: NULL");
		}
		
		for(VoucherAllocationDTO item:allocationList){
			String txId=null;
				ResponseTransferVoucherFromManyDTO responseTransferVoucherFromMany = null;
				if(item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.COMPANY.name())||item.getProfile().equalsIgnoreCase(Constants.ENTITIES_TYPE.EMPLOYEE.name())) {
					RequestTransferVoucherFromMany request = new RequestTransferVoucherFromMany();
					request.setAddress_from(item.getBckChainFromAddress());
					request.setDestinations(buildDestinationRequest(item.getVoucherList(),item.getBckChainToAddress()));
					responseTransferVoucherFromMany = blockChainClient.invokeTransferVoucherFromMany(request);
				}else {
					RequestBurnVouchersDTO request = new RequestBurnVouchersDTO();
					request.setAddress_from(item.getBckChainFromAddress());
					request.setTransfer(buildTransferRequest(item.getVoucherList()));
					responseTransferVoucherFromMany = blockChainClient.invokeBurnVouchers(request);
				}
				cal = Calendar.getInstance();
				timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
					log.info("NUOVO LOG DOPO BLOCKCHAIN TRANSACTION"+ timeString + "ID: "+ item.getFromId());
				if(responseTransferVoucherFromMany.getError()==null || responseTransferVoucherFromMany.getError().isEmpty()) {
					String rawTx = responseTransferVoucherFromMany.getResult().getRaw_tx();
					RequestSignRawTxDTO requestSignRawTx  = new RequestSignRawTxDTO();
					requestSignRawTx.setRawtx(rawTx);
					requestSignRawTx.setPrivate_key(item.getPrivateKey());
					ResponseSignRawTxDTO responseSignRawDTO = invokeSignRaw(requestSignRawTx);
					String rawtx  = responseSignRawDTO.getResult().getRaw_tx();
					RequestBroadCastRawTxDTO requestBroadcast= new RequestBroadCastRawTxDTO();
					requestBroadcast.setRawtx(rawtx);
					ResponseBroadcastRawTxDTO responseBroadCastRawTx = invokeBroadCastRawTx(requestBroadcast);
					txId= responseBroadCastRawTx.getResult();
				}else {
					log.error("Blockchain Message: {},{}",responseTransferVoucherFromMany.getError(),responseTransferVoucherFromMany.getMessage());
					throw new Exception("Error Invoking blockchain: "+responseTransferVoucherFromMany.getError());
				}
				if(simula_crash==null ||  !simula_crash.equals("1")) {
					try {
						updateTransactionId(item,txId);
					}catch(Exception e) {
						log.warn("some problem updating transaction let's continue... the problem will be managed by recovery scheduler");
					}
				}
				Transaction transaction = new Transaction();
				transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(item.getTransactionId()));
				emailServiceInterface.voucherConfeMail2(transaction);
		
		}		
		
		log.debug("allocateVouchers ended: {}",new Date());
		
		cal = Calendar.getInstance();
		timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		if(allocationList != null && !allocationList.isEmpty()) {
			log.info("DOPO BLOCKCHAIN TRANSACTION"+ timeString + "ID: "+ allocationList.get(0).getFromId());
		}else {
			log.info("DOPO BLOCKCHAIN TRANSACTION"+ timeString + "ID: NULL");
		}
	}



	private void updateTransactionId(VoucherAllocationDTO item, String newTxId) {
		
			String txId = item.getTransactionId();
			Transaction transaction = new Transaction();
			transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(txId));
			transaction.setTrcTxId(newTxId);
			transaction.setTrcState(TransactionStatus.CONFIRMED.getValue());																									 
			transactionDao.update(transaction);
			
		
	}

	private ResponseBroadcastRawTxDTO invokeBroadCastRawTx(RequestBroadCastRawTxDTO request) throws Exception {
		ResponseBroadcastRawTxDTO responseBroadcastRawTxDTO = blockChainClient.invokeBroadcastRawTxDTO(request);
		if(responseBroadcastRawTxDTO.getError()!=null && !responseBroadcastRawTxDTO.getError().isEmpty()) {
			throw new Exception("Error Invoking invokeBroadCastRawTx: "+responseBroadcastRawTxDTO.getError());
		}
		return responseBroadcastRawTxDTO;
	}

	private ResponseSignRawTxDTO invokeSignRaw(RequestSignRawTxDTO requestSignRawTx) throws Exception {
		ResponseSignRawTxDTO responseSignRawDTO =  blockChainClient.invokeSignRaw(requestSignRawTx);
		if(responseSignRawDTO.getError()!=null && !responseSignRawDTO.getError().isEmpty()) {
			throw new Exception("Error Invoking invokeSignRaw: "+responseSignRawDTO.getError());
		}
		return responseSignRawDTO;
	}

	private List<DestinationDTO> buildDestinationRequest(List<VoucherDTO> allocationList,String blockChainToAddress) {
		List<DestinationDTO> destinationList = new ArrayList<DestinationDTO>();
		DestinationDTO destination = new DestinationDTO();
		destination.setAddress_to(blockChainToAddress);
		destination.setTransfer(buildTransferRequest(allocationList));
		destinationList.add(destination);
		return destinationList;
	}

	private List<TransferDTO> buildTransferRequest(List<VoucherDTO> voucherList) {
		List<TransferDTO> transferList= new ArrayList<TransferDTO>();
		for(VoucherDTO voucher:voucherList) {
			TransferDTO transfer = new TransferDTO();
			transfer.setName(voucher.getVchName());
			transfer.setQty(Integer.parseInt(voucher.getVchQuantity()));
			transferList.add(transfer);
		}
		return transferList;
	}
	
	




}
