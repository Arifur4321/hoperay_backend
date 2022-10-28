package com.mediatica.vouchain.dto;


import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.User;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class EmployeeDTO extends DTO{

	private String empFirstName;
    private String empLastName;
    private String empMatricola;
    private String empInvitationCode;
    private String empCheckedLogin;
    private String cpyUsrId;
    private String empAccessType;
    private String empPinCode;
    private String empNotificationEnabled;
	
	
	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public String getEmpMatricola() {
		return empMatricola;
	}

	public void setEmpMatricola(String empMatricola) {
		this.empMatricola = empMatricola;
	}

	public String getEmpInvitationCode() {
		return empInvitationCode;
	}

	public void setEmpInvitationCode(String empInvitationCode) {
		this.empInvitationCode = empInvitationCode;
	}

	public String getEmpCheckedLogin() {
		return empCheckedLogin;
	}

	public void setEmpCheckedLogin(String empCheckedLogin) {
		this.empCheckedLogin = empCheckedLogin;
	}
	
	public String getAccessType() {
		return empAccessType;
	}

	public void setAccessType(String accessType) {
		this.empAccessType = accessType;
	}

	public String getPinCode() {
		return empPinCode;
	}

	public void setPinCode(String pinCode) {
		this.empPinCode = pinCode;
	}

	public String getEmpAccessType() {
		return empAccessType;
	}

	public void setEmpAccessType(String empAccessType) {
		this.empAccessType = empAccessType;
	}

	public String getEmpPinCode() {
		return empPinCode;
	}

	public void setEmpPinCode(String empPinCode) {
		this.empPinCode = empPinCode;
	}

	public String getEmpNotificationEnabled() {
		return empNotificationEnabled;
	}

	public void setEmpNotificationEnabled(String empNotificationEnabled) {
		this.empNotificationEnabled = empNotificationEnabled;
	}

	@Override
	public Object wrap(Object usr, boolean includePassword) {
		User user = (User)usr;
		EmployeeDTO employee = new EmployeeDTO();
	    //user fields
		employee.usrId=user.getUsrId()!=null?user.getUsrId().toString():"";
		employee.usrBchAddress=user.getUsrBchAddress();
		employee.usrPrivateKey=user.getUsrPrivateKey();
		employee.usrEmail=user.getUsrEmail();
		if(includePassword) {
			employee.usrPassword=user.getUsrPassword(); 
		}
		if(user.getUsrAccessType()!=null){
			employee.empAccessType=user.getUsrAccessType();
			if(user.getUsrAccessType().equals("PIN") && user.getUsrPin()!=null) {
				employee.empPinCode=user.getUsrPin().toString();
			}
		}
		// fields
		employee.empFirstName=user.getEmployee().getEmpFirstName();
		employee.empLastName=user.getEmployee().getEmpLastName();
		employee.empMatricola=user.getEmployee().getEmpMatricola();
		employee.empInvitationCode=user.getEmployee().getEmpInvitationCode();
		employee.cpyUsrId=(""+user.getEmployee().getCpyUsrId().getUsrId());
		employee.empCheckedLogin="false";
		employee.empNotificationEnabled=user.getUsrNotificationEnable();
	    if(user.getEmployee().getEmpCheckedLogin()!=null && user.getEmployee().getEmpCheckedLogin()==true) {
	    	employee.empCheckedLogin="true";
	    }
		return employee;
	}


	@Override
	public Object unwrap(Object employeeDTO) {
		EmployeeDTO employee = (EmployeeDTO)employeeDTO;
		
		Employee emp = new Employee();
		User usr = new User();
		
		boolean loginChecked=false;
		if(employee.getEmpCheckedLogin()!=null && employee.getEmpCheckedLogin().equalsIgnoreCase("true")) {
			loginChecked=true;
		}
		emp.setEmpCheckedLogin(loginChecked);

		emp.setEmpFirstName(employee.getEmpFirstName());
		emp.setEmpInvitationCode(employee.getEmpInvitationCode());
		emp.setEmpLastName(employee.getEmpLastName());
		emp.setEmpMatricola(employee.getEmpMatricola());

		boolean isActive=false;
		if(employee.getUsrActive()!= null && employee.getUsrActive().equals("true")) {
			isActive=true;
		}
		usr.setUsrActive(isActive);
		usr.setUsrEmail(employee.getUsrEmail());
		usr.setUsrPassword(employee.getUsrPassword());
		usr.setUsrNotificationEnable(employee.getEmpNotificationEnabled());
		Company cmp = new Company();
		if(employee.getCpyUsrId()!=null) {
			cmp.setUsrId(Integer.parseInt(employee.getCpyUsrId()));
		}
		emp.setCpyUsrId(cmp);
		usr.setEmployee(emp);
		
		return usr;
	}

	@Override
	public String toString() {
		return "EmployeeDTO [empFirstName=" + empFirstName + ", empLastName=" + empLastName + ", empMatricola="
				+ empMatricola + ", empInvitationCode=" + empInvitationCode + ", empCheckedLogin=" + empCheckedLogin
				+ ", usrId=" + usrId + ", usrEmail=" + usrEmail + ", usrActive=" + usrActive + "]";
	}

	public String getCpyUsrId() {
		return cpyUsrId;
	}

	public void setCpyUsrId(String cpyUsrId) {
		this.cpyUsrId = cpyUsrId;
	}



	
	
	
}
