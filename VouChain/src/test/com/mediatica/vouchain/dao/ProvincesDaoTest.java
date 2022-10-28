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

import com.mediatica.vouchain.dao.ProvincesDaoImpl;
import com.mediatica.vouchain.entities.Tbprovince;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class ProvincesDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ProvincesDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	ProvincesDaoImpl provincesDaoImpl;

	
	@Test
	public void selectAllTest() {		
		try {
			List<Tbprovince> listaProvince = provincesDaoImpl.selectAll();		
			assertTrue(listaProvince!=null && listaProvince.size()>0);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}		
	}
}
