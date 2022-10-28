package test.com.mediatica.vouchain.restcontroller;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.VoucherAllocationDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class VoucherRestControllerTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(VoucherRestControllerTest.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private EncrptingUtils encrptingUtils;
	
	@Value("${voucher_auth_username}")
	private String VOUCHER_USERNAME;
	
	@Value("${voucher_auth_password}")
	private String VOUCHER_PASSWORD;
	
	private String GET_PURCHASABLE_VOUCHER_LIST_URI="http://localhost:8080/VouChain/api/vouchers/getPurchasableVouchersList";
	
	private String GET_ACTIVE_VOUCHER_LIST_URI="http://localhost:8080/VouChain/api/vouchers/getActiveVoucherList";

	private String PURCHASE_VOUCHERS_URI="http://localhost:8080/VouChain/api/vouchers/purchaseVouchers";

	private String NEW_VOUCHER_TYPE_URI="http://localhost:8080/VouChain/api/vouchers/newVoucherType";

	private String VOUCHER_ALLOCATION_URI="http://localhost:8080/VouChain/api/vouchers/voucherAllocation";

	private String GET_EXPANDABLE_VOUCHER_LIST_URI="http://localhost:8080/VouChain/api/vouchers/getExpendableVouchersList";

	private String GET_EXPANDED_VOUCHER_LIST_URI="http://localhost:8080/VouChain/api/vouchers/getExpendedVoucherList";
	
	
	
	
	
	private DTOList<VoucherDTO> invokeGetPurchasableVouchersList() {
		DTOList<VoucherDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			Response response  = client
					.target(GET_PURCHASABLE_VOUCHER_LIST_URI)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<VoucherDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private DTOList<VoucherDTO> invokeGetActiveVoucherList(String request) {
		DTOList<VoucherDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeGetActiveVoucherList");		
			Response response  = client
					.target(GET_ACTIVE_VOUCHER_LIST_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<VoucherDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	
	private SimpleResponseDTO invokePurchaseVouchers(List<VoucherDTO> request, String usrId) {
		SimpleResponseDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokePurchaseVouchers");		
			Response response  = client
					.target(PURCHASE_VOUCHERS_URI)
					.path(usrId)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(SimpleResponseDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private VoucherDTO invokeNewVoucherType(VoucherDTO request) {
		VoucherDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeNewVoucherType");		
			Response response  = client
					.target(NEW_VOUCHER_TYPE_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(VoucherDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	private SimpleResponseDTO invokeVoucherAllocation(List<VoucherAllocationDTO> request) {
		SimpleResponseDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeVoucherAllocation");		
			Response response  = client
					.target(VOUCHER_ALLOCATION_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(SimpleResponseDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	
	private DTOList<VoucherDTO> invokeGetExpendableVouchersList(String request) {
		DTOList<VoucherDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeGetExpendableVouchersList");		
			Response response  = client
					.target(GET_EXPANDABLE_VOUCHER_LIST_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<VoucherDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	private DTOList<VoucherDTO> invokeGetExpendedVoucherList(String request) {
		DTOList<VoucherDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeGetExpendedVoucherList");		
			Response response  = client
					.target(GET_EXPANDED_VOUCHER_LIST_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<VoucherDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Voucher Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	
	
	
	
	
	
	private Client configureClient() {

		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
		HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(VOUCHER_USERNAME, encrptingUtils.decrypt(VOUCHER_PASSWORD.trim()));
			client.register(feature);
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
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
		log.debug("********************* Transaction method {}Json string request is {}",method,jsonString);
	}

}
