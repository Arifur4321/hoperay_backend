package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.dao.CitiesDaoImpl;
import com.mediatica.vouchain.entities.Tbcomuni;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class CitiesDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(CitiesDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	CitiesDaoImpl citiesDaoImpl;	
	
	@Test
	public void selectAllTest() {		
		try {
			List<Tbcomuni> listaComuni = citiesDaoImpl.selectAll();		
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}		
	}
	
	@Test
	public void getCitiesByProvinceTest() {		
		try {
			List<Tbcomuni> listaComuni = citiesDaoImpl.getCitiesByProvince("RM");		
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}		
	}
	
	//try to get cities by not existing province
	@Test
	public void getCitiesByProvinceTest_notExistingProvince_KO() {		
		try {
			List<Tbcomuni> listaComuni = citiesDaoImpl.getCitiesByProvince("ZZ");		
			assertTrue(listaComuni!=null && listaComuni.size()>0);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}			
	}

}
