package com.mediatica.vouchain.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.TransactionDetailDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.AssetDTO;
import com.mediatica.vouchain.dto.GetIncomingTransactionsRequestDTO;
import com.mediatica.vouchain.dto.GetIncomingTransactionsResponseDTO;
import com.mediatica.vouchain.dto.IncomingTransactionsDTO;
import com.mediatica.vouchain.dto.RequestIsTXConfirmedDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseIsTxCofirmedDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class TransactionHandlerUtils {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(TransactionHandler.class);

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	@Autowired
	TransactionDetailDaoImpl transactionDetailDaoImpl;

	@Value("${transaction_handler_enabled}")
	private String transactionHandlerEnabled;

	@Value("${transaction_delta_time}")
	private int transactionDeltaTime;

	@Value("${confirmation_number}")
	private int confirmationNumber;

	@Autowired
	BlockChainRestClient blockChainClient;

	@Autowired
	UserDaoImpl userDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void managingOtherCases(Transaction t) {
		try {
			if (t.getTrcTxId() == null || t.getTrcTxId().isEmpty()) {
				log.info("we are in the case that txId is null so we have to call again the blockchain");
				GetIncomingTransactionsResponseDTO response = callAPiBlockchain(t);
				if (response != null) {
					Set<String> txIdList = getTransactionId(t, response);

					if (txIdList == null || txIdList.isEmpty()) {
						log.info("No transaction found in blockchain let's remove the transaction: {}", t.getTrcId());
						if (t.getTransactionDetailCollection() != null
								&& !t.getTransactionDetailCollection().isEmpty()) {
							transactionDaoImpl.removeTransactionsDetails(t);
						}
						transactionDaoImpl.removePhysical2(t);
					} else {
						manageUpdateTransactionId(txIdList, t);
					}
				}
			} else {
				log.info("Going to verify if the transaction is confirmed");
				RequestIsTXConfirmedDTO request = new RequestIsTXConfirmedDTO();
				request.setTxid(t.getTrcTxId());
				ResponseIsTxCofirmedDTO response = blockChainClient.invokeIsTxConfirmed(request);
				if (response.getError() == null || response.getError().isEmpty()) {
					if (response.getResult().getConfirmations() >= confirmationNumber) {
						t.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
						transactionDaoImpl.update(t);
					}
				} else {
					throw new Exception(
							"Blockchain isTxConfirmed service answered with a error: " + response.getError());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e.getStackTrace());
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void manageUpdateTransactionId(Set<String> txIdList, Transaction t) {
		try {
			boolean updated = tryToUpdateIdxList(txIdList, t);
			if (!updated) {
				log.info("didn't found any transaction id let's remove the transaction {}", t.getTrcId());
				try {
					transactionDaoImpl.removePhysical2(t);
				} catch (Exception e) {
					log.info("Error removing tranaction t: {}" + t.getTrcId());
					Collection<TransactionDetail> list = t.getTransactionDetailCollection();
					for (TransactionDetail td : list) {
						log.info("removing td: " + td.getTransactionDetailPK().getTrcId());
						transactionDetailDaoImpl.removePhysical(td);

					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private boolean tryToUpdateIdxList(Set<String> txIdList, Transaction t) {
		boolean updated = false;
		for (String txId : txIdList) {
			// t.setTrcTxId(txId);
			try {
				transactionDaoImpl.update2(t, txId);
				updated = true;
				log.info("Recovered the transaction {} with transactionId {}", t.getTrcId(), txId);
				break;
			} catch (Exception e) {
				log.warn("Possible duplicate key let's continue anymore");
			}
		}
		return updated;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private GetIncomingTransactionsResponseDTO callAPiBlockchain(Transaction t) {
		GetIncomingTransactionsResponseDTO response = null;
		try {
			GetIncomingTransactionsRequestDTO request = buildRequest(t);

			if (t.getTrcType().equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
				log.debug("Calling invokeGetOutgoingTransactions");
				response = blockChainClient.invokeGetOutgoingTransactions(request);
				log.debug("Called invokeGetOutgoingTransactions");
			} else {
				log.debug("Calling invokeGetIncomingTransactions");
				response = blockChainClient.invokeGetIncomingTransactions(request);
				log.debug("Called invokeGetIncomingTransactions");
			}
			if (response.getError() != null && !response.getError().isEmpty()) {
				log.error("BlockChain answered with error: {}", response.getError());
				response = null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return response;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void manageNewUserCase(Transaction t) throws Exception {
		log.info("{} it is a transactione of type NWU ", t.getTrcId());
		User user = t.getUsrIdDa();
		if (user.getUsrBchAddress() == null || user.getUsrPrivateKey() == null) {
			log.info("Something went wrong in the creation of the user let's try to invoke again the blockchain API");
			callAPIBlockChainForUserRegistration(user, t);
		} else {
			log.info("all went right in the creation we only put his state to 1 so we dont evaluate him anymore");
			t.setTrcState(TransactionStatus.CONFIRMED.getValue());
			transactionDaoImpl.update(t);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private Set<String> getTransactionId(Transaction t, GetIncomingTransactionsResponseDTO response) {
		Set<String> transactionIdList = new HashSet();
		if (response.getError() == null) {
			log.info("The response hasn't error let's continue to find the list of possible transactionId");
			List<IncomingTransactionsDTO> transactionList = response.getResult().getTransactions();

			for (IncomingTransactionsDTO item : transactionList) {
				List<AssetDTO> assetList = item.getAssets();
				for (AssetDTO asset : assetList) {
					if (verifyEquals(item, asset, t)) {
						log.info("Found a possible transactionID: {} for transaction {}", item.getTxid(), t.getTrcId());
						transactionIdList.add(item.getTxid());
					}
				}
			}
		} else {
			log.error(response.getError());
			transactionIdList = null;
		}
		return transactionIdList;
	}

	private boolean verifyEquals(IncomingTransactionsDTO item, AssetDTO asset, Transaction t) {
		boolean isTheSame = false;
		if (t.getTrcType().equals(TransactionType.COMPANY_BUY_VOUCHER.getDescription())) {
			log.debug("Verifying {} case", TransactionType.COMPANY_BUY_VOUCHER.getDescription());
			if (item.getReceiver().equalsIgnoreCase(t.getUsrIdA().getUsrBchAddress())
					&& verifyAsset(asset, t.getTransactionDetailCollection())) {
				isTheSame = true;
			}
		} else {

			if (t.getTrcType().equals(TransactionType.REDEEM_VOUCHER.getDescription())) {
				log.debug("Verifying {} case", TransactionType.REDEEM_VOUCHER.getDescription());
				if (item.getSender().equalsIgnoreCase(t.getUsrIdDa().getUsrBchAddress())
						&& verifyAsset(asset, t.getTransactionDetailCollection())) {
					isTheSame = true;
				}
			} else {
				log.debug("Verifying {} case", "SPS,ALL");
				if (item.getSender().equalsIgnoreCase(t.getUsrIdDa().getUsrBchAddress())
						&& item.getReceiver().equalsIgnoreCase(t.getUsrIdA().getUsrBchAddress())
						&& verifyAsset(asset, t.getTransactionDetailCollection())) {
					isTheSame = true;
				}
			}
		}

		return isTheSame;
	}

	private boolean verifyAsset(AssetDTO asset, Collection<TransactionDetail> transactionDetailCollection) {
		boolean found = false;
		for (TransactionDetail item : transactionDetailCollection) {
			if (item.getVoucher().getVchName().equals(asset.getName())
					&& item.getTrdQuantity().longValue() == asset.getQty()) {
				found = true;
				break;
			}
		}
		return found;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void callAPIBlockChainForUserRegistration(User user, Transaction trx) throws Exception {
		////// calling blockchain

		log.info("Calling BlockChain Services in companySignUp: " + new Date());
		ResponseNewKeyPairDTO keyPairResult = blockChainClient.invokeNewKeyPair();
		if (keyPairResult.getError() == null || keyPairResult.getError().isEmpty()) {
			user.setUsrBchAddress(keyPairResult.getResult().getAddress());
			ResponseGrantPermissionDTO grantResult = null;
			if (user.getCompany() != null) {
				grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(),
						Constants.ENTITIES_TYPE.COMPANY.toString());
			} else {
				if (user.getEmployee() != null) {
					grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(),
							Constants.ENTITIES_TYPE.EMPLOYEE.toString());
				} else {
					if (user.getMerchant() != null) {
						grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(),
								Constants.ENTITIES_TYPE.MERCHANT.toString());
					} else {
						throw new Exception("No type founfdfor user: " + user.getUsrId());
					}
				}
			}
			if (grantResult.getError() == null || grantResult.getError().isEmpty()) {
				user.setUsrPrivateKey(keyPairResult.getResult().getPrivateKey());
				String trcTxId = grantResult.getResult();
				trx.setTrcTxId(trcTxId);

				////// updating user and transaction with the information retrieved from the
				////// blockchain
				log.debug("updating in the database the user: {}", user.getUsrEmail());
				userDao.update(user);
				log.debug("updating in the database the transaction: {}", trx.getTrcId());
				trx.setTrcState(TransactionStatus.CONFIRMED.getValue());
				transactionDaoImpl.insert(trx);
			} else {
				throw new Exception(grantResult.getError());
			}
		} else {
			throw new Exception(keyPairResult.getError());
		}
		//////

	}

	private GetIncomingTransactionsRequestDTO buildRequest(Transaction t) {
		GetIncomingTransactionsRequestDTO request = new GetIncomingTransactionsRequestDTO();
		String address = "";

		// quale user usare?
		if (t.getTrcType().equals(TransactionType.COMPANY_BUY_VOUCHER.getDescription())) {
			address = t.getUsrIdA().getUsrBchAddress();
		}
		if (t.getTrcType().equals(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription())) {
			address = t.getUsrIdA().getUsrBchAddress();
		}
		if (t.getTrcType().equals(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription())) {
			address = t.getUsrIdA().getUsrBchAddress();
		}
		if (t.getTrcType().equals(TransactionType.REDEEM_VOUCHER.getDescription())) {
			address = t.getUsrIdDa().getUsrBchAddress();
		}
		request.setAddress(address);
		// outgoing
		Collection<TransactionDetail> detailList = t.getTransactionDetailCollection();
		List<String> assets = new ArrayList<String>();
		for (TransactionDetail item : detailList) {
			assets.add(item.getVoucher().getVchName());
		}
		request.setAssets(assets);

		return request;
	}

}
