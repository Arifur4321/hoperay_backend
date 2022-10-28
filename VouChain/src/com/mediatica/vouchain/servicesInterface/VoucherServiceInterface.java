package com.mediatica.vouchain.servicesInterface;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.VoucherAllocationDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.VoucherYetInTheSystemException;

public interface VoucherServiceInterface {

	public VoucherDTO addNewVoucherType(VoucherDTO voucherDTO) throws ParseException, VoucherYetInTheSystemException, IOException, Exception;

	public List<VoucherDTO> getPurchasableVouchersList();

    public List<VoucherDTO> purchaseVoucherList(List<VoucherDTO> voucherList, String usrId) throws ParseException, IOException, Exception;

    public List<VoucherDTO> getExpendableVouchersList(String employeeId) throws Exception;

	public void allocateVouchers(List<VoucherAllocationDTO> allocationList) throws Exception;

	public List<VoucherDTO> getActiveVoucherList(String companyId) throws Exception;

	public List<VoucherDTO> getExpendedVouchersList(String merchantId) throws Exception;

}
