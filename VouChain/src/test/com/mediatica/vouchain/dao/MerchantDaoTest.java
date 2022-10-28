package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.dao.MerchantDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class MerchantDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MerchantDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	MerchantDaoImpl merchantDaoImpl;
	
	@Autowired
	MerchantServiceImpl merchantServiceImpl;
	
	@Test
	public void listAllTest() {	
		
		MerchantDTO merchantDTO = fillMerchantDTO();	
		Integer idUser = null;
		MerchantDTO dtoResult = null;
		
		try {		

			log.info("Starting signUp...");
			Session session = sessionFactory.getCurrentSession();
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUser=Integer.valueOf(dtoResult.getUsrId());
			}
			
			List<Merchant> merchantList = merchantDaoImpl.listAll();	
			
			//delete data just inserted in DB	
			log.info("executing rollback...");
			rollBack(idUser, session);
			log.info("rollback OK");
			
			assertTrue(merchantList!=null && merchantList.size()>0);

		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}		
	}
	
	
	
	//--------------UTILITIES------------------------------------
	
	private MerchantDTO fillMerchantDTO() {		
		
		MerchantDTO dto = new MerchantDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Merchant.654");
		dto.setMrcAddress("via del corso 12");
		dto.setMrcBank("ING direct");
		dto.setMrcCity("Roma");
		dto.setMrcCodiceFiscale("123CodFiscale123");
		dto.setMrcFirstNameRef("Ciro");
		dto.setMrcLastNameRef("Rossi");

		
		return dto;
	}
	
	private void rollBack(Integer idUserInserted, Session session) {
		//delete the Transaction
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Transaction where usr_id_da = " + idUserInserted + " or usr_id_a = " + idUserInserted);
		query2.executeUpdate();				
		txn2.commit();
		
		//delete the Merchant
		org.hibernate.Transaction txn1 = session.beginTransaction();
		Query query1 = session.createQuery("delete from Merchant where usr_id = " + idUserInserted);
		query1.executeUpdate();				
		txn1.commit();


		//delete the User
		org.hibernate.Transaction txn3 = session.beginTransaction();
		Query query3 = session.createQuery("delete from User where usr_id = " + idUserInserted);
		query3.executeUpdate();				
		txn3.commit();
	}
	

}
