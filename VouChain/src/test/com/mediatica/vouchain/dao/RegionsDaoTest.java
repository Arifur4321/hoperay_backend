package test.com.mediatica.vouchain.dao;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class RegionsDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(RegionsDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	

}
