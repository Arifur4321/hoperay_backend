package test.com.mediatica.vouchain.otherTest;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Test;

import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.RequestGrantPermissionsDTO;
import com.mediatica.vouchain.dto.RequestNewKeyPairDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDetailDTO;

public class BlockChainTest {



    private Client client = ClientBuilder.newClient();
	
//    @Test
//    public ResponseNewKeyPairDTO getJsonEmployeeGET(int id) {
//    	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("wrapper", "password");
//    	client.register(feature);
//    	ResponseNewKeyPairDTO response = client
//							        .target(REST_URI)
//							        .path(String.valueOf(id))
//							        .request(MediaType.APPLICATION_JSON)
//							        .get(ResponseNewKeyPairDTO.class);
//    	
//    	
//    	return response;
//    }
    
    
    
    @Test
    public void postNewKeyPair() {
    	Response response  = null;
    	String REST_URI  = "http://192.168.99.5:8123/account/new_keypair";
    	try {
	    	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("wrapper", "password");
	    	client.register(feature);
	    	RequestNewKeyPairDTO request = new RequestNewKeyPairDTO();
	    	request.setSeed(null);
	    	response  = client
	    		      .target(REST_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	ResponseNewKeyPairDTO block  =  response.readEntity(ResponseNewKeyPairDTO.class);

	    	System.out.println(response);
	    	System.out.println(response.getStatus());
	    	System.out.println(response.readEntity(String.class));
	    	System.out.println(block);
	    	
	    	String address = block.getResult().getAddress();
	    	
	    	System.out.println("address: "+address);
	    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	assertTrue(response!=null);
    }
    
    @Test
    public void postGrantAccountPermissions() {
    	String address ="13pZP2ip1NkPowkRPsyWUyZrbEdTEv7QqA";
    	String REST_URI  = "http://192.168.99.5:8123/permissions/grant_account_permissions";
    	Response response  = null;
    	try {
	    	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("wrapper", "password");
	    	client.register(feature);
	    	
	    	RequestGrantPermissionsDTO request = new RequestGrantPermissionsDTO();
	    	request.setAddress(address);
	    	request.setRole(Constants.ENTITIES_TYPE.COMPANY.toString());
	    	response  = client
	    		      .target(REST_URI)
	    		      .request(MediaType.APPLICATION_JSON)
	    		      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
	    	response.bufferEntity();
	    	ResponseGrantPermissionDTO block  =  response.readEntity(ResponseGrantPermissionDTO.class);
	    	
	    	System.out.println(response);
	    	System.out.println(response.getStatus());
	    	System.out.println(response.readEntity(String.class));
	    	System.out.println(block);
		}catch(Exception e) {
			e.printStackTrace();
		}
    	assertTrue(response!=null);
    	
    }
    

}
