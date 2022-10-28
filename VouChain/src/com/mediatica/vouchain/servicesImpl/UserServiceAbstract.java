package com.mediatica.vouchain.servicesImpl;

import java.math.BigDecimal;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.PasswordDTO;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.PasswordNotCorrectException;
import com.mediatica.vouchain.servicesInterface.UserServicesInterface;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Service
@Transactional
public abstract class UserServiceAbstract implements UserServicesInterface {

	@Autowired
	UserDaoImpl userDao;

	@Autowired
	GeographicServiceImpl geographicServiceImpl;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserServiceAbstract.class);

	/**
	 * It verifies the login of the company
	 * 
	 * @param email
	 * @return
	 */
	@Override
	public DTO login(String email) {
		User usr = null;
		DTO dto = null;
		log.debug("Passed email is:{}", email);
		usr = userDao.findUserByEmail(email);
		if (usr != null) {
			if (usr.getCompany() != null) {
				dto = new CompanyDTO();
				dto = (CompanyDTO) dto.wrap(usr, true);
			} else if (usr.getEmployee() != null) {
				dto = new EmployeeDTO();
				dto = (EmployeeDTO) dto.wrap(usr, true);
			} else {
				dto = new MerchantDTO();
				dto = (MerchantDTO) dto.wrap(usr, true);

			}
		}
		return dto;
	}

	/**
	 * 
	 */
	@Override
	public DTO showProfile(String usrId) {
		User usr = new User();
		DTO dto = null;
		if (usrId != null && !usrId.isEmpty()) {
			usr = userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
		}
		if (usr != null) {
			if (usr.getCompany() != null) {
				dto = new CompanyDTO();
				dto = (CompanyDTO) dto.wrap(usr);
			} else if (usr.getEmployee() != null) {
				dto = new EmployeeDTO();
				dto = (EmployeeDTO) dto.wrap(usr);
			} else {
				dto = new MerchantDTO();
				dto = (MerchantDTO) dto.wrap(usr);
				
				LocalizationDTO localizationDTO = new LocalizationDTO();
				localizationDTO = geographicServiceImpl.getMapsLongLat(((MerchantDTO) dto).getMrcAddressOffice(),
						((MerchantDTO) dto).getMrcProvOffice(), ((MerchantDTO) dto).getMrcCityOffice());
				if(localizationDTO.getLongitude()!=null || localizationDTO.getLatitude()!=null) {
					((MerchantDTO) dto).setMrcLatitude(localizationDTO.getLatitude());
					((MerchantDTO) dto).setMrcLongitude(localizationDTO.getLongitude());
				}
			}
		}
		return dto;
	}

	/**
	 * 
	 * @throws PasswordNotCorrectException
	 */
	public boolean changePassword(PasswordDTO dto) throws PasswordNotCorrectException {
		User user = new User();
		user = userDao.findByPrimaryKey(user, Integer.parseInt(dto.getUserId()));
		if (user != null && BCrypt.checkpw(dto.getOldPsw(), user.getUsrPassword())
				&& checkType(user, dto.getUsrProfile())) {
			user.setUsrPassword(BCrypt.hashpw(dto.getNewPsw(), user.getUsrSalt()));
			userDao.update(user);
		} else {
			throw new PasswordNotCorrectException("password_not_correct");
		}
		return true;
	}

	private boolean checkType(User user, String usrProfile) {
		boolean flag = false;
		if (usrProfile.equalsIgnoreCase(Constants.ENTITIES_TYPE.COMPANY.name()) && user.getCompany() != null
				|| usrProfile.equalsIgnoreCase(Constants.ENTITIES_TYPE.EMPLOYEE.name()) && user.getEmployee() != null
				|| usrProfile.equalsIgnoreCase(Constants.ENTITIES_TYPE.MERCHANT.name()) && user.getMerchant() != null) {
			flag = true;
		}
		return flag;
	}

}
