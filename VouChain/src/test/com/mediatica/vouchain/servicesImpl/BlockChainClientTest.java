package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.bouncycastle.util.test.TestFailedException;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.RequestIssueNewVoucherDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseIssueNewVoucherDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class BlockChainClientTest {

	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	BlockChainRestClient blockChainRestClient;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(BlockChainRestClient.class);

//------------INVOKE NEW KEY PAIR----------------------------------------

	@Test
	public void invokeNewKeyPairTest() {
		
		try {
			log.info("invokation new key pair...");
			ResponseNewKeyPairDTO response = blockChainRestClient.invokeNewKeyPair();
			log.info("key pair invoked");			
			
			assertTrue(response!=null && response.getError()==null);
			
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
	
//------------INVOKE GRANT PERMISSION----------------------------------------
	
	@Test
	public void invokeGrantPermissionTest() {
		
		try {
			ResponseNewKeyPairDTO keyPairResult = blockChainRestClient.invokeNewKeyPair();
			if(keyPairResult.getError()==null || keyPairResult.getError().isEmpty()) {
				
				log.info("invokation new key pair...");
				ResponseGrantPermissionDTO response = blockChainRestClient.invokeGrantPermission(keyPairResult.getResult().getAddress(), Constants.ENTITIES_TYPE.EMPLOYEE.toString());
				log.info("key pair invoked");
				
				assertTrue(response!=null && response.getError()==null);
			}
			
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
	
	//Test KO: try to invoke grant permission with a not existing entity type
	@Test
	public void invokeGrantPermissionTest_notExistingEntityType_KO() {
		
		try {
			ResponseNewKeyPairDTO keyPairResult = blockChainRestClient.invokeNewKeyPair();
			if(keyPairResult.getError()==null || keyPairResult.getError().isEmpty()) {
				
				log.info("invokation new key pair...");
				//set not existing entity type
				ResponseGrantPermissionDTO response = blockChainRestClient.invokeGrantPermission(keyPairResult.getResult().getAddress(), "NOT_EXISTING_ENTITY_TYPE");
				log.info("key pair invoked");
				
				assertTrue(response!=null && response.getError()==null);
			}
			
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
	
	
//------------INVOKE ISSUE NEW VOUCHER TEST----------------------------------------

	@Test
	public void invokeIssueNewVoucherTest() {
		
		try {
			VoucherDTO voucherDTO = fillVoucherDTO();
			
			RequestIssueNewVoucherDTO request = new RequestIssueNewVoucherDTO();
			request.setAddress_to(voucherDTO.getUsrBchAddress());
			request.setExpiration(voucherDTO.getVchEndDate());
			request.setName(voucherDTO.getVchName());
			request.setQuantity(1);
			double value = voucherDTO.getVchValue()!=null?Integer.parseInt(voucherDTO.getVchValue()):0;
			request.setValue(value);		
		
			log.info("invokation issue new voucher..."+request);
			ResponseIssueNewVoucherDTO response = blockChainRestClient.invokeIssueNewVoucher(request);
			log.info("issue new voucher invoked");
			
			assertTrue(response!=null && response.getError()==null);
			
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
	
	//Test ko: try to invoke issue new voucher with no address to
	@Test
	public void invokeIssueNewVoucherTest_nullAddressTo_KO() {
		
		try {
			VoucherDTO voucherDTO = fillVoucherDTO();
			
			RequestIssueNewVoucherDTO request = new RequestIssueNewVoucherDTO();
			
			//set null address to
			request.setAddress_to(null);
			request.setExpiration(voucherDTO.getVchEndDate());
			request.setName(voucherDTO.getVchName());
			request.setQuantity(0);
			int value = voucherDTO.getVchValue()!=null?Integer.parseInt(voucherDTO.getVchValue()):0;
			request.setValue(value);		
		
			log.info("invokation issue new voucher...");
			//try to invoke issue new voucher with no address to
			ResponseIssueNewVoucherDTO response = blockChainRestClient.invokeIssueNewVoucher(request);
			log.info("issue new voucher invoked");
			
			assertTrue(response!=null && response.getError()==null);
			
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
	
	
	
	
	
	

	
//------------UTILITIES----------------------------------------

	private VoucherDTO fillVoucherDTO() {
		VoucherDTO voucherDTO = new VoucherDTO();
		voucherDTO.setUsrBchAddress("1C1jRXwMcnaXXSeAYXnKo9pWF9htrwCrxQ");
		voucherDTO.setVchEndDate("2021-12-20");
		voucherDTO.setVchName("Test1");
		voucherDTO.setCompanyId("65");
		voucherDTO.setVchQuantity("1");
		voucherDTO.setVchValue("100");
		return voucherDTO;
	}
	


}
