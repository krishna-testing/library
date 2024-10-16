package org.clx.library.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CDOE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already account created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account successfully created!";

    public static String generateAccountNumber (){
        Year curentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int randomNumber = (int)Math.floor(Math.random() * (max - min + 1)+ min);

        String year = String.valueOf(curentYear);
        String randNumber = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }
}
