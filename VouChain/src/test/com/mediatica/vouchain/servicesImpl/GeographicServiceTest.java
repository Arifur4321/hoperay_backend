package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

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

import com.mediatica.vouchain.dto.CityDTO;
import com.mediatica.vouchain.dto.ProvinceDTO;
import com.mediatica.vouchain.servicesImpl.GeographicServiceImpl;
import com.mediatica.vouchain.servicesInterface.GeographicServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class GeographicServiceTest {

	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	GeographicServicesInterface geographicServicesInterface;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(GeographicServiceImpl.class);

	
	@Test
	public void getCitiesByProvinceId() {		
		
		try {
			//get provinces by sign
			List<CityDTO> listaComuni = geographicServicesInterface.getCitiesByProvinceId("PA");		
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
			List<CityDTO> listaComuni = geographicServicesInterface.getCitiesByProvinceId("ZZ");		
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
	public void listAllProvinces() {		
		
		try {
			//list all provinces
			List<ProvinceDTO> listaProvince = geographicServicesInterface.listAllProvinces();	
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
	public void listAllCities() {		
		
		try {
			//list all cities
			List<CityDTO> listaComuni = geographicServicesInterface.listAllCities();	
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
}
