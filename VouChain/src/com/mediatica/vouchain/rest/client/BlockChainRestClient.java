package com.mediatica.vouchain.rest.client;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediatica.vouchain.dto.GetIncomingTransactionsRequestDTO;
import com.mediatica.vouchain.dto.GetIncomingTransactionsResponseDTO;
import com.mediatica.vouchain.dto.RequestBroadCastRawTxDTO;
import com.mediatica.vouchain.dto.RequestBurnVouchersDTO;
import com.mediatica.vouchain.dto.RequestGetVoucherBalancesDTO;
import com.mediatica.vouchain.dto.RequestGrantPermissionsDTO;
import com.mediatica.vouchain.dto.RequestIsTXConfirmedDTO;
import com.mediatica.vouchain.dto.RequestIssueMoreVoucher;
import com.mediatica.vouchain.dto.RequestIssueNewVoucherDTO;
import com.mediatica.vouchain.dto.RequestNewKeyPairDTO;
import com.mediatica.vouchain.dto.RequestSignRawTxDTO;
import com.mediatica.vouchain.dto.RequestTransferVoucherFromMany;
import com.mediatica.vouchain.dto.ResponseBroadcastRawTxDTO;
import com.mediatica.vouchain.dto.ResponseGetVoucherBalancesDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseIsTxCofirmedDTO;
import com.mediatica.vouchain.dto.ResponseIssueMoreVoucher;
import com.mediatica.vouchain.dto.ResponseIssueNewVoucherDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.dto.ResponseSignRawTxDTO;
import com.mediatica.vouchain.dto.ResponseTransferVoucherFromManyDTO;
import com.mediatica.vouchain.exceptions.InvokeBlockchainException;

import com.mediatica.vouchain.utilities.EncrptingUtils;


/**
 * 
 * @author Pietro Napolitano
 *
 */
@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class BlockChainRestClient {


	


	@Autowired
    private EncrptingUtils encrptingUtils;


	private static org.slf4j.Logger log = LoggerFactory.getLogger(BlockChainRestClient.class);
	

	@Value("${grant_permission_uri}")
	private String GRANT_PERMISSION_URI;
	
	@Value("${new_key_pair_uri}")
	private String NEW_KEYPAIR_URI;
	
	@Value("${issue_new_voucher_uri}")
	private String ISSUE_NEW_VOUCHER_URI;
	
	@Value("${issue_more_voucher_uri}")
	private String ISSUE_MORE_VOUCHER_URI;
	
	@Value("${get_incoming_transactions_uri}")
	private String GET_INCOMING_TRANSACTIONS_URI;
	
	@Value("${get_outgoing_transactions_uri}")
	private String GET_OUTGOING_TRANSACTIONS_URI;
	
	@Value("${is_tx_confirmed_uri}")
	private String IS_TX_CONFIRMED_URI;
	
	@Value("${get_voucher_balances_uri}")
	private String GET_VOUCHER_BALANCES_URI;
	
	@Value("${transfer_voucher_from_many_uri}")
	private String TRANSFER_VOUCHER_FROM_MANY_URI;

	@Value("${burn_vouchers_uri}")
	private String BURN_VOUCHERS_URI ;
	
	@Value("${broadcast_raw_tx_uri}")
	private String BROADCAST_RAW_TX_URI;
	
	@Value("${sign_raw_uri}")
	private String SIGN_RAW_URI;
	
	@Value("${blockchain_username}")
	private String BLOCKCHAIN_USERNAME;
	
	@Value("${blockchain_password}")
	private String BLOCKCHAIN_PASSWORD;
	
	@Value("${blockchain_read_timeout}")
	private int BLOCKCHAIN_READ_TIMEOUT;
	
	@Value("${blockchain_connection_timeout}")
	private int BLOCKCHAIN_CONNECTION_TIMEOUT;
	

	public ResponseNewKeyPairDTO invokeNewKeyPair() throws InvokeBlockchainException{
		ResponseNewKeyPairDTO responseDTO=null;
		try {
			Client client = configureClient();
	    	RequestNewKeyPairDTO request = new RequestNewKeyPairDTO();
	    	request.setSeed(null);
	    	logRequest(request,"invokeNewKeyPair");
	    	Response response  = client
	    		      .target(NEW_KEYPAIR_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseNewKeyPairDTO.class);
		}catch(Exception e) {
			log.error(new Date()+"Error Invoking Blockchain service NewKeyPair",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service NewKeyPair");
		}
		return responseDTO;
		
	}
	
	
	public ResponseGrantPermissionDTO invokeGrantPermission(String address,String entityType) throws InvokeBlockchainException{
		ResponseGrantPermissionDTO responseDTO=null;
		try {
			Client client = configureClient();
	    	RequestGrantPermissionsDTO request = new RequestGrantPermissionsDTO();
	    	request.setAddress(address);
	    	request.setRole(entityType);
	    	logRequest(request,"invokeGrantPermission");
	    	Response response  = client
	    		      .target(GRANT_PERMISSION_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseGrantPermissionDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service GrantPermission",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service GrantPermission");
		}
		return responseDTO;
		
	}
	
	
	private Client configureClient() {
		
		ClientConfig configuration = new ClientConfig();

		configuration = configuration.property(ClientProperties.CONNECT_TIMEOUT, BLOCKCHAIN_CONNECTION_TIMEOUT);
		log.debug("BLOCKCHAIN_CONNECTION_TIMEOUT: {}",BLOCKCHAIN_CONNECTION_TIMEOUT);
		configuration = configuration.property(ClientProperties.READ_TIMEOUT, BLOCKCHAIN_READ_TIMEOUT);
		log.debug("READ_TIMEOUT: {}",BLOCKCHAIN_READ_TIMEOUT);
		
		Client client = ClientBuilder.newClient(configuration);
    	HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(BLOCKCHAIN_USERNAME, encrptingUtils.decrypt(BLOCKCHAIN_PASSWORD.trim()));
	    	client.register(feature);
	    	return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;

	}


	public ResponseIssueNewVoucherDTO invokeIssueNewVoucher(RequestIssueNewVoucherDTO request) throws InvokeBlockchainException {
		ResponseIssueNewVoucherDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeIssueNewVoucher");
	    	Response response  = client
	    		      .target(ISSUE_NEW_VOUCHER_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseIssueNewVoucherDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeIssueNewVoucher",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeIssueNewVoucher");
		}
		return responseDTO;
	}


	public ResponseGetVoucherBalancesDTO invokeGetVoucherBalances(
			RequestGetVoucherBalancesDTO request) throws InvokeBlockchainException {
		ResponseGetVoucherBalancesDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeGetVoucherBalances");
	    	Response response  = client
	    		      .target(GET_VOUCHER_BALANCES_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseGetVoucherBalancesDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeGetVoucherBalances",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeGetVoucherBalances");
		}
		return responseDTO;
	}


	public ResponseTransferVoucherFromManyDTO invokeTransferVoucherFromMany(RequestTransferVoucherFromMany request) throws InvokeBlockchainException {
		ResponseTransferVoucherFromManyDTO responseDTO=null;
		try {

			Client client = configureClient();
			logRequest(request,"invokeTransferVoucherFromMany");
	    	Response response  = client
	    		      .target(TRANSFER_VOUCHER_FROM_MANY_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseTransferVoucherFromManyDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeTransferVoucherFromMany: ",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeTransferVoucherFromMany");
		}
		return responseDTO;
	}


	public ResponseBroadcastRawTxDTO invokeBroadcastRawTxDTO(RequestBroadCastRawTxDTO request) throws InvokeBlockchainException {
		ResponseBroadcastRawTxDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeBroadcastRawTxDTO");
	    	Response response  = client
	    		      .target(BROADCAST_RAW_TX_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseBroadcastRawTxDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeBroadcastRawTx",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeBroadcastRawTx");
		}
		return responseDTO;
	}


	public ResponseSignRawTxDTO invokeSignRaw(RequestSignRawTxDTO requestSignRawTx) throws InvokeBlockchainException {
		ResponseSignRawTxDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(requestSignRawTx,"invokeSignRaw");
			Response response  = client
	    		      .target(SIGN_RAW_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(requestSignRawTx, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseSignRawTxDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeSignRaw",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeSignRaw");
		}
		return responseDTO;
	}
	
	public ResponseTransferVoucherFromManyDTO invokeBurnVouchers(RequestBurnVouchersDTO request) throws InvokeBlockchainException {
		ResponseTransferVoucherFromManyDTO responseDTO=null;
		try {

			Client client = configureClient();
			logRequest(request,"invokeBurnVouchers");
	    	Response response  = client
	    		      .target(BURN_VOUCHERS_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseTransferVoucherFromManyDTO.class);
		}catch(Exception e) {
			
			log.error("Error Invoking Blockchain service invokeBurnVouchers ",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeBurnVouchers");
		}
		return responseDTO;
	}

	private void logRequest(Object request,String method) {
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonString=null;
		try {
			jsonString = mapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    log.debug("********************* Blockchain Metod {}Json string request is {}",method,jsonString);
	}


	public ResponseIssueMoreVoucher invokeIssueMoreVoucher(RequestIssueMoreVoucher request) throws InvokeBlockchainException {
		ResponseIssueMoreVoucher responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeIssueMoreVoucher");
	    	Response response  = client
	    		      .target(ISSUE_MORE_VOUCHER_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseIssueMoreVoucher.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeIssueMoreVoucher",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeIssueMoreVoucher");
		}
		return responseDTO;	
		}


	public GetIncomingTransactionsResponseDTO invokeGetIncomingTransactions(GetIncomingTransactionsRequestDTO request) throws InvokeBlockchainException {
		GetIncomingTransactionsResponseDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeGetIncomingTransactions");
	    	Response response  = client
	    		      .target(GET_INCOMING_TRANSACTIONS_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(GetIncomingTransactionsResponseDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeGetIncomingTransactions",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeGetIncomingTransactions");
		}
		return responseDTO;	
	}


	public GetIncomingTransactionsResponseDTO invokeGetOutgoingTransactions(GetIncomingTransactionsRequestDTO request) throws InvokeBlockchainException {
		GetIncomingTransactionsResponseDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeGetOutGoingTransactions");
	    	Response response  = client
	    		      .target(GET_OUTGOING_TRANSACTIONS_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(GetIncomingTransactionsResponseDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeGetOutgoingTransaction",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeGetOutgoingTransactions");
		}
		return responseDTO;	
	}


	public ResponseIsTxCofirmedDTO invokeIsTxConfirmed(RequestIsTXConfirmedDTO request) throws InvokeBlockchainException {
		ResponseIsTxCofirmedDTO responseDTO=null;
		try {
			Client client = configureClient();
			logRequest(request,"invokeIsTxConfirmed");
	    	Response response  = client
	    		      .target(IS_TX_CONFIRMED_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	responseDTO  =  response.readEntity(ResponseIsTxCofirmedDTO.class);
		}catch(Exception e) {
			log.error("Error Invoking Blockchain service invokeIsTxConfirmed",e);
			throw new InvokeBlockchainException("Error Invoking Blockchain service invokeIsTxConfirmed");
		}
		return responseDTO;	
	}



	
	
	
}
