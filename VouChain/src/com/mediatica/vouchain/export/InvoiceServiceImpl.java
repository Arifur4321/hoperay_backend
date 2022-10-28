package com.mediatica.vouchain.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.hibernate.Session;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.Document;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.InvoiceDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.entities.Invoice;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.xml.FatturaElettronica;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiGenerali;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiPagamento;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale;
import com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente;

@Service
public class InvoiceServiceImpl implements InvoiceServiceInterface {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);
	
	@Autowired
	InvoiceProperties props;
	
	@Autowired
	TransactionDaoImpl trDaoImpl;
	
	@Autowired
	InvoiceDaoImpl invoiceDaoImpl;
	
	@Override
	public File generateInvoiceXML(Transaction transaction,List<TransactionDetail> detailList) {

		FatturaElettronica invoice = null;
		File invoiceFile = null;
		try {
			User user = transaction.getUsrIdA();
			String invoiceFilePath = Constants.VOUCHAIN_HOME+Constants.XML_EXPORT_SUBDIR_PATH+(new Date()).getTime()+"_"+user.getUsrEmail().substring(0,user.getUsrEmail().indexOf("@"))+Constants.XML_SUFFIX;
			invoiceFile = new File(invoiceFilePath);
			invoice=populateFatturaElettronica(transaction,detailList);
	        JAXBContext jaxbContext = JAXBContext.newInstance(FatturaElettronica.class);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(invoice,invoiceFile);
	        invoiceFile = changeHeader(invoiceFilePath,invoiceFile);
		}catch(Exception e) {
			log.error("Error creating invoice: {}",e.getMessage());
			e.printStackTrace();
		}
        return invoiceFile;
	}
	
	
	private File  changeHeader(String invoiceFilePath,File invoice) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(invoiceFilePath));
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[10];
		while (reader.read(buffer) != -1) {
			stringBuilder.append(new String(buffer));
			buffer = new char[10];
		}
		reader.close();

		String content = stringBuilder.toString();
		content=content.replace("<FatturaElettronica>", "<ns3:FatturaElettronica versione=\"FPR12\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns3=\"http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		content=content.replace("</FatturaElettronica>", "</ns3:FatturaElettronica>");
		int index = content.lastIndexOf("</ns3:FatturaElettronica>")+25;
		content = content.substring(0,index);

	//	System.out.println(content);
		invoice.delete();
		BufferedWriter writer = new BufferedWriter(new FileWriter(invoiceFilePath));
	    writer.write(content);
	    writer.close();
	    return new File(invoiceFilePath);
	}
	
	public byte[] generatePdfFromXmlXslt(File xmlfile, File xsltfile,String email) throws Exception {
	    TransformerFactory factory = TransformerFactory.newInstance();
		String htmlFilePath = Constants.VOUCHAIN_HOME+Constants.PDF_EXPORT_SUBDIR_PATH+(new Date()).getTime()+"__"+email.substring(0,email.indexOf("@"))+Constants.HTML_SUFFIX;
		String pdfFilePath  = Constants.VOUCHAIN_HOME+Constants.PDF_EXPORT_SUBDIR_PATH+(new Date()).getTime()+"_"+email.substring(0,email.indexOf("@"))+Constants.PDF_SUFFIX;
			
	    Source xslt = new StreamSource(xsltfile);
	    Transformer transformer = factory.newTransformer(xslt);
	
	    Source text = new StreamSource(xmlfile);
	    File htmlFile = new File(htmlFilePath);
	    transformer.transform(text, new StreamResult(htmlFile));
	    byte[] out=generatePDFFromHTML(htmlFilePath,pdfFilePath);
		
	    return out;
	}
	
	private byte[] generatePDFFromHTML(String htmlFilePath,String pdfFilePath) throws Exception, DocumentException {
	    com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
	    document.open();
	    XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlFilePath));
	    document.close();
	    byte[] out=Files.readAllBytes(new File(pdfFilePath).toPath());
	    return out;
	}

	private FatturaElettronica populateFatturaElettronica(Transaction transaction,List<TransactionDetail> detailList) {

		User user = transaction.getUsrIdA();
		FatturaElettronica fe = new FatturaElettronica();
		String strDouble = String.format(Locale.GERMAN,"%.2f", 2.00023); 
		String progressivo = getNumber();

		
		
		FatturaElettronicaHeader header=new FatturaElettronicaHeader();

			////// CEDENTE PRESTATORE /////////////
			Anagrafica anagraficaCP=new Anagrafica();
			anagraficaCP.setDenominazione(props.getRagioneSociale());
			IdFiscaleIVA idFiscaleIvaCP= new IdFiscaleIVA();
			idFiscaleIvaCP.setIdCodice(props.getPartitaIva());
			idFiscaleIvaCP.setIdPaese(Constants.ITA);
			
			DatiAnagrafici datiAnagraficiCP=new DatiAnagrafici();
			datiAnagraficiCP.setAnagrafica(anagraficaCP);
			datiAnagraficiCP.setCodiceFiscale(props.getCodiceFiscale());
			datiAnagraficiCP.setIdFiscaleIVA(idFiscaleIvaCP);
			datiAnagraficiCP.setRegimeFiscale(props.getRegimeFiscale());

			IscrizioneREA iscrizioneRea= new IscrizioneREA();
			iscrizioneRea.setCapitaleSociale(props.getCapitaleSociale());
			iscrizioneRea.setNumeroREA(props.getNumeroRea());
			iscrizioneRea.setSocioUnico(props.getSocioUnico());
			iscrizioneRea.setStatoLiquidazione(props.getStatoLiquidazione());
			iscrizioneRea.setUfficio(props.getUfficio());

			Sede sede= new Sede();
			sede.setCAP(props.getZip());
			sede.setComune(props.getCity());
			sede.setIndirizzo(props.getAddress());
			sede.setNazione(Constants.ITA);
			sede.setProvincia(props.getProv());

			CedentePrestatore cedentePrestatore=new CedentePrestatore();
			cedentePrestatore.setDatiAnagrafici(datiAnagraficiCP);
			cedentePrestatore.setIscrizioneREA(iscrizioneRea);
			cedentePrestatore.setSede(sede);

			if(props.getStabile_organizzazione().equalsIgnoreCase("SI")){
				StabileOrganizzazione stbOrg=new StabileOrganizzazione();
				stbOrg.setCAP(props.getStabileOrganizzazioneZip());
				stbOrg.setComune(props.getStabileOrganizzazioneCity());
				stbOrg.setIndirizzo(props.getStabileOrganizzazioneAddress());
				stbOrg.setNazione(Constants.ITA);
				stbOrg.setProvincia(props.getStabileOrganizzazione_prov());
				cedentePrestatore.setStabileOrganizzazione(stbOrg);
			}

			header.setCedentePrestatore(cedentePrestatore);
			////////////////////
			
		
			////// CESSIONARIO COMMITTENTE /////////////
		
			com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica anagraficaCC=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica();
			anagraficaCC.setDenominazione(user.getCompany().getCpyRagioneSociale());
			com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA idFiscaleIvaCC= new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA();
			idFiscaleIvaCC.setIdCodice(user.getCompany().getCpyPartitaIva());
			idFiscaleIvaCC.setIdPaese(Constants.ITA);
			
			com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici datiAnagraficiCC=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici();
			datiAnagraficiCC.setAnagrafica(anagraficaCC);
			datiAnagraficiCC.setCodiceFiscale(user.getCompany().getCpyCodiceFiscale());
			datiAnagraficiCC.setIdFiscaleIVA(idFiscaleIvaCC);
			
			
			com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede sedeCC= new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede();
			sedeCC.setCAP(user.getCompany().getCpyZip());
			sedeCC.setComune(user.getCompany().getCpyCity());
			sedeCC.setIndirizzo(user.getCompany().getCpyAddress());
			sedeCC.setNazione(Constants.ITA);
			sedeCC.setProvincia(user.getCompany().getCpyProv());
				
				CessionarioCommittente cessComm= new CessionarioCommittente();
				cessComm.setDatiAnagrafici(datiAnagraficiCC);
				cessComm.setSede(sedeCC);
				
				header.setCessionarioCommittente(cessComm);
			
			///////////////////////////////////////////////////////
			
			////// DATI TRASMISSIONE /////////////
			
			ContattiTrasmittente contattiTrasm= new ContattiTrasmittente();
			contattiTrasm.setEmail(user.getUsrEmail());
						
			IdTrasmittente idTrasmittente=new IdTrasmittente();
			idTrasmittente.setIdCodice(user.getCompany().getCpyCodiceFiscale());
			idTrasmittente.setIdPaese(Constants.ITA);
			
			DatiTrasmissione datiTrasmissione=new DatiTrasmissione();
			datiTrasmissione.setCodiceDestinatario(user.getCompany().getCpyCuu()!=null?user.getCompany().getCpyCuu():"0000000");
			datiTrasmissione.setContattiTrasmittente(contattiTrasm);
			datiTrasmissione.setFormatoTrasmissione(Constants.FPR12);
			datiTrasmissione.setIdTrasmittente(idTrasmittente);
			if(user.getCompany().getCpyCuu().equals("0000000")) {
				datiTrasmissione.setPECDestinatario(user.getCompany().getCpyPec());
			}
			datiTrasmissione.setProgressivoInvio(progressivo); 
			
				header.setDatiTrasmissione(datiTrasmissione);
		
			///////////////////////////////////////////////////////
			
			
			////// DATI TRASMISSIONE /////////////
			if(props.getRappresentanteFiscale().equalsIgnoreCase("SI")) {	
				com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica anagraficaRF=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica();
				anagraficaRF.setDenominazione(props.getRappresentanteFiscaleDenominazione());
				
				com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA idFiscaleIvaRF=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA();
				idFiscaleIvaRF.setIdCodice(props.getRappresentanteFiscaleCodice());
				idFiscaleIvaRF.setIdPaese(props.getRappresentanteFiscaleIdPaese());
				
				com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici datiAnaRF=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici();
				datiAnaRF.setAnagrafica(anagraficaRF);
				datiAnaRF.setIdFiscaleIVA(idFiscaleIvaRF);	
				
				RappresentanteFiscale rapprFisc= new RappresentanteFiscale();	
				rapprFisc.setDatiAnagrafici(datiAnaRF);	

			header.setRappresentanteFiscale(rapprFisc);
			}
			/////////////////////////////////////////////
			
			
			////// SOGGETTO EMITTENTE /////////////
			header.setSoggettoEmittente(props.getSoggettoEmittente());
			///////////////////////////////
			
			////// TERZO INTERMEDIARIO/////////////
			if(props.getTerzoIntermediario().equalsIgnoreCase("SI")) {		
					com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica anagraficaTISE=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica();
					anagraficaTISE.setDenominazione(props.getTerzoIntermediarioRagioneSociale());
					com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA idFiscaleTISE=new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA();
					idFiscaleTISE.setIdCodice(props.getTerzoIntermediarioPartitaIva());
					idFiscaleTISE.setIdPaese(props.getTerzoIntermediarioIdPaese());
					
					com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici datiAnagraficiTISE = new com.mediatica.vouchain.xml.FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici();
					datiAnagraficiTISE.setAnagrafica(anagraficaTISE);
					datiAnagraficiTISE.setIdFiscaleIVA(idFiscaleTISE);
					TerzoIntermediarioOSoggettoEmittente tise= new TerzoIntermediarioOSoggettoEmittente();
					tise.setDatiAnagrafici(datiAnagraficiTISE);
					
					header.setTerzoIntermediarioOSoggettoEmittente(tise);
			}
			/////////////////////////
		fe.setFatturaElettronicaHeader(header);
		
		FatturaElettronicaBody body= new FatturaElettronicaBody();
		////// DATI BENI E SERVIZI/////////////
		
		DatiRiepilogo datiRiepilogo=new DatiRiepilogo();
		datiRiepilogo.setAliquotaIVA(props.getAliquotaIva());
		datiRiepilogo.setEsigibilitaIVA(props.getEsigibilitaIva());
		double sum = calculateImporto(transaction,detailList);
		datiRiepilogo.setImponibileImporto(String.format(Locale.GERMAN,"%.2f",sum));
		double imposta = (sum*Double.parseDouble(props.getAliquotaIva()))/100;
		datiRiepilogo.setImposta(String.format(Locale.GERMAN,"%.2f",imposta));
		
		DatiBeniServizi datiBeniServizi=new DatiBeniServizi();
		datiBeniServizi.setDatiRiepilogo(datiRiepilogo);
		int nr=0;
		
		for (TransactionDetail td:detailList) {
			nr++;
			DettaglioLinee dettaglio=new DettaglioLinee();
			dettaglio.setAliquotaIVA(String.format(Locale.GERMAN,"%.2f",Double.parseDouble(props.getAliquotaIva())));
			dettaglio.setDescrizione(td.getVoucher().getVchName());
			dettaglio.setNumeroLinea(String.valueOf(nr));
			double totalPrice= td.getTrdQuantity().doubleValue()*td.getVoucher().getVchValue().doubleValue();
			dettaglio.setPrezzoTotale(String.format(Locale.GERMAN,"%.2f",totalPrice));
			dettaglio.setPrezzoUnitario(String.format(Locale.GERMAN,"%.2f", td.getVoucher().getVchValue())); 
			dettaglio.setQuantita(String.format(Locale.GERMAN,"%.2f",td.getTrdQuantity().doubleValue()));
			datiBeniServizi.getDettaglioLinee().add(dettaglio);
		}
		
		body.setDatiBeniServizi(datiBeniServizi);
		/////////////////////////
		
		////// DATI GENERALI/////////////
		DatiGeneraliDocumento datiGeneraliDoc=new DatiGeneraliDocumento();
		datiGeneraliDoc.setCausale(props.getCausale());
		String data = Constants.FORMATTER_YYYY_MM_DD.format(new Date());
		datiGeneraliDoc.setData(data);
		datiGeneraliDoc.setDivisa(Constants.EUR);
		double importoTotaleDocumento = (sum*(1+Double.parseDouble(props.getAliquotaIva())/100));
		datiGeneraliDoc.setImportoTotaleDocumento(String.format(Locale.GERMAN,"%.2f",importoTotaleDocumento));
		datiGeneraliDoc.setNumero(progressivo);  
		datiGeneraliDoc.setTipoDocumento(Constants.TD01);

		
		DatiGenerali datiGenerali=new DatiGenerali();
		datiGenerali.setDatiGeneraliDocumento(datiGeneraliDoc);
		
		body.setDatiGenerali(datiGenerali);
		///////////////////////////////////////////////
		
		////// DATI PAGAMENTO/////////////

		DettaglioPagamento dettaglioPagamento= new DettaglioPagamento();
		dettaglioPagamento.setGiorniTerminiPagamento(props.getGiorniTerminiPagamento());
		dettaglioPagamento.setImportoPagamento(String.format(Locale.GERMAN,"%.2f",importoTotaleDocumento));
		dettaglioPagamento.setModalitaPagamento(props.getModalitaPagamento());
		
		DatiPagamento datiPagamento=new DatiPagamento();
		datiPagamento.setCondizioniPagamento(Constants.TP02);

		datiPagamento.setDettaglioPagamento(dettaglioPagamento);
		body.setDatiPagamento(datiPagamento);
		
		
		
		fe.setFatturaElettronicaBody(body);
		return fe;
	}

	private double calculateImporto(Transaction transaction,List<TransactionDetail> detailList) {
	    double sum = 0.0;
		for (TransactionDetail td:detailList) {
	    	double qnt = td.getTrdQuantity();
	    	BigDecimal value = td.getVoucher().getVchValue();
	    	sum += qnt*value.doubleValue();
	    }
	    
		return sum;
	}
	
	private String getNumber() {
		Session session = invoiceDaoImpl.openSession();
		org.hibernate.Transaction tr = session.getTransaction();
		Invoice inv = new Invoice();
		invoiceDaoImpl.insert(inv);
		int nr=inv.getIdinvoice();
		tr.rollback();
		session.close();
		String number = String.valueOf(nr);
		number =  String.format("%5s", number).replace(' ', '0'); 
		return number;
	}

}
