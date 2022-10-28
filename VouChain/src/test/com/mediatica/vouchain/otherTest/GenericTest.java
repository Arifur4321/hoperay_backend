package test.com.mediatica.vouchain.otherTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.validation.constraints.AssertTrue;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.util.Store;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.AcroFields;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
@ComponentScan("com.mediatica.vouchain")
public class GenericTest {


//		@Test
//		public void createPDFItext() throws IOException, DocumentException {
//			PdfReader reader = new PdfReader("c:/test/contratto1.pdf");
//			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("c:/test/contrattoRiempitoITEXT.pdf"));
//			AcroFields form = stamper.getAcroFields();
//			form.setField("name", "pietro");
//			form.setField("pec", "ciao@gmail.com");
//			form.setField("iva", "12354484");
//			form.setField("zipCode", "HJLYTR56G78K");
//			stamper.setFormFlattening(true);
//			stamper.close();
//			reader.close();
//		}

	
	@Test
	public void createPDF() throws IOException {
//	
    PDDocument pDDocument = PDDocument.load(new File("C:\\vouchain\\templates\\pdf\\ContractTemplate.pdf"));
  //  PDDocument pDDocument = PDDocument.load(new File("C:\\vouchain\\templates\\pdf\\ContractTemplate_old.pdf"));
    
    PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();

    PDField field = pDAcroForm.getField("clientCode");
    field.setValue("clientCode");
    
    field = pDAcroForm.getField("companyName");
    field.setValue("companyName");
    
    field = pDAcroForm.getField("VAT");
    field.setValue("VAT");
    
    field = pDAcroForm.getField("address");
    field.setValue("address");
    
    field = pDAcroForm.getField("representative");
    field.setValue("representative");
    
    field = pDAcroForm.getField("phoneNumber");
    field.setValue("phoneNumber");
    
    field = pDAcroForm.getField("phoneNumber");
    field.setValue("phoneNumber");
    
    field = pDAcroForm.getField("email");
    field.setValue("email");
    
    field = pDAcroForm.getField("PEC");
    field.setValue("PEC");
   
    field = pDAcroForm.getField("invoiceCode");
    field.setValue("invoiceCode");
    
    field = pDAcroForm.getField("date");
    field.setValue("date");
    
    
    
    
    
//    PDField field = pDAcroForm.getField("name");
//    field.setValue("Vouchain System SPA");
//    field.setReadOnly(true);
//    field = pDAcroForm.getField("iva");
//    field.setValue("635820201");
//    field.setReadOnly(true);
//    
//    field = pDAcroForm.getField("fiscalCode");
//    field.setValue("NPLPTR637JGIUG");
//    field.setReadOnly(true);
//    
//    field = pDAcroForm.getField("pec");
//    field.setValue("hhhh@hhhh.it");
//    field.setReadOnly(true);
    
    pDDocument.save("c:/test/contrattoRiempito1.pdf");
    pDDocument.close();
    System.out.println("ALL Ok");
    assertTrue(true);
	}	
	
	
	
	@Test
	public void decrpta() throws Exception {
	    File f = new File("c:/test/test.pdf.p7m");

	    System.out.print(f.getAbsolutePath());
	    byte[] content = org.apache.commons.io.FileUtils.readFileToByteArray(f);

	    byte[] contentUnsigned = removeP7MCodes(f.getName(), content);
	    if (contentUnsigned != null) {
	         
	        File file = new File("c:/test/outputfile.pdf");
	         
	        FileOutputStream fos = null;
	 
	        try {
	             
	            fos = new FileOutputStream(file);
	             
	            // Writes bytes from the specified byte array to this file output stream 
	            fos.write(contentUnsigned);
	 
	        }catch(Exception e){
	        	System.out.println(e.getStackTrace());
	        }
	    }
	    	
	}

	public static byte[] removeP7MCodes(final String fileName, final byte[] p7bytes) {
	    try {

	        if (p7bytes == null)
	            return p7bytes;

	        if (!fileName.toUpperCase().endsWith(".P7M")) {
	            return p7bytes;
	        }


	        CMSSignedData cms = new CMSSignedData(p7bytes); 

	        if (cms.getSignedContent() == null) {
	            System.out.print("Unable to find signed Content during decoding from P7M for file: " + fileName);               
	            return null;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        cms.getSignedContent().write(out);

	        return out.toByteArray();
	    } catch (CMSException e) {
	    	 System.out.println("CMSException from P7M for file: " + fileName);
	    } catch (IOException e) {
	    	 System.out.print("IOException from P7M for file: " + fileName);
	    }
	    return null;
	}
	
	
	
}
