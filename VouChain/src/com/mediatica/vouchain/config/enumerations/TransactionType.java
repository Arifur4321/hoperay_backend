package com.mediatica.vouchain.config.enumerations;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public enum TransactionType {



    
	NEW_USER(1,"NWU"),
	NEW_VOUCHER_TYPE(2,"NWV"),
	COMPANY_BUY_VOUCHER(3,"ACV"),
	COMPANY_GIVE_VOUCHER_TO_EMPLOYEE(4,"ALL"),
	EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT(5,"SPS"),
	REDEEM_VOUCHER(6,"RED");


    private int value;
    private String description;
    private static final  Collection<TransactionType> list = new ArrayList<TransactionType>();

     static {
        list.addAll(Arrays.asList(TransactionType.values()));
    }

    private TransactionType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
   
    public static Collection<TransactionType> getList() {
        return list;
    }

    public static String getDescriptionByValue(int value){
               switch (value) {

               			case 1: return NEW_USER.getDescription();
                        case 2: return NEW_VOUCHER_TYPE.getDescription();
                        case 3: return COMPANY_BUY_VOUCHER.getDescription();
                        case 4: return COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription();
                        case 5: return EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription();
                        case 6: return REDEEM_VOUCHER.getDescription();
                        default: return "";
		}
    }

    public static TransactionType getEnumById(int value){
                switch (value) {
                		case 1: return NEW_USER;
                        case 2: return NEW_VOUCHER_TYPE;
                        case 3: return COMPANY_BUY_VOUCHER;
                        case 4: return COMPANY_GIVE_VOUCHER_TO_EMPLOYEE;
                        case 5: return EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT;
                        case 6: return REDEEM_VOUCHER;
                        default: return NEW_USER;
		}
    }

}


