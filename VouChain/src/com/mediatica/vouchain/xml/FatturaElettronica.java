//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.07.10 alle 10:24:43 AM CEST 
//


package com.mediatica.vouchain.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FatturaElettronicaHeader">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DatiTrasmissione">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="IdTrasmittente">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="ProgressivoInvio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="FormatoTrasmissione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="CodiceDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             
 *                             &lt;element name="ContattiTrasmittente">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="PECDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="CedentePrestatore">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DatiAnagrafici">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IdFiscaleIVA">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Anagrafica">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="RegimeFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Sede">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="StabileOrganizzazione">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="IscrizioneREA">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Ufficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="NumeroREA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="CapitaleSociale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="SocioUnico" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="StatoLiquidazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="RappresentanteFiscale">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DatiAnagrafici">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IdFiscaleIVA">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="Anagrafica">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="CessionarioCommittente">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DatiAnagrafici">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IdFiscaleIVA">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Anagrafica">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Sede">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TerzoIntermediarioOSoggettoEmittente">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DatiAnagrafici">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IdFiscaleIVA">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="Anagrafica">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SoggettoEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="FatturaElettronicaBody">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DatiGenerali">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DatiGeneraliDocumento">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="TipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Divisa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="ImportoTotaleDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Causale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DatiBeniServizi">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="DettaglioLinee" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="NumeroLinea" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Descrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="PrezzoUnitario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="PrezzoTotale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="DatiRiepilogo">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="ImponibileImporto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Imposta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="EsigibilitaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DatiPagamento">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="CondizioniPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="DettaglioPagamento">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="ModalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="GiorniTerminiPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="ImportoPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="versione" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fatturaElettronicaHeader",
    "fatturaElettronicaBody"
})
@XmlRootElement(name = "FatturaElettronica")
public class FatturaElettronica {

    @XmlElement(name = "FatturaElettronicaHeader", required = true)
    protected FatturaElettronica.FatturaElettronicaHeader fatturaElettronicaHeader;
    @XmlElement(name = "FatturaElettronicaBody", required = true)
    protected FatturaElettronica.FatturaElettronicaBody fatturaElettronicaBody;
    @XmlAttribute(name = "versione")
    protected String versione;

    /**
     * Recupera il valore della proprietà fatturaElettronicaHeader.
     * 
     * @return
     *     possible object is
     *     {@link FatturaElettronica.FatturaElettronicaHeader }
     *     
     */
    public FatturaElettronica.FatturaElettronicaHeader getFatturaElettronicaHeader() {
        return fatturaElettronicaHeader;
    }

    /**
     * Imposta il valore della proprietà fatturaElettronicaHeader.
     * 
     * @param value
     *     allowed object is
     *     {@link FatturaElettronica.FatturaElettronicaHeader }
     *     
     */
    public void setFatturaElettronicaHeader(FatturaElettronica.FatturaElettronicaHeader value) {
        this.fatturaElettronicaHeader = value;
    }

    /**
     * Recupera il valore della proprietà fatturaElettronicaBody.
     * 
     * @return
     *     possible object is
     *     {@link FatturaElettronica.FatturaElettronicaBody }
     *     
     */
    public FatturaElettronica.FatturaElettronicaBody getFatturaElettronicaBody() {
        return fatturaElettronicaBody;
    }

    /**
     * Imposta il valore della proprietà fatturaElettronicaBody.
     * 
     * @param value
     *     allowed object is
     *     {@link FatturaElettronica.FatturaElettronicaBody }
     *     
     */
    public void setFatturaElettronicaBody(FatturaElettronica.FatturaElettronicaBody value) {
        this.fatturaElettronicaBody = value;
    }

    /**
     * Recupera il valore della proprietà versione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersione() {
        return versione;
    }

    /**
     * Imposta il valore della proprietà versione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersione(String value) {
        this.versione = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DatiGenerali">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DatiGeneraliDocumento">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="TipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Divisa" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="ImportoTotaleDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Causale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DatiBeniServizi">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DettaglioLinee" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="NumeroLinea" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Descrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="PrezzoUnitario" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="PrezzoTotale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="DatiRiepilogo">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="ImponibileImporto" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Imposta" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="EsigibilitaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DatiPagamento">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="CondizioniPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DettaglioPagamento">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ModalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="GiorniTerminiPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="ImportoPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "datiGenerali",
        "datiBeniServizi",
        "datiPagamento"
    })
    public static class FatturaElettronicaBody {

        @XmlElement(name = "DatiGenerali", required = true)
        protected FatturaElettronica.FatturaElettronicaBody.DatiGenerali datiGenerali;
        @XmlElement(name = "DatiBeniServizi", required = true)
        protected FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi datiBeniServizi;
        @XmlElement(name = "DatiPagamento", required = true)
        protected FatturaElettronica.FatturaElettronicaBody.DatiPagamento datiPagamento;

        /**
         * Recupera il valore della proprietà datiGenerali.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiGenerali }
         *     
         */
        public FatturaElettronica.FatturaElettronicaBody.DatiGenerali getDatiGenerali() {
            return datiGenerali;
        }

        /**
         * Imposta il valore della proprietà datiGenerali.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiGenerali }
         *     
         */
        public void setDatiGenerali(FatturaElettronica.FatturaElettronicaBody.DatiGenerali value) {
            this.datiGenerali = value;
        }

        /**
         * Recupera il valore della proprietà datiBeniServizi.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi }
         *     
         */
        public FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi getDatiBeniServizi() {
            return datiBeniServizi;
        }

        /**
         * Imposta il valore della proprietà datiBeniServizi.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi }
         *     
         */
        public void setDatiBeniServizi(FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi value) {
            this.datiBeniServizi = value;
        }

        /**
         * Recupera il valore della proprietà datiPagamento.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiPagamento }
         *     
         */
        public FatturaElettronica.FatturaElettronicaBody.DatiPagamento getDatiPagamento() {
            return datiPagamento;
        }

        /**
         * Imposta il valore della proprietà datiPagamento.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaBody.DatiPagamento }
         *     
         */
        public void setDatiPagamento(FatturaElettronica.FatturaElettronicaBody.DatiPagamento value) {
            this.datiPagamento = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DettaglioLinee" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="NumeroLinea" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Descrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="PrezzoUnitario" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="PrezzoTotale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="DatiRiepilogo">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="ImponibileImporto" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Imposta" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="EsigibilitaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "dettaglioLinee",
            "datiRiepilogo"
        })
        public static class DatiBeniServizi {

            @XmlElement(name = "DettaglioLinee")
            protected List<FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee> dettaglioLinee;
            @XmlElement(name = "DatiRiepilogo", required = true)
            protected FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo datiRiepilogo;

            /**
             * Gets the value of the dettaglioLinee property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the dettaglioLinee property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDettaglioLinee().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee }
             * 
             * 
             */
            public List<FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee> getDettaglioLinee() {
                if (dettaglioLinee == null) {
                    dettaglioLinee = new ArrayList<FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee>();
                }
                return this.dettaglioLinee;
            }

            /**
             * Recupera il valore della proprietà datiRiepilogo.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo }
             *     
             */
            public FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo getDatiRiepilogo() {
                return datiRiepilogo;
            }

            /**
             * Imposta il valore della proprietà datiRiepilogo.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo }
             *     
             */
            public void setDatiRiepilogo(FatturaElettronica.FatturaElettronicaBody.DatiBeniServizi.DatiRiepilogo value) {
                this.datiRiepilogo = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="ImponibileImporto" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Imposta" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="EsigibilitaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "aliquotaIVA",
                "imponibileImporto",
                "imposta",
                "esigibilitaIVA"
            })
            public static class DatiRiepilogo {

                @XmlElement(name = "AliquotaIVA", required = true)
                protected String aliquotaIVA;
                @XmlElement(name = "ImponibileImporto", required = true)
                protected String imponibileImporto;
                @XmlElement(name = "Imposta", required = true)
                protected String imposta;
                @XmlElement(name = "EsigibilitaIVA", required = true)
                protected String esigibilitaIVA;

                /**
                 * Recupera il valore della proprietà aliquotaIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAliquotaIVA() {
                    return aliquotaIVA;
                }

                /**
                 * Imposta il valore della proprietà aliquotaIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAliquotaIVA(String value) {
                    this.aliquotaIVA = value;
                }

                /**
                 * Recupera il valore della proprietà imponibileImporto.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImponibileImporto() {
                    return imponibileImporto;
                }

                /**
                 * Imposta il valore della proprietà imponibileImporto.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImponibileImporto(String value) {
                    this.imponibileImporto = value;
                }

                /**
                 * Recupera il valore della proprietà imposta.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImposta() {
                    return imposta;
                }

                /**
                 * Imposta il valore della proprietà imposta.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImposta(String value) {
                    this.imposta = value;
                }

                /**
                 * Recupera il valore della proprietà esigibilitaIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEsigibilitaIVA() {
                    return esigibilitaIVA;
                }

                /**
                 * Imposta il valore della proprietà esigibilitaIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEsigibilitaIVA(String value) {
                    this.esigibilitaIVA = value;
                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="NumeroLinea" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Descrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="PrezzoUnitario" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="PrezzoTotale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="AliquotaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "numeroLinea",
                "descrizione",
                "quantita",
                "prezzoUnitario",
                "prezzoTotale",
                "aliquotaIVA"
            })
            public static class DettaglioLinee {

                @XmlElement(name = "NumeroLinea", required = true)
                protected String numeroLinea;
                @XmlElement(name = "Descrizione", required = true)
                protected String descrizione;
                @XmlElement(name = "Quantita", required = true)
                protected String quantita;
                @XmlElement(name = "PrezzoUnitario", required = true)
                protected String prezzoUnitario;
                @XmlElement(name = "PrezzoTotale", required = true)
                protected String prezzoTotale;
                @XmlElement(name = "AliquotaIVA", required = true)
                protected String aliquotaIVA;

                /**
                 * Recupera il valore della proprietà numeroLinea.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNumeroLinea() {
                    return numeroLinea;
                }

                /**
                 * Imposta il valore della proprietà numeroLinea.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNumeroLinea(String value) {
                    this.numeroLinea = value;
                }

                /**
                 * Recupera il valore della proprietà descrizione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDescrizione() {
                    return descrizione;
                }

                /**
                 * Imposta il valore della proprietà descrizione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDescrizione(String value) {
                    this.descrizione = value;
                }

                /**
                 * Recupera il valore della proprietà quantita.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getQuantita() {
                    return quantita;
                }

                /**
                 * Imposta il valore della proprietà quantita.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setQuantita(String value) {
                    this.quantita = value;
                }

                /**
                 * Recupera il valore della proprietà prezzoUnitario.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPrezzoUnitario() {
                    return prezzoUnitario;
                }

                /**
                 * Imposta il valore della proprietà prezzoUnitario.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPrezzoUnitario(String value) {
                    this.prezzoUnitario = value;
                }

                /**
                 * Recupera il valore della proprietà prezzoTotale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPrezzoTotale() {
                    return prezzoTotale;
                }

                /**
                 * Imposta il valore della proprietà prezzoTotale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPrezzoTotale(String value) {
                    this.prezzoTotale = value;
                }

                /**
                 * Recupera il valore della proprietà aliquotaIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAliquotaIVA() {
                    return aliquotaIVA;
                }

                /**
                 * Imposta il valore della proprietà aliquotaIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAliquotaIVA(String value) {
                    this.aliquotaIVA = value;
                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DatiGeneraliDocumento">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="TipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Divisa" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="ImportoTotaleDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Causale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "datiGeneraliDocumento"
        })
        public static class DatiGenerali {

            @XmlElement(name = "DatiGeneraliDocumento", required = true)
            protected FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento datiGeneraliDocumento;

            /**
             * Recupera il valore della proprietà datiGeneraliDocumento.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento }
             *     
             */
            public FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento getDatiGeneraliDocumento() {
                return datiGeneraliDocumento;
            }

            /**
             * Imposta il valore della proprietà datiGeneraliDocumento.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento }
             *     
             */
            public void setDatiGeneraliDocumento(FatturaElettronica.FatturaElettronicaBody.DatiGenerali.DatiGeneraliDocumento value) {
                this.datiGeneraliDocumento = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="TipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Divisa" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="ImportoTotaleDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Causale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "tipoDocumento",
                "divisa",
                "data",
                "numero",
                "importoTotaleDocumento",
                "causale"
            })
            public static class DatiGeneraliDocumento {

                @XmlElement(name = "TipoDocumento", required = true)
                protected String tipoDocumento;
                @XmlElement(name = "Divisa", required = true)
                protected String divisa;
                @XmlElement(name = "Data", required = true)
                protected String data;
                @XmlElement(name = "Numero", required = true)
                protected String numero;
                @XmlElement(name = "ImportoTotaleDocumento", required = true)
                protected String importoTotaleDocumento;
                @XmlElement(name = "Causale", required = true)
                protected String causale;

                /**
                 * Recupera il valore della proprietà tipoDocumento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTipoDocumento() {
                    return tipoDocumento;
                }

                /**
                 * Imposta il valore della proprietà tipoDocumento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTipoDocumento(String value) {
                    this.tipoDocumento = value;
                }

                /**
                 * Recupera il valore della proprietà divisa.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDivisa() {
                    return divisa;
                }

                /**
                 * Imposta il valore della proprietà divisa.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDivisa(String value) {
                    this.divisa = value;
                }

                /**
                 * Recupera il valore della proprietà data.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getData() {
                    return data;
                }

                /**
                 * Imposta il valore della proprietà data.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setData(String value) {
                    this.data = value;
                }

                /**
                 * Recupera il valore della proprietà numero.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNumero() {
                    return numero;
                }

                /**
                 * Imposta il valore della proprietà numero.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNumero(String value) {
                    this.numero = value;
                }

                /**
                 * Recupera il valore della proprietà importoTotaleDocumento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImportoTotaleDocumento() {
                    return importoTotaleDocumento;
                }

                /**
                 * Imposta il valore della proprietà importoTotaleDocumento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImportoTotaleDocumento(String value) {
                    this.importoTotaleDocumento = value;
                }

                /**
                 * Recupera il valore della proprietà causale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCausale() {
                    return causale;
                }

                /**
                 * Imposta il valore della proprietà causale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCausale(String value) {
                    this.causale = value;
                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="CondizioniPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DettaglioPagamento">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ModalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="GiorniTerminiPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="ImportoPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "condizioniPagamento",
            "dettaglioPagamento"
        })
        public static class DatiPagamento {

            @XmlElement(name = "CondizioniPagamento", required = true)
            protected String condizioniPagamento;
            @XmlElement(name = "DettaglioPagamento", required = true)
            protected FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento dettaglioPagamento;

            /**
             * Recupera il valore della proprietà condizioniPagamento.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCondizioniPagamento() {
                return condizioniPagamento;
            }

            /**
             * Imposta il valore della proprietà condizioniPagamento.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCondizioniPagamento(String value) {
                this.condizioniPagamento = value;
            }

            /**
             * Recupera il valore della proprietà dettaglioPagamento.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento }
             *     
             */
            public FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento getDettaglioPagamento() {
                return dettaglioPagamento;
            }

            /**
             * Imposta il valore della proprietà dettaglioPagamento.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento }
             *     
             */
            public void setDettaglioPagamento(FatturaElettronica.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento value) {
                this.dettaglioPagamento = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="ModalitaPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="GiorniTerminiPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="ImportoPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "modalitaPagamento",
                "giorniTerminiPagamento",
                "importoPagamento"
            })
            public static class DettaglioPagamento {

                @XmlElement(name = "ModalitaPagamento", required = true)
                protected String modalitaPagamento;
                @XmlElement(name = "GiorniTerminiPagamento", required = true)
                protected String giorniTerminiPagamento;
                @XmlElement(name = "ImportoPagamento", required = true)
                protected String importoPagamento;

                /**
                 * Recupera il valore della proprietà modalitaPagamento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getModalitaPagamento() {
                    return modalitaPagamento;
                }

                /**
                 * Imposta il valore della proprietà modalitaPagamento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setModalitaPagamento(String value) {
                    this.modalitaPagamento = value;
                }

                /**
                 * Recupera il valore della proprietà giorniTerminiPagamento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getGiorniTerminiPagamento() {
                    return giorniTerminiPagamento;
                }

                /**
                 * Imposta il valore della proprietà giorniTerminiPagamento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setGiorniTerminiPagamento(String value) {
                    this.giorniTerminiPagamento = value;
                }

                /**
                 * Recupera il valore della proprietà importoPagamento.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImportoPagamento() {
                    return importoPagamento;
                }

                /**
                 * Imposta il valore della proprietà importoPagamento.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImportoPagamento(String value) {
                    this.importoPagamento = value;
                }

            }

        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DatiTrasmissione">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="IdTrasmittente">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="ProgressivoInvio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="FormatoTrasmissione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="CodiceDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="ContattiTrasmittente">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="PECDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="CedentePrestatore">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DatiAnagrafici">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IdFiscaleIVA">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Anagrafica">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="RegimeFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Sede">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="StabileOrganizzazione">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="IscrizioneREA">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Ufficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="NumeroREA" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="CapitaleSociale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="SocioUnico" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="StatoLiquidazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="RappresentanteFiscale">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DatiAnagrafici">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IdFiscaleIVA">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="Anagrafica">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="CessionarioCommittente">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DatiAnagrafici">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IdFiscaleIVA">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Anagrafica">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Sede">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TerzoIntermediarioOSoggettoEmittente">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DatiAnagrafici">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IdFiscaleIVA">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="Anagrafica">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SoggettoEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "datiTrasmissione",
        "cedentePrestatore",
        "rappresentanteFiscale",
        "cessionarioCommittente",
        "terzoIntermediarioOSoggettoEmittente",
        "soggettoEmittente"
    })
    public static class FatturaElettronicaHeader {

        @XmlElement(name = "DatiTrasmissione", required = true)
        protected FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione datiTrasmissione;
        @XmlElement(name = "CedentePrestatore", required = true)
        protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore cedentePrestatore;
        @XmlElement(name = "RappresentanteFiscale", required = true)
        protected FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale rappresentanteFiscale;
        @XmlElement(name = "CessionarioCommittente", required = true)
        protected FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente cessionarioCommittente;
        @XmlElement(name = "TerzoIntermediarioOSoggettoEmittente", required = true)
        protected FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente terzoIntermediarioOSoggettoEmittente;
        @XmlElement(name = "SoggettoEmittente", required = true)
        protected String soggettoEmittente;

        /**
         * Recupera il valore della proprietà datiTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione }
         *     
         */
        public FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione getDatiTrasmissione() {
            return datiTrasmissione;
        }

        /**
         * Imposta il valore della proprietà datiTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione }
         *     
         */
        public void setDatiTrasmissione(FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione value) {
            this.datiTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà cedentePrestatore.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore }
         *     
         */
        public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore getCedentePrestatore() {
            return cedentePrestatore;
        }

        /**
         * Imposta il valore della proprietà cedentePrestatore.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore }
         *     
         */
        public void setCedentePrestatore(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore value) {
            this.cedentePrestatore = value;
        }

        /**
         * Recupera il valore della proprietà rappresentanteFiscale.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale }
         *     
         */
        public FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale getRappresentanteFiscale() {
            return rappresentanteFiscale;
        }

        /**
         * Imposta il valore della proprietà rappresentanteFiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale }
         *     
         */
        public void setRappresentanteFiscale(FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale value) {
            this.rappresentanteFiscale = value;
        }

        /**
         * Recupera il valore della proprietà cessionarioCommittente.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente }
         *     
         */
        public FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente getCessionarioCommittente() {
            return cessionarioCommittente;
        }

        /**
         * Imposta il valore della proprietà cessionarioCommittente.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente }
         *     
         */
        public void setCessionarioCommittente(FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente value) {
            this.cessionarioCommittente = value;
        }

        /**
         * Recupera il valore della proprietà terzoIntermediarioOSoggettoEmittente.
         * 
         * @return
         *     possible object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente }
         *     
         */
        public FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente getTerzoIntermediarioOSoggettoEmittente() {
            return terzoIntermediarioOSoggettoEmittente;
        }

        /**
         * Imposta il valore della proprietà terzoIntermediarioOSoggettoEmittente.
         * 
         * @param value
         *     allowed object is
         *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente }
         *     
         */
        public void setTerzoIntermediarioOSoggettoEmittente(FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente value) {
            this.terzoIntermediarioOSoggettoEmittente = value;
        }

        /**
         * Recupera il valore della proprietà soggettoEmittente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSoggettoEmittente() {
            return soggettoEmittente;
        }

        /**
         * Imposta il valore della proprietà soggettoEmittente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSoggettoEmittente(String value) {
            this.soggettoEmittente = value;
        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DatiAnagrafici">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IdFiscaleIVA">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Anagrafica">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="RegimeFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Sede">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="StabileOrganizzazione">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="IscrizioneREA">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Ufficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="NumeroREA" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="CapitaleSociale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="SocioUnico" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="StatoLiquidazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "datiAnagrafici",
            "sede",
            "stabileOrganizzazione",
            "iscrizioneREA"
        })
        public static class CedentePrestatore {

            @XmlElement(name = "DatiAnagrafici", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici datiAnagrafici;
            @XmlElement(name = "Sede", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede sede;
            @XmlElement(name = "StabileOrganizzazione", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione stabileOrganizzazione;
            @XmlElement(name = "IscrizioneREA", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA iscrizioneREA;

            /**
             * Recupera il valore della proprietà datiAnagrafici.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici getDatiAnagrafici() {
                return datiAnagrafici;
            }

            /**
             * Imposta il valore della proprietà datiAnagrafici.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici }
             *     
             */
            public void setDatiAnagrafici(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici value) {
                this.datiAnagrafici = value;
            }

            /**
             * Recupera il valore della proprietà sede.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede getSede() {
                return sede;
            }

            /**
             * Imposta il valore della proprietà sede.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede }
             *     
             */
            public void setSede(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.Sede value) {
                this.sede = value;
            }

            /**
             * Recupera il valore della proprietà stabileOrganizzazione.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione getStabileOrganizzazione() {
                return stabileOrganizzazione;
            }

            /**
             * Imposta il valore della proprietà stabileOrganizzazione.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione }
             *     
             */
            public void setStabileOrganizzazione(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.StabileOrganizzazione value) {
                this.stabileOrganizzazione = value;
            }

            /**
             * Recupera il valore della proprietà iscrizioneREA.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA getIscrizioneREA() {
                return iscrizioneREA;
            }

            /**
             * Imposta il valore della proprietà iscrizioneREA.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA }
             *     
             */
            public void setIscrizioneREA(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.IscrizioneREA value) {
                this.iscrizioneREA = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IdFiscaleIVA">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Anagrafica">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="RegimeFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idFiscaleIVA",
                "codiceFiscale",
                "anagrafica",
                "regimeFiscale"
            })
            public static class DatiAnagrafici {

                @XmlElement(name = "IdFiscaleIVA", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA idFiscaleIVA;
                @XmlElement(name = "CodiceFiscale", required = true)
                protected String codiceFiscale;
                @XmlElement(name = "Anagrafica", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica anagrafica;
                @XmlElement(name = "RegimeFiscale", required = true)
                protected String regimeFiscale;

                /**
                 * Recupera il valore della proprietà idFiscaleIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA getIdFiscaleIVA() {
                    return idFiscaleIVA;
                }

                /**
                 * Imposta il valore della proprietà idFiscaleIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public void setIdFiscaleIVA(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.IdFiscaleIVA value) {
                    this.idFiscaleIVA = value;
                }

                /**
                 * Recupera il valore della proprietà codiceFiscale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodiceFiscale() {
                    return codiceFiscale;
                }

                /**
                 * Imposta il valore della proprietà codiceFiscale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodiceFiscale(String value) {
                    this.codiceFiscale = value;
                }

                /**
                 * Recupera il valore della proprietà anagrafica.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica getAnagrafica() {
                    return anagrafica;
                }

                /**
                 * Imposta il valore della proprietà anagrafica.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public void setAnagrafica(FatturaElettronica.FatturaElettronicaHeader.CedentePrestatore.DatiAnagrafici.Anagrafica value) {
                    this.anagrafica = value;
                }

                /**
                 * Recupera il valore della proprietà regimeFiscale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getRegimeFiscale() {
                    return regimeFiscale;
                }

                /**
                 * Imposta il valore della proprietà regimeFiscale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setRegimeFiscale(String value) {
                    this.regimeFiscale = value;
                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "denominazione"
                })
                public static class Anagrafica {

                    @XmlElement(name = "Denominazione", required = true)
                    protected String denominazione;

                    /**
                     * Recupera il valore della proprietà denominazione.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDenominazione() {
                        return denominazione;
                    }

                    /**
                     * Imposta il valore della proprietà denominazione.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDenominazione(String value) {
                        this.denominazione = value;
                    }

                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "idPaese",
                    "idCodice"
                })
                public static class IdFiscaleIVA {

                    @XmlElement(name = "IdPaese", required = true)
                    protected String idPaese;
                    @XmlElement(name = "IdCodice", required = true)
                    protected String idCodice;

                    /**
                     * Recupera il valore della proprietà idPaese.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdPaese() {
                        return idPaese;
                    }

                    /**
                     * Imposta il valore della proprietà idPaese.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdPaese(String value) {
                        this.idPaese = value;
                    }

                    /**
                     * Recupera il valore della proprietà idCodice.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdCodice() {
                        return idCodice;
                    }

                    /**
                     * Imposta il valore della proprietà idCodice.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdCodice(String value) {
                        this.idCodice = value;
                    }

                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Ufficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="NumeroREA" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="CapitaleSociale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="SocioUnico" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="StatoLiquidazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "ufficio",
                "numeroREA",
                "capitaleSociale",
                "socioUnico",
                "statoLiquidazione"
            })
            public static class IscrizioneREA {

                @XmlElement(name = "Ufficio", required = true)
                protected String ufficio;
                @XmlElement(name = "NumeroREA", required = true)
                protected String numeroREA;
                @XmlElement(name = "CapitaleSociale", required = true)
                protected String capitaleSociale;
                @XmlElement(name = "SocioUnico", required = true)
                protected String socioUnico;
                @XmlElement(name = "StatoLiquidazione", required = true)
                protected String statoLiquidazione;

                /**
                 * Recupera il valore della proprietà ufficio.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getUfficio() {
                    return ufficio;
                }

                /**
                 * Imposta il valore della proprietà ufficio.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUfficio(String value) {
                    this.ufficio = value;
                }

                /**
                 * Recupera il valore della proprietà numeroREA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNumeroREA() {
                    return numeroREA;
                }

                /**
                 * Imposta il valore della proprietà numeroREA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNumeroREA(String value) {
                    this.numeroREA = value;
                }

                /**
                 * Recupera il valore della proprietà capitaleSociale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCapitaleSociale() {
                    return capitaleSociale;
                }

                /**
                 * Imposta il valore della proprietà capitaleSociale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCapitaleSociale(String value) {
                    this.capitaleSociale = value;
                }

                /**
                 * Recupera il valore della proprietà socioUnico.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSocioUnico() {
                    return socioUnico;
                }

                /**
                 * Imposta il valore della proprietà socioUnico.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSocioUnico(String value) {
                    this.socioUnico = value;
                }

                /**
                 * Recupera il valore della proprietà statoLiquidazione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getStatoLiquidazione() {
                    return statoLiquidazione;
                }

                /**
                 * Imposta il valore della proprietà statoLiquidazione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setStatoLiquidazione(String value) {
                    this.statoLiquidazione = value;
                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "indirizzo",
                "cap",
                "comune",
                "provincia",
                "nazione"
            })
            public static class Sede {

                @XmlElement(name = "Indirizzo", required = true)
                protected String indirizzo;
                @XmlElement(name = "CAP", required = true)
                protected String cap;
                @XmlElement(name = "Comune", required = true)
                protected String comune;
                @XmlElement(name = "Provincia", required = true)
                protected String provincia;
                @XmlElement(name = "Nazione", required = true)
                protected String nazione;

                /**
                 * Recupera il valore della proprietà indirizzo.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndirizzo() {
                    return indirizzo;
                }

                /**
                 * Imposta il valore della proprietà indirizzo.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndirizzo(String value) {
                    this.indirizzo = value;
                }

                /**
                 * Recupera il valore della proprietà cap.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCAP() {
                    return cap;
                }

                /**
                 * Imposta il valore della proprietà cap.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCAP(String value) {
                    this.cap = value;
                }

                /**
                 * Recupera il valore della proprietà comune.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getComune() {
                    return comune;
                }

                /**
                 * Imposta il valore della proprietà comune.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setComune(String value) {
                    this.comune = value;
                }

                /**
                 * Recupera il valore della proprietà provincia.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getProvincia() {
                    return provincia;
                }

                /**
                 * Imposta il valore della proprietà provincia.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setProvincia(String value) {
                    this.provincia = value;
                }

                /**
                 * Recupera il valore della proprietà nazione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNazione() {
                    return nazione;
                }

                /**
                 * Imposta il valore della proprietà nazione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNazione(String value) {
                    this.nazione = value;
                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "indirizzo",
                "cap",
                "comune",
                "provincia",
                "nazione"
            })
            public static class StabileOrganizzazione {

                @XmlElement(name = "Indirizzo", required = true)
                protected String indirizzo;
                @XmlElement(name = "CAP", required = true)
                protected String cap;
                @XmlElement(name = "Comune", required = true)
                protected String comune;
                @XmlElement(name = "Provincia", required = true)
                protected String provincia;
                @XmlElement(name = "Nazione", required = true)
                protected String nazione;

                /**
                 * Recupera il valore della proprietà indirizzo.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndirizzo() {
                    return indirizzo;
                }

                /**
                 * Imposta il valore della proprietà indirizzo.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndirizzo(String value) {
                    this.indirizzo = value;
                }

                /**
                 * Recupera il valore della proprietà cap.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCAP() {
                    return cap;
                }

                /**
                 * Imposta il valore della proprietà cap.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCAP(String value) {
                    this.cap = value;
                }

                /**
                 * Recupera il valore della proprietà comune.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getComune() {
                    return comune;
                }

                /**
                 * Imposta il valore della proprietà comune.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setComune(String value) {
                    this.comune = value;
                }

                /**
                 * Recupera il valore della proprietà provincia.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getProvincia() {
                    return provincia;
                }

                /**
                 * Imposta il valore della proprietà provincia.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setProvincia(String value) {
                    this.provincia = value;
                }

                /**
                 * Recupera il valore della proprietà nazione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNazione() {
                    return nazione;
                }

                /**
                 * Imposta il valore della proprietà nazione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNazione(String value) {
                    this.nazione = value;
                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DatiAnagrafici">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IdFiscaleIVA">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Anagrafica">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Sede">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "datiAnagrafici",
            "sede"
        })
        public static class CessionarioCommittente {

            @XmlElement(name = "DatiAnagrafici", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici datiAnagrafici;
            @XmlElement(name = "Sede", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede sede;

            /**
             * Recupera il valore della proprietà datiAnagrafici.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici getDatiAnagrafici() {
                return datiAnagrafici;
            }

            /**
             * Imposta il valore della proprietà datiAnagrafici.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici }
             *     
             */
            public void setDatiAnagrafici(FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici value) {
                this.datiAnagrafici = value;
            }

            /**
             * Recupera il valore della proprietà sede.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede getSede() {
                return sede;
            }

            /**
             * Imposta il valore della proprietà sede.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede }
             *     
             */
            public void setSede(FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.Sede value) {
                this.sede = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IdFiscaleIVA">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="CodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Anagrafica">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idFiscaleIVA",
                "codiceFiscale",
                "anagrafica"
            })
            public static class DatiAnagrafici {

                @XmlElement(name = "IdFiscaleIVA", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA idFiscaleIVA;
                @XmlElement(name = "CodiceFiscale", required = true)
                protected String codiceFiscale;
                @XmlElement(name = "Anagrafica", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica anagrafica;

                /**
                 * Recupera il valore della proprietà idFiscaleIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA getIdFiscaleIVA() {
                    return idFiscaleIVA;
                }

                /**
                 * Imposta il valore della proprietà idFiscaleIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public void setIdFiscaleIVA(FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.IdFiscaleIVA value) {
                    this.idFiscaleIVA = value;
                }

                /**
                 * Recupera il valore della proprietà codiceFiscale.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCodiceFiscale() {
                    return codiceFiscale;
                }

                /**
                 * Imposta il valore della proprietà codiceFiscale.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCodiceFiscale(String value) {
                    this.codiceFiscale = value;
                }

                /**
                 * Recupera il valore della proprietà anagrafica.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica getAnagrafica() {
                    return anagrafica;
                }

                /**
                 * Imposta il valore della proprietà anagrafica.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public void setAnagrafica(FatturaElettronica.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica value) {
                    this.anagrafica = value;
                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "denominazione"
                })
                public static class Anagrafica {

                    @XmlElement(name = "Denominazione", required = true)
                    protected String denominazione;

                    /**
                     * Recupera il valore della proprietà denominazione.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDenominazione() {
                        return denominazione;
                    }

                    /**
                     * Imposta il valore della proprietà denominazione.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDenominazione(String value) {
                        this.denominazione = value;
                    }

                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "idPaese",
                    "idCodice"
                })
                public static class IdFiscaleIVA {

                    @XmlElement(name = "IdPaese", required = true)
                    protected String idPaese;
                    @XmlElement(name = "IdCodice", required = true)
                    protected String idCodice;

                    /**
                     * Recupera il valore della proprietà idPaese.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdPaese() {
                        return idPaese;
                    }

                    /**
                     * Imposta il valore della proprietà idPaese.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdPaese(String value) {
                        this.idPaese = value;
                    }

                    /**
                     * Recupera il valore della proprietà idCodice.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdCodice() {
                        return idCodice;
                    }

                    /**
                     * Imposta il valore della proprietà idCodice.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdCodice(String value) {
                        this.idCodice = value;
                    }

                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Comune" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="Nazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "indirizzo",
                "cap",
                "comune",
                "provincia",
                "nazione"
            })
            public static class Sede {

                @XmlElement(name = "Indirizzo", required = true)
                protected String indirizzo;
                @XmlElement(name = "CAP", required = true)
                protected String cap;
                @XmlElement(name = "Comune", required = true)
                protected String comune;
                @XmlElement(name = "Provincia", required = true)
                protected String provincia;
                @XmlElement(name = "Nazione", required = true)
                protected String nazione;

                /**
                 * Recupera il valore della proprietà indirizzo.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndirizzo() {
                    return indirizzo;
                }

                /**
                 * Imposta il valore della proprietà indirizzo.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndirizzo(String value) {
                    this.indirizzo = value;
                }

                /**
                 * Recupera il valore della proprietà cap.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCAP() {
                    return cap;
                }

                /**
                 * Imposta il valore della proprietà cap.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCAP(String value) {
                    this.cap = value;
                }

                /**
                 * Recupera il valore della proprietà comune.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getComune() {
                    return comune;
                }

                /**
                 * Imposta il valore della proprietà comune.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setComune(String value) {
                    this.comune = value;
                }

                /**
                 * Recupera il valore della proprietà provincia.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getProvincia() {
                    return provincia;
                }

                /**
                 * Imposta il valore della proprietà provincia.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setProvincia(String value) {
                    this.provincia = value;
                }

                /**
                 * Recupera il valore della proprietà nazione.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNazione() {
                    return nazione;
                }

                /**
                 * Imposta il valore della proprietà nazione.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNazione(String value) {
                    this.nazione = value;
                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="IdTrasmittente">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="ProgressivoInvio" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="FormatoTrasmissione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="CodiceDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="ContattiTrasmittente">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="PECDestinatario" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "idTrasmittente",
            "progressivoInvio",
            "formatoTrasmissione",
            "codiceDestinatario",
            "pecDestinatario",
            "contattiTrasmittente"
        })
        public static class DatiTrasmissione {

            @XmlElement(name = "IdTrasmittente", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente idTrasmittente;
            @XmlElement(name = "ProgressivoInvio", required = true)
            protected String progressivoInvio;
            @XmlElement(name = "FormatoTrasmissione", required = true)
            protected String formatoTrasmissione;
            @XmlElement(name = "CodiceDestinatario", required = true)
            protected String codiceDestinatario;
            @XmlElement(name = "ContattiTrasmittente", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente contattiTrasmittente;
            @XmlElement(name = "PECDestinatario", required = true)
            protected String pecDestinatario;
            
            /**
             * Recupera il valore della proprietà idTrasmittente.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente getIdTrasmittente() {
                return idTrasmittente;
            }

            /**
             * Imposta il valore della proprietà idTrasmittente.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente }
             *     
             */
            public void setIdTrasmittente(FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.IdTrasmittente value) {
                this.idTrasmittente = value;
            }

            /**
             * Recupera il valore della proprietà progressivoInvio.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProgressivoInvio() {
                return progressivoInvio;
            }

            /**
             * Imposta il valore della proprietà progressivoInvio.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProgressivoInvio(String value) {
                this.progressivoInvio = value;
            }

            /**
             * Recupera il valore della proprietà formatoTrasmissione.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFormatoTrasmissione() {
                return formatoTrasmissione;
            }

            /**
             * Imposta il valore della proprietà formatoTrasmissione.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFormatoTrasmissione(String value) {
                this.formatoTrasmissione = value;
            }

            /**
             * Recupera il valore della proprietà codiceDestinatario.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodiceDestinatario() {
                return codiceDestinatario;
            }

            /**
             * Imposta il valore della proprietà codiceDestinatario.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodiceDestinatario(String value) {
                this.codiceDestinatario = value;
            }

            /**
             * Recupera il valore della proprietà pecDestinatario.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPECDestinatario() {
                return pecDestinatario;
            }

            /**
             * Imposta il valore della proprietà pecDestinatario.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPECDestinatario(String value) {
                this.pecDestinatario = value;
            }

            /**
             * Recupera il valore della proprietà contattiTrasmittente.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente getContattiTrasmittente() {
                return contattiTrasmittente;
            }

            /**
             * Imposta il valore della proprietà contattiTrasmittente.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente }
             *     
             */
            public void setContattiTrasmittente(FatturaElettronica.FatturaElettronicaHeader.DatiTrasmissione.ContattiTrasmittente value) {
                this.contattiTrasmittente = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "Email"
            })
            public static class ContattiTrasmittente {

                @XmlElement(required = true)
                protected String Email;

                /**
                 * Recupera il valore della proprietà Email.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEmail() {
                    return Email;
                }

                /**
                 * Imposta il valore della proprietà Email.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEmail(String value) {
                    this.Email = value;
                }

            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idPaese",
                "idCodice"
            })
            public static class IdTrasmittente {

                @XmlElement(name = "IdPaese", required = true)
                protected String idPaese;
                @XmlElement(name = "IdCodice", required = true)
                protected String idCodice;

                /**
                 * Recupera il valore della proprietà idPaese.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIdPaese() {
                    return idPaese;
                }

                /**
                 * Imposta il valore della proprietà idPaese.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIdPaese(String value) {
                    this.idPaese = value;
                }

                /**
                 * Recupera il valore della proprietà idCodice.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIdCodice() {
                    return idCodice;
                }

                /**
                 * Imposta il valore della proprietà idCodice.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIdCodice(String value) {
                    this.idCodice = value;
                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DatiAnagrafici">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IdFiscaleIVA">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="Anagrafica">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "datiAnagrafici"
        })
        public static class RappresentanteFiscale {

            @XmlElement(name = "DatiAnagrafici", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici datiAnagrafici;

            /**
             * Recupera il valore della proprietà datiAnagrafici.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici getDatiAnagrafici() {
                return datiAnagrafici;
            }

            /**
             * Imposta il valore della proprietà datiAnagrafici.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici }
             *     
             */
            public void setDatiAnagrafici(FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici value) {
                this.datiAnagrafici = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IdFiscaleIVA">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="Anagrafica">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idFiscaleIVA",
                "anagrafica"
            })
            public static class DatiAnagrafici {

                @XmlElement(name = "IdFiscaleIVA", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA idFiscaleIVA;
                @XmlElement(name = "Anagrafica", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica anagrafica;

                /**
                 * Recupera il valore della proprietà idFiscaleIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA getIdFiscaleIVA() {
                    return idFiscaleIVA;
                }

                /**
                 * Imposta il valore della proprietà idFiscaleIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public void setIdFiscaleIVA(FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.IdFiscaleIVA value) {
                    this.idFiscaleIVA = value;
                }

                /**
                 * Recupera il valore della proprietà anagrafica.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica getAnagrafica() {
                    return anagrafica;
                }

                /**
                 * Imposta il valore della proprietà anagrafica.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public void setAnagrafica(FatturaElettronica.FatturaElettronicaHeader.RappresentanteFiscale.DatiAnagrafici.Anagrafica value) {
                    this.anagrafica = value;
                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "denominazione"
                })
                public static class Anagrafica {

                    @XmlElement(name = "Denominazione", required = true)
                    protected String denominazione;

                    /**
                     * Recupera il valore della proprietà denominazione.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDenominazione() {
                        return denominazione;
                    }

                    /**
                     * Imposta il valore della proprietà denominazione.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDenominazione(String value) {
                        this.denominazione = value;
                    }

                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "idPaese",
                    "idCodice"
                })
                public static class IdFiscaleIVA {

                    @XmlElement(name = "IdPaese", required = true)
                    protected String idPaese;
                    @XmlElement(name = "IdCodice", required = true)
                    protected String idCodice;

                    /**
                     * Recupera il valore della proprietà idPaese.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdPaese() {
                        return idPaese;
                    }

                    /**
                     * Imposta il valore della proprietà idPaese.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdPaese(String value) {
                        this.idPaese = value;
                    }

                    /**
                     * Recupera il valore della proprietà idCodice.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdCodice() {
                        return idCodice;
                    }

                    /**
                     * Imposta il valore della proprietà idCodice.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdCodice(String value) {
                        this.idCodice = value;
                    }

                }

            }

        }


        /**
         * <p>Classe Java per anonymous complex type.
         * 
         * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DatiAnagrafici">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IdFiscaleIVA">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="Anagrafica">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "datiAnagrafici"
        })
        public static class TerzoIntermediarioOSoggettoEmittente {

            @XmlElement(name = "DatiAnagrafici", required = true)
            protected FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici datiAnagrafici;

            /**
             * Recupera il valore della proprietà datiAnagrafici.
             * 
             * @return
             *     possible object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici }
             *     
             */
            public FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici getDatiAnagrafici() {
                return datiAnagrafici;
            }

            /**
             * Imposta il valore della proprietà datiAnagrafici.
             * 
             * @param value
             *     allowed object is
             *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici }
             *     
             */
            public void setDatiAnagrafici(FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici value) {
                this.datiAnagrafici = value;
            }


            /**
             * <p>Classe Java per anonymous complex type.
             * 
             * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IdFiscaleIVA">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="Anagrafica">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idFiscaleIVA",
                "anagrafica"
            })
            public static class DatiAnagrafici {

                @XmlElement(name = "IdFiscaleIVA", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA idFiscaleIVA;
                @XmlElement(name = "Anagrafica", required = true)
                protected FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica anagrafica;

                /**
                 * Recupera il valore della proprietà idFiscaleIVA.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA getIdFiscaleIVA() {
                    return idFiscaleIVA;
                }

                /**
                 * Imposta il valore della proprietà idFiscaleIVA.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA }
                 *     
                 */
                public void setIdFiscaleIVA(FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.IdFiscaleIVA value) {
                    this.idFiscaleIVA = value;
                }

                /**
                 * Recupera il valore della proprietà anagrafica.
                 * 
                 * @return
                 *     possible object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica getAnagrafica() {
                    return anagrafica;
                }

                /**
                 * Imposta il valore della proprietà anagrafica.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica }
                 *     
                 */
                public void setAnagrafica(FatturaElettronica.FatturaElettronicaHeader.TerzoIntermediarioOSoggettoEmittente.DatiAnagrafici.Anagrafica value) {
                    this.anagrafica = value;
                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="Denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "denominazione"
                })
                public static class Anagrafica {

                    @XmlElement(name = "Denominazione", required = true)
                    protected String denominazione;

                    /**
                     * Recupera il valore della proprietà denominazione.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDenominazione() {
                        return denominazione;
                    }

                    /**
                     * Imposta il valore della proprietà denominazione.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDenominazione(String value) {
                        this.denominazione = value;
                    }

                }


                /**
                 * <p>Classe Java per anonymous complex type.
                 * 
                 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="IdPaese" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="IdCodice" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "idPaese",
                    "idCodice"
                })
                public static class IdFiscaleIVA {

                    @XmlElement(name = "IdPaese", required = true)
                    protected String idPaese;
                    @XmlElement(name = "IdCodice", required = true)
                    protected String idCodice;

                    /**
                     * Recupera il valore della proprietà idPaese.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdPaese() {
                        return idPaese;
                    }

                    /**
                     * Imposta il valore della proprietà idPaese.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdPaese(String value) {
                        this.idPaese = value;
                    }

                    /**
                     * Recupera il valore della proprietà idCodice.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIdCodice() {
                        return idCodice;
                    }

                    /**
                     * Imposta il valore della proprietà idCodice.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIdCodice(String value) {
                        this.idCodice = value;
                    }

                }

            }

        }

    }

}
