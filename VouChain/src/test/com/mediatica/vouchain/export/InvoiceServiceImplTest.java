package test.com.mediatica.vouchain.export;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.export.InvoiceServiceImpl;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class InvoiceServiceImplTest {

	@Autowired
	InvoiceServiceImpl service;
	
	@Autowired 
	TransactionDaoImpl transDaoImpl;
	
	private final static Integer transactionId= 5;
	
	@Test
	public void generateInvoice() throws Exception {
		Transaction transaction = new Transaction(); 
		transaction = transDaoImpl.findByPrimaryKey(transaction, transactionId);
		File invoiceFileXML = service.generateInvoiceXML(transaction,new ArrayList<>(transaction.getTransactionDetailCollection()));
		assertTrue(invoiceFileXML!=null);
		File xslFile = new File(Constants.VOUCHAIN_HOME+Constants.XSL_TEMPLATE_SUBDIR_PATH+Constants.XSL_TEMPLATE);
		service.generatePdfFromXmlXslt(invoiceFileXML,xslFile,"test@xml.it");
		
		//service.generatePdfFromXmlXslt(new File("C:/vouchain/export/xml/1594822323109_azienda01.xml"),xslFile,"test@xml.it");
		//assertTrue(byteArr!=null);		
	
	}
	
	
	
}
