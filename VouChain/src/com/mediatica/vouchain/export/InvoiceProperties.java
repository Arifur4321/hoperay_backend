package com.mediatica.vouchain.export;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("file:${vouchain_home}/configurations/invoice.properties")
public class InvoiceProperties {

	@Value("${codice_fiscale}")
	private String codiceFiscale;
	@Value("${partita_iva}")
	private String partitaIva;
	@Value("${ragione_sociale}")
	private String ragioneSociale;
	@Value("${regime_fiscale}")
	private String regimeFiscale;
	@Value("${address}")
	private String address;
	@Value("${zip}")
	private String zip;
	@Value("${city}")
	private String city;
	@Value("${prov}")
	private String prov;
	@Value("${stabile_organizzazione}")
	private String stabile_organizzazione;
	@Value("${stabileOrganizzazione_address}")
	private String stabileOrganizzazioneAddress;
	@Value("${stabileOrganizzazione_zip}")
	private String stabileOrganizzazioneZip;
	@Value("${stabileOrganizzazione_city}")
	private String stabileOrganizzazioneCity;
	@Value("${stabileOrganizzazione_prov}")
	private String stabileOrganizzazione_prov;
	@Value("${ufficio}")
	private String ufficio;
	@Value("${numero_rea}")
	private String numeroRea;
	@Value("${capitale_sociale}")
	private String capitaleSociale;
	@Value("${socio_unico}")
	private String socioUnico;
	@Value("${stato_liquidazione}")
	private String statoLiquidazione;
	@Value("${RappresentanteFiscale}")
	private String rappresentanteFiscale;
	@Value("${RappresentanteFiscale_IdPaese}")
	private String rappresentanteFiscaleIdPaese;
	@Value("${RappresentanteFiscale_partita_iva}")
	private String rappresentanteFiscalePartitaIva;
	@Value("${RappresentanteFiscale_ragione_sociale}")
	private String rappresentanteFiscaleRagioneSociale;
	@Value("${RappresentanteFiscale_codice}")
	private String rappresentanteFiscaleCodice;		
	
	@Value("${RappresentanteFiscale_denominazione}")
	private String rappresentanteFiscaleDenominazione;


	@Value("${causale}")
	private String causale;
	@Value("${SoggettoEmittente}")
	private String soggettoEmittente;
	@Value("${aliquota_iva}")
	private String aliquotaIva;
	@Value("${ModalitaPagamento}")
	private String modalitaPagamento;
	@Value("${GiorniTerminiPagamento}")
	private String giorniTerminiPagamento;
	@Value("${esigibilita_iva}")
	private String esigibilitaIva;
	
	
	@Value("${TerzoIntermediarioOSoggettoEmittente}")
	private String terzoIntermediario;
	@Value("${TerzoIntermediarioOSoggettoEmittente_IdPaese}")
	private String terzoIntermediarioIdPaese;
	@Value("${TerzoIntermediarioOSoggettoEmittente_partita_iva}")
	private String terzoIntermediarioPartitaIva;
	@Value("${TerzoIntermediarioOSoggettoEmittente_ragione_sociale}")
	private String terzoIntermediarioRagioneSociale;



	public String getCodiceFiscale() {
		return codiceFiscale;
	}


	public String getPartitaIva() {
		return partitaIva;
	}


	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public String getRappresentanteFiscaleCodice() {
		return rappresentanteFiscaleCodice;
	}

	
	public String getRegimeFiscale() {
		return regimeFiscale;
	}


	public String getAddress() {
		return address;
	}


	public String getZip() {
		return zip;
	}


	public String getCity() {
		return city;
	}


	public String getProv() {
		return prov;
	}


	public String getStabile_organizzazione() {
		return stabile_organizzazione;
	}


	public String getStabileOrganizzazioneAddress() {
		return stabileOrganizzazioneAddress;
	}


	public String getStabileOrganizzazioneZip() {
		return stabileOrganizzazioneZip;
	}


	public String getStabileOrganizzazioneCity() {
		return stabileOrganizzazioneCity;
	}


	public String getStabileOrganizzazione_prov() {
		return stabileOrganizzazione_prov;
	}


	public String getUfficio() {
		return ufficio;
	}


	public String getNumeroRea() {
		return numeroRea;
	}


	public String getCapitaleSociale() {
		return capitaleSociale;
	}


	public String getSocioUnico() {
		return socioUnico;
	}


	public String getStatoLiquidazione() {
		return statoLiquidazione;
	}


	public String getRappresentanteFiscale() {
		return rappresentanteFiscale;
	}


	public String getRappresentanteFiscaleIdPaese() {
		return rappresentanteFiscaleIdPaese;
	}


	public String getRappresentanteFiscalePartitaIva() {
		return rappresentanteFiscalePartitaIva;
	}


	public String getRappresentanteFiscaleRagioneSociale() {
		return rappresentanteFiscaleRagioneSociale;
	}


	public String getSoggettoEmittente() {
		return soggettoEmittente;
	}


	public String getAliquotaIva() {
		return aliquotaIva;
	}


	public String getModalitaPagamento() {
		return modalitaPagamento;
	}


	public String getGiorniTerminiPagamento() {
		return giorniTerminiPagamento;
	}

	public String getTerzoIntermediario() {
		return terzoIntermediario;
	}


	public String getTerzoIntermediarioIdPaese() {
		return terzoIntermediarioIdPaese;
	}


	public String getTerzoIntermediarioPartitaIva() {
		return terzoIntermediarioPartitaIva;
	}


	public String getTerzoIntermediarioRagioneSociale() {
		return terzoIntermediarioRagioneSociale;
	}

	public String getEsigibilitaIva() {
		return esigibilitaIva;
	}

	public String getCausale() {

		return causale;
	}

	public String getRappresentanteFiscaleDenominazione() {
		return rappresentanteFiscaleDenominazione;
	}

	
	
	
	
}
