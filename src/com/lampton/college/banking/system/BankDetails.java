package com.lampton.college.banking.system;

/**
 * This the main class to call all the functions in OpenAccount class
 * @author SrinathS
 */
public class BankDetails {	
	 public static void main(String[] args) {
		 //Calling OpenAccount class to invoke main functions
		 OpenAccount openAccount = new OpenAccount();
		 openAccount.openBankAccount();		
	}
}
