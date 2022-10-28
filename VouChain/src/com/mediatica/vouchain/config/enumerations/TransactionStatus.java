package com.mediatica.vouchain.config.enumerations;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public enum TransactionStatus {



    
	PENDING(0,"Pending"),
	CONFIRMED(1,"Confirmed");


    private int value;
    private String description;
    private static final  Collection<TransactionStatus> list = new ArrayList<TransactionStatus>();

     static {
        list.addAll(Arrays.asList(TransactionStatus.values()));
    }

    private TransactionStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getValue() {
    	boolean flag = true;
    	if(value==0) {
    		flag=false;
    	}
        return flag;
    }

    public void setValue(int value) {
        this.value = value;
    }
   
    public static Collection<TransactionStatus> getList() {
        return list;
    }

    public static String getDescriptionByValue(int value){
               switch (value) {

               			case 0: return PENDING.getDescription();
                        case 1: return CONFIRMED.getDescription();
                        default: return "";
		}
    }

    public static TransactionStatus getEnumById(int value){
                switch (value) {
                		case 0: return PENDING;
                        case 1: return CONFIRMED;
                        default: return PENDING;
		}
    }

}



