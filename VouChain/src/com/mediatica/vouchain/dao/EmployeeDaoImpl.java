package com.mediatica.vouchain.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.Tbcomuni;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class EmployeeDaoImpl extends DaoImpl<Employee>{

	@Override
	public List<Employee> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Employee> listEmployeesByCompanyId(int idCompany) {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("SELECT emp FROM Employee emp WHERE emp.cpyUsrId.usrId = :id ORDER BY emp.empLastName");
			query.setParameter("id", idCompany);
			List<Employee> result = null;
			try {
				result = (List<Employee>)query.getResultList();
			}catch (NoResultException nre){
				//Ignore this because as per your logic this is ok!
			}
			return result ;
	}

	public Employee findByInvitationCode(String invitationCode) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("Employee.findByEmpInvitationCode");
		query.setParameter("empInvitationCode", invitationCode);
		Employee emp = null;
		try {
			emp = (Employee)query.getSingleResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return emp;
	}

}
