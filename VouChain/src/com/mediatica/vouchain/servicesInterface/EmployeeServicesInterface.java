package com.mediatica.vouchain.servicesInterface;

import java.util.List;

import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public interface EmployeeServicesInterface extends UserServicesInterface {
	
	/**
	 * it sends a mail to every item contained in the input list
	 * 
	 * @param list
	 */
	public void sendMail(List<EmployeeDTO> list);
	
	/**
	 * 
	 * saves the list of passed employees
	 * 
	 * @param employeeList
	 * @return
	 * @throws EmailYetInTheSystemException
	 */
	public List<EmployeeDTO> insertEmployeesList(List<EmployeeDTO> employeeList) throws EmailYetInTheSystemException ;

	/**
	 * retrieves all the employees associated to the given companu
	 * 
	 * @param companyId
	 * @return
	 */
	public List<EmployeeDTO> getEmployeeList(String companyId);

	/**
	 * 
	 * check the invitation code and returns the associated employee
	 * 
	 * @param invitationCode
	 * @return
	 */
	public EmployeeDTO checkInvitationCode(String invitationCode);

	/**
	 * confirms the employ and send him an confirmation email
	 * 
	 * @param employeeDTO
	 * @return
	 * @throws Exception
	 */
	public EmployeeDTO confirm(EmployeeDTO employeeDTO) throws Exception;
	
}
