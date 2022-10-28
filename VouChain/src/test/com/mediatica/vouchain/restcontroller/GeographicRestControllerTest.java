package test.com.mediatica.vouchain.restcontroller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.util.test.TestFailedException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.hibernate.SessionFactory;
import org.junit.Test;
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
import com.mediatica.vouchain.dto.CityDTO;
import com.mediatica.vouchain.dto.ProvinceDTO;
import com.mediatica.vouchain.servicesInterface.GeographicServicesInterface;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class GeographicRestControllerTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(GeographicRestControllerTest.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	GeographicServicesInterface geographicServicesInterface;
	
	@Autowired
	private EncrptingUtils encrptingUtils;
	
	@Value("${company_auth_username}")
	private String COMPANY_USERNAME;
	
	@Value("${company_auth_password}")
	private String COMPANY_PASSWORD;
	
	private String GET_ALL_PROVINCES_URI="http://localhost:8080/VouChain/api/geographic/getAllProvinces";
	
	private String GET_ALL_CITIES_BY_PROVINCE_URI="http://localhost:8080/VouChain/api/geographic/getAllCitiesByProvince";

	private String GET_ALL_CITIES_URI="http://localhost:8080/VouChain/api/geographic/getAllCities";
	
	
	@Test
	public void listAllProvinces() {		
		
		try {
			//list all provinces
			List<ProvinceDTO> listaProvince = invokeGetAllProvinces();	
			assertTrue(listaProvince!=null && listaProvince.size()>0);
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}	
	}
	
	
	@Test
	public void getCitiesByProvinceId() {		
		
		try {
			//get provinces by sign
			List<CityDTO> listaComuni = invokeGetAllCitiesByProvince("PA");		
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}	
	}
	
	//TEST KO: try to get cities for a not existing province
	@Test
	public void getCitiesByProvinceId_notExistingProvincia_KO() {		
		
		try {
			//try to get cities for a not existing province
			List<CityDTO> listaComuni = invokeGetAllCitiesByProvince("ZZ");		
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}	
	}
	
	
	@Test
	public void listAllCities() {		
		
		try {
			//list all cities
			List<CityDTO> listaComuni = invokeGetAllCities();	
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}	
	}
	
	
	
	
	
	//-----------------INVOKE METHODS--------------------

	
	private List<ProvinceDTO> invokeGetAllProvinces() {
		List<ProvinceDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			Response response  = client
					.target(GET_ALL_PROVINCES_URI)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();			
			responseDTO  =  response.readEntity(new GenericType<List<ProvinceDTO>>() {});
			
		} catch (Exception e) {
			log.error("Error Invoking Geographic Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private List<CityDTO> invokeGetAllCitiesByProvince(String request) {
		List<CityDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeGetAllCitiesByProvince");		
			Response response  = client
					.target(GET_ALL_CITIES_BY_PROVINCE_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<List<CityDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Geographic Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private List<CityDTO> invokeGetAllCities() {
		List<CityDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			Response response  = client
					.target(GET_ALL_CITIES_URI)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();			
			responseDTO  =  response.readEntity(new GenericType<List<CityDTO>>() {});
			
		} catch (Exception e) {
			log.error("Error Invoking Geographic Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	private Client configureClient() {

		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
		HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(COMPANY_USERNAME, encrptingUtils.decrypt(COMPANY_PASSWORD.trim()));
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
		log.debug("********************* Geographic Rest Controller method {}Json string request is {}",method,jsonString);
	}





}
