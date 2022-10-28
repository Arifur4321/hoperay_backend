package test.com.mediatica.vouchain.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.export.ExcelServiceImpl;
import com.mediatica.vouchain.servicesImpl.TransactionServiceImpl;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class ExcelServiceImplTest {

	@Autowired
	ExcelServiceImpl service;
	
	@Autowired
	TransactionServiceImpl transactionService;
	
	@Test
	public void exportExcel() {
		try {
			List<TransactionDTO> list = transactionService.transactionsList("2", "company", null, null, true);
			byte[] inByte = service.exportTransaction(list, "2", null, null);
            OutputStream os  = new FileOutputStream(new File("C:/vouchain/"+Math.random()+".xlsx")); 
	
	        // Starts writing the bytes in it 
	        os.write(inByte); 
	        System.out.println("Successfully"
	                           + " byte inserted"); 
	
	        // Close the file 
	        os.close(); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
