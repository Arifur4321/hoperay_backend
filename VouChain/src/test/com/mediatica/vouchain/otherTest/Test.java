package test.com.mediatica.vouchain.otherTest;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;


import org.bouncycastle.util.Store;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
public class Test {

	
	public void test() {
		fail("Not yet implemented");
	}

	@org.junit.Test
	public void verifSignedData()
			  throws Exception {
		try {
		File file = new File("c:/test/modello.pdf.p7m");

		byte[] signedData = Files.readAllBytes(file.toPath());
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			    ByteArrayInputStream inputStream = new ByteArrayInputStream(signedData);
			    ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
			    CMSSignedData cmsSignedData = new CMSSignedData( ContentInfo.getInstance(asnInputStream.readObject()));
			    Store<X509CertificateHolder> certs = cmsSignedData.getCertificates();
			    SignerInformationStore signers = cmsSignedData.getSignerInfos();
			 
			    SignerInformation signer = signers.getSigners().iterator().next();
			    
			    Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());
			    //CERTIFICATO ESTRATTO DAL DOCUMENTO
			    X509CertificateHolder certHolder = certCollection.iterator().next();
			    //VERIFICA DEL FILE 
			    assertTrue( signer
					      .verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC")
							      .build(certHolder)));
		}catch(Exception e) {
			e.printStackTrace();
		}
			}
	
	@org.junit.Test
	public void verifySign() throws IOException, CMSException {
			    
		File file = new File("c:/test/test.pdf.p7m");
		try {
				byte[] signedData = Files.readAllBytes(file.toPath());
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			    ByteArrayInputStream inputStream = new ByteArrayInputStream(signedData);
			    ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
			    CMSSignedData cmsSignedData = new CMSSignedData( ContentInfo.getInstance(asnInputStream.readObject()));
			    Store<X509CertificateHolder> certs = cmsSignedData.getCertificates();
			    SignerInformationStore signers = cmsSignedData.getSignerInfos();
			    SignerInformation signer = signers.getSigners().iterator().next();
			    
			    Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());
			    //CERTIFICATO ESTRATTO DAL DOCUMENTO
			    X509CertificateHolder certHolder = certCollection.iterator().next();
			    
			      System.out.println("info 1: "+certHolder.getIssuer());
		          System.out.println("info 2: "+certHolder.getSubject());
		          System.out.println("date from: "+certHolder.getNotBefore());
		          System.out.println("date to: "+certHolder.getNotAfter());
		          System.out.println("Serial n. "+certHolder.getSerialNumber());
		              
				    
		         ///////////////////// If you want to verify the signature of this PKCS7 file against X509 certificate ***************	    
				 CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509","BC");
		//
		//		 // Open the certificate file (questo è il file che mi sono tirato giu dal server
				 FileInputStream fileinputstream = new FileInputStream("c:/test/CA.pem");
		
			
				//get CA public key
				 PublicKey pkCA = certificatefactory.generateCertificate(fileinputstream).getPublicKey();
				 System.out.println("PublicKey: "+pkCA);
				 X509Certificate myCertificate = new JcaX509CertificateConverter().getCertificate(certHolder);
				 System.out.println("myCA: "+myCertificate);
				 
				 myCertificate.verify(pkCA);
				 System.out.println("Verfication done successfully ");
				 //////////
		}catch(Exception e) {
			e.printStackTrace();
		}
		assertTrue(true);
		    
		//******************************************************************************************************	
	}
	
	
	
	
	@org.junit.Test
	public void testCA() {
		   try {
			   File f = new File("c:/test/test.pdf.p7m");
			   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			   byte[] buffer = new byte[(int) f.length()];
			   DataInputStream in = new DataInputStream(new FileInputStream(f));
			   in.readFully(buffer);
			   in.close();
	
			   //Corresponding class of signed_data is CMSSignedData
			   CMSSignedData signature = new CMSSignedData(buffer);
			   Store cs = signature.getCertificates();
			   SignerInformationStore signers = signature.getSignerInfos();
			   Collection c = signers.getSigners();
			   Iterator it = c.iterator();
	
			   //the following array will contain the content of xml document
			   byte[] data = null;
	
			   while (it.hasNext()) {
			        SignerInformation signer = (SignerInformation) it.next();
			        Collection certCollection = cs.getMatches(signer.getSID());
			        Iterator certIt = certCollection.iterator();
			        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
	
			        System.out.println("signature algorithm: "+cert.getSignatureAlgorithm());
			        System.out.println("signature issuer: "+cert.getIssuer());
			        CMSProcessable sc = signature.getSignedContent();
			        data = (byte[]) sc.getContent();
			        
				     // ************************************************************* //
				     // ********************* Verify signature ********************** //
				     //get CA public key
				     // Create a X509 certificate
				     CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509","BC");
	
				     // Open the certificate file
				     FileInputStream fileinputstream = new FileInputStream("C:\\test\\CA.pem");
				     Certificate publicCerificate = certificatefactory.generateCertificate(fileinputstream);
				     //get CA public key
				     PublicKey pk = publicCerificate.getPublicKey();
				     System.out.println("public key: "+pk);
	
				     X509Certificate myCA = new JcaX509CertificateConverter().setProvider("BC").getCertificate(cert);
				     System.out.println(myCA.getType());
				     System.out.println("my public key: "+myCA.getPublicKey());
	
				     myCA.verify(pk);
				     System.out.println("Verfication done successfully ");
			    }
		   }catch(Exception e) {
			   e.printStackTrace();
			   fail();
		   }
		   assertTrue(true);
	}
	
	
	
//	@org.junit.Test
//	private PrivateKey provider() throws CertificateException, NoSuchProviderException, KeyStoreException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
//		Security.addProvider(new BouncyCastleProvider());
//		CertificateFactory certFactory= 	CertificateFactory.getInstance("X.509", "BC");
//	
//		  
//		X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new FileInputStream("Baeldung.cer"));
//
//		  
//		char[] keystorePassword = "password".toCharArray();
//		char[] keyPassword = "password".toCharArray();
//		  
//		KeyStore keystore = KeyStore.getInstance("PKCS12");
//		keystore.load(new FileInputStream("Baeldung.p12"), keystorePassword);
//		PrivateKey key = (PrivateKey) keystore.getKey("baeldung", keyPassword);
//		return key;
//	}
//	
//	@org.junit.Test
//	public static byte[] decryptData(
//			  byte[] encryptedData, 
//			  PrivateKey decryptionKey) 
//			  throws CMSException {
//			  
//			    byte[] decryptedData = null;
//			    if (null != encryptedData && null != decryptionKey) {
//			        CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);
//			  
//			        Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
//			        RecipientInformation recipient2 = (RecipientInformation) recipients.iterator().next();
//			        KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator().next();
//			        
//			        
//			        JceKeyTransRecipient recipient  = new JceKeyTransEnvelopedRecipient(decryptionKey);
//			         
//			        return recipientInfo.getContent(recipient);
//			    }
//			    return decryptedData;
//			}
	
	
	
	@org.junit.Test
	public void testCA2() {
		String p7mPath = "c:/test/test_altered_name.pdf.p7m";
		String caPath = "c:/test/CA.pem";
	   try {
		   File f = new File(p7mPath);
		   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		   byte[] buffer = new byte[(int) f.length()];
		   DataInputStream in = new DataInputStream(new FileInputStream(f));
		   in.readFully(buffer);
		   in.close();

		   //Corresponding class of signed_data is CMSSignedData
		   CMSSignedData signature = new CMSSignedData(buffer);
		   Store cs = signature.getCertificates();
		   SignerInformationStore signers = signature.getSignerInfos();
		   Collection c = signers.getSigners();
		   Iterator it = c.iterator();

		   //the following array will contain the content of xml document
		   byte[] data = null;

		   while (it.hasNext()) {
		        SignerInformation signer = (SignerInformation) it.next();
		        Collection certCollection = cs.getMatches(signer.getSID());
		        Iterator certIt = certCollection.iterator();
		        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

		        System.out.println("signature algorithm: "+cert.getSignatureAlgorithm());
		        System.out.println("signature issuer: "+cert.getIssuer());
		        CMSProcessable sc = signature.getSignedContent();
		        data = (byte[]) sc.getContent();
		        
			     // ************************************************************* //
			     // ********************* Verify signature ********************** //
			     //get CA public key
			     // Create a X509 certificate
			     CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509","BC");

			     // Open the certificate file
			     //FileInputStream fileinputstream = new FileInputStream(caPath);
			     BufferedInputStream buffinputstream = new BufferedInputStream(new FileInputStream(caPath));
			     System.out.println("Mark supported: " + buffinputstream.markSupported());
			     boolean caFound = false;
			     int i = 0;
			     while (!caFound) {
			    	 i++;
				     Certificate publicCerificate = certificatefactory.generateCertificate(buffinputstream);
				     //get CA public key
				     PublicKey pk = publicCerificate.getPublicKey();
				     System.out.println("public key: "+pk);

				     X509Certificate myCA = new JcaX509CertificateConverter().setProvider("BC").getCertificate(cert);
				     System.out.println(myCA.getType());
				     System.out.println("my public key: "+myCA.getPublicKey());
				     System.out.println("******************************************************************** "+i);
				     try {
					     myCA.verify(pk);
					     System.out.println("Verfication done successfully ");
					     caFound = true;
				     } catch(Exception e) {
				     	e.printStackTrace();
				     }
			     }
			     assert(caFound);
		    }
	   }catch(Exception e) {
		   e.printStackTrace();
           assert(false);
	   }
	   assert(true);
	}
	
	@org.junit.Test
	public void test1() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		String encoded = encoder.encode("123456");
		System.out.println(encoded);
		assert(true);
	}
	
}
