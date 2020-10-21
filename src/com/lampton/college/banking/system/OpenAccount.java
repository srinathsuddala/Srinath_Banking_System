package com.lampton.college.banking.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * This the class to call all the main functions such as Register, SignIn etc.
 * Moreover, calls Transaction class to do different transactions like Deposit, Draw etc.
 * @author SrinathS
 */
public class OpenAccount {

	int count = 0;
	String name = "";
	public static final String INVALID_OPTION = "Invalid Option";
	public static final String ACCOUNT_NUMBER = "Enter Account Number";
	public static final String USER_NAME_EXIST = "\nError: Sorry, Username is already exist!";
	public static final String YOUR_CHOICE = "Your choice: ";
	public static final String INVALID_CREDENTIALS = "\nError:  Sorry, Invalid credentials!\n";
	public static final String USERNAME = "\nEnter Username:";
	public static final String PASSWORD = "\nEnter Password:";
	public static final String INVALID_USERNAME = "\nError: Sorry, Username is invalid!\n";
	String accountType = "";
	double amount = 0;
	
	
	/**
	 * Starting method to show pop-up message like Start and Exit.
	 * @return
	 */
	@SuppressWarnings("resource")
	public void openBankAccount() {		
		Scanner input = new Scanner(System.in);
		String inputValue = "";
		
		try {
			while (!inputValue.equalsIgnoreCase("N")) {			
				System.out.println("============================================");
				System.out.println("Hi, Welcome To Lampton Bank Managment System");
				if (count < 1) {
					count = count + 1;
				} else {
					System.out.print("Again \n");
				}
				System.out.println("============================================");
	
				System.out.print("Question: What do you want to do?\n\n");
				if (count < 2) {
					System.out.print("1 : Start\n\n");
					count = count + 1;
				} else {
					System.out.print("1 : Start again\n\n");
				}
				System.out.print("2 : Exit\n\n");
				System.out.println("Your selection : ");
				int switchChoice = input.nextInt();
				switch (switchChoice) {
	
				case 1: {
					start();
					break;
				}
				case 2: {
					return;
				}
				default: {
					System.out.println(INVALID_OPTION);
					error();
					break;
				}
				}
				System.out.println("\nDo you want to run your program again? \nY = Yes\nN = No\n");
				System.out.println(YOUR_CHOICE);
	
				inputValue = (input.next());// End If			
			} // End While
		} catch (Exception ex) {
			error();
		}
	}
	
	
	/**
	 * It will show the pop-up message regarding Sign In, Register, Forgot Password and Exit
	 * @return
	 */
	private void start() {
		String choice;
		Transaction transaction = new Transaction();
		Scanner sc = new Scanner(System.in);
		String password = "";
		BufferedWriter bufferedWriter = null;
		FileWriter fstream = null;

		do {

			System.out.println("\nQ: What do you want to do next?\n");
			System.out.print("1 : Sign In\n\n");
			System.out.print("2 : Register\n\n");
			System.out.print("3 : Forgot Password\n\n");
			System.out.print("4 : Exit\n\n");

			System.out.println(YOUR_CHOICE);
			choice = sc.next();
			switch (choice) {
			case "1":	
				sc = new Scanner(System.in);
				System.out.println(USERNAME);									
				name = sc.nextLine();
				sc = new Scanner(System.in);
				System.out.println(PASSWORD);									
				password = sc.nextLine();
				transaction.setName(name);
				
				try {
					boolean found = false;
					List<String> lines = Files.readAllLines(Paths.get("Credentials.txt"), Charset.defaultCharset());
					    if(lines!=null) {
						    for(int i = 0; i < lines.size(); i++){
								if(name.equals(lines.get(i)) && i < lines.size() - 1  && password.equals(lines.get(i+1))) {
									found = true;
									System.out.println("\nSuccess Message: Logged In!");
									transaction.setName(name);
									transactionPopup();									
									break;
								}
							}
						    if(!found)
						    	System.err.format(INVALID_CREDENTIALS);	   
					    } else {
					    	System.err.format(INVALID_CREDENTIALS);	
					    }
					}	catch(Exception  e){
							System.err.format("\nError: Invalid!\n");							
					}			
				break;
			case "2":
				try{					
					sc = new Scanner(System.in);	
					System.out.println(USERNAME);									
					name = sc.nextLine();
					if(name.length() < 5) {
						System.err.println("\nUsername must be minimum 5 characters\n");
						while(name.length() < 5) {
							sc = new Scanner(System.in);	
							System.out.println(USERNAME);									
							name = sc.nextLine();
						}
					}
					
					transaction.setName(name);
					if(transaction.findUserName("Credentials")){
						System.err.println(USER_NAME_EXIST);
						break;
					}
					sc = new Scanner(System.in);
					System.out.println(PASSWORD);									
					password = sc.nextLine();
					
					if(password.length() < 5) {
						System.err.println("\nPassword must be minimum 5 characters");
						while(password.length() < 5) {
							sc = new Scanner(System.in);	
							System.out.println(PASSWORD);									
							password = sc.nextLine();
						}
					}
					
					fstream = new FileWriter("Credentials.txt", true);
					bufferedWriter = new BufferedWriter(fstream);
					bufferedWriter.write(name+"\n");
					bufferedWriter.write(password+"\n");
					bufferedWriter.close();
					System.out.println("\nSuccess Message: Registered successfully!");
				} catch (Exception ex) {
					System.err.println("Error: Something went wrong, please try again!");
				}
				break;
			case "3":
				sc = new Scanner(System.in);
				System.out.println(USERNAME);									
				name = sc.nextLine();
				transaction.setName(name);
				try {
					boolean found = false;
					int i = 0;
					BufferedReader reader = new BufferedReader(new FileReader("Credentials.txt"));
					String line;
					fstream = new FileWriter("TempFile.txt", true);
					BufferedWriter out = new BufferedWriter(fstream);
					
					List<String> lines = null;
					lines = Files.readAllLines(Paths.get("Credentials.txt"), Charset.defaultCharset());
					if (lines != null) {
						for (int j = 0; j < lines.size(); j++) {
							if (i == 0 && !found && name.equals(lines.get(j))) {
								if(transaction.findUserName("Credentials")) {									
									found = true;
									i++;
								}
								out.write(lines.get(j)+"\n");
							} 	else if (found && i == 1){
								sc = new Scanner(System.in);
								System.out.println("\nEnter new password:");							
								password = sc.nextLine();
								if(password.length() < 5) {
									System.err.println("\nPassword must be greater than 4");
									while(password.length() < 5) {
										sc = new Scanner(System.in);	
										System.out.println("Enter new password:");									
										password = sc.nextLine();
									}
								}
								
								System.out.println("\nSuccess : Password recovered!");	
								out.write(password+"\n");
								i++;
							}	else {
								out.write(lines.get(j)+"\n");
							}
							
							
						}
						
					} else {
						System.err.println(INVALID_USERNAME);
						File f2 = new File("TempFile.txt");
						f2.delete();
						break;
					}
					out.close();
					reader.close();
				    if(!found) {
						System.err.println(INVALID_USERNAME);
						File f2 = new File("TempFile.txt");
						f2.delete();
						break;
				    }
				  
					}	catch(Exception  e){
							File f2 = new File("TempFile.txt");
							f2.delete();
							System.err.format(INVALID_USERNAME);
							break;
					}
				
				// Rename the temporary file and delete original file
				File f1 = new File("Credentials.txt");
				f1.delete();			
				File f2 = new File("TempFile.txt");
				boolean b = f2.renameTo(f1);
				if (b) {
				} else {
					System.err.println("Error: Sorry, Username is invalid!");
				}
				break;
			case "4":
				System.out.println("Thank you!");
				return;	
			default:
				error();
			}
		} while (choice != "q");
		sc.close();
	}
	
	/**
	 * This method shows the pop-up message for different functionalities such as Open accounts, doing different
	 * transactions and View Account information etc.
	 */
	private void transactionPopup() {
		String choice;
		String ch;
		String operation;
		Transaction transaction = new Transaction();
		Scanner sc = new Scanner(System.in);		
		long accountNo = 0;
		transaction.setName(name);

		do {

			System.out.println("\nQ: What do you want to do next?\n");
			System.out.print("1 : Open new accounts\n\n");
			System.out.print("2 : Transactions\n\n");
			System.out.print("3 : View Account Information\n\n");
			System.out.print("4 : Forgot Account Number\n\n");
			System.out.print("5 : Exit\n\n");

			System.out.println(YOUR_CHOICE);
			choice = sc.next();
			switch (choice) {
			case "1":
				accountType = checkAccount("availabe ");
				if(accountType.equalsIgnoreCase(INVALID_OPTION)) {
					System.out.println(INVALID_OPTION);
					return;
				} else if(accountType.equalsIgnoreCase("")) {
					break;
				} else {
					double openingBalance;
					sc = new Scanner(System.in);
					System.out.println("Enter the opening balance :");
					openingBalance = sc.nextDouble();				
					transaction.transaction("Opening", accountType, name, accountNo, openingBalance);
					accountNo = accountNo + 1;					
				}
				break;
			case "2":
				System.out.print("\nQ: What do you want to do for Transaction?\n\n");
				System.out.print("a : Deposit money\n\n");
				System.out.print("b : Draw money\n\n");
				System.out.print("c : Transfer money to other accounts within the bank\n\n");
				System.out.print("d : Pay utility bills\n\n");
				System.out.println("Your choice:\n");
				ch = sc.next();
				if (ch.equalsIgnoreCase("a"))
					operation = "Deposit";
				else if (ch.equalsIgnoreCase("b"))
					operation = "Withdraw";
				else if (ch.equalsIgnoreCase("c"))
					operation = "Transfer";
				else if (ch.equalsIgnoreCase("d"))
					operation = "Pay";
				else {
					error();
					break;
				}
				
				if(!operation.equalsIgnoreCase("Transfer")){
					if (ch.equalsIgnoreCase("a")){
						accountType =  openAccount("");
					} else {
						accountType =  openAccount(" from ");
					}
					
					if(accountType.equalsIgnoreCase(INVALID_OPTION)){
						error();
						break;
					}
					System.out.println(ACCOUNT_NUMBER);
					accountNo = sc.nextLong();
					System.out.println("Amount:");
					amount = sc.nextDouble();
					transaction.transaction(operation, accountType, name, accountNo, amount);
				}
				
				//
				if(operation.equalsIgnoreCase("Transfer")){
					String fromAcctType =  openAccount("from ");
					
					if(fromAcctType.equalsIgnoreCase(INVALID_OPTION)){
						error();
						break;
					}
					
					System.out.println(ACCOUNT_NUMBER);
					accountNo = sc.nextLong();
					if(!transaction.findAcctNumber(fromAcctType, accountNo)) {
						System.err.println("\nError: Account number is not found, try again!");
						break;
					}
					System.out.println("Amount:");
					amount = sc.nextDouble();				
										
					accountType =  openAccount("to ");
					if(fromAcctType.equalsIgnoreCase(accountType)) {
						while(fromAcctType.equalsIgnoreCase(accountType)) {
							System.err.println("\nError: Please select different account to transfer!");
							accountType =  openAccount("to ");
						}
					}	
					
					
					System.out.println(ACCOUNT_NUMBER);
					long toAccountNo = sc.nextLong();
					
					if(!transaction.findAcctNumber(accountType, toAccountNo)) {
						System.err.println("\nError: Account number is not found, try again!");
						break;
					} else {					
						transaction.transaction(operation, fromAcctType, name, accountNo, amount);
						
						if(!transaction.isError()) {																						
							transaction.transaction("deposit", accountType, name, toAccountNo, amount);
						}
					}
				}				
				break;
			case "3":
				accountType =  openAccount("");
				System.out.println(ACCOUNT_NUMBER);
				accountNo = sc.nextLong();
				System.out.println("");
				operation = "showInfo";
				transaction.transaction(operation, accountType, name, accountNo, 0);
				break;
			case "4":
				String acctType = openAccount("");
				sc = new Scanner(System.in);	
				System.out.println("\nEnter Username :");									
				String enteredName = sc.nextLine();
				if(!enteredName.equals(name)) {
					System.err.println("\nError: Sorry, User name is not valid!");
					break;
				}
				
				boolean found = false;
				try {					
					List<String> lines = null;					
				    lines = Files.readAllLines(Paths.get(acctType+".txt"), Charset.defaultCharset());
				    if(lines!=null) {
					    for(int i = 0; i < lines.size(); i++){
							if(name.equalsIgnoreCase(lines.get(i))) {
								found = true;
								System.out.println("\nSuccess: Please find your account number for "+acctType+" account = "+lines.get(i-1));							
							}
						}
					    if(!found)
							System.err.println("\nError: Sorry, User name is not exist for this account!");	   
				    } else {
				    	System.err.println("\nError: Sorry, User name is not exist for this account!");	
				    }
					}catch(Exception  e){
						System.err.format("\nError: Sorry, User name is not exist for this account!");
					}
							
				break;
			case "5":
				System.out.println("Thank you!");
				return;	
			default:
				error();			
			}
		} while (choice != "q");
		sc.close();
	}

	private void error() {
		System.err.print("\nError: You have selected some thing wrong\n");
	}
	
	
	/**
	 * This method checks what are the available accounts for the user
	 * @param String type
	 * @return String
	 */
	private String checkAccount(String type) {
		Transaction transaction = new Transaction();
		transaction.setName(name.trim());
		boolean userFound = false;
		Scanner sc = new Scanner(System.in);
		System.out.print("\nPlease select the below "+type+"account(s) \n\n");
		if(!transaction.findUserNameForAccounts("Chequing")) {
			userFound = true;
			System.out.print("a : Chequing account\n\n");
		}
		if(!transaction.findUserNameForAccounts("Saving")) {
			userFound = true;
			System.out.print("b : Saving account\n\n");
		}
		if(!transaction.findUserNameForAccounts("Investment")) {
			userFound = true;
			System.out.print("c : Investment account\n\n");
		}
		if(!transaction.findUserNameForAccounts("Credit")) {
			userFound = true;
			System.out.print("d : Credit card accout\n\n");
		}
		
		String accountType = "";
		if(userFound) {		
			System.out.println(YOUR_CHOICE);
			String ch = sc.next();
			if (ch.equalsIgnoreCase("a"))
				accountType = "Chequing";
			else if (ch.equalsIgnoreCase("b"))
				accountType = "Saving";
			else if (ch.equalsIgnoreCase("c"))
				accountType = "Investment";
			else if (ch.equalsIgnoreCase("d"))
				accountType = "Credit";
			else {
				accountType = INVALID_OPTION;
			}
		} else {
			System.out.println("Success: Oh great, you have already enrolled all the accounts: ");			
		}
		
		return accountType;
	}
	
	/**
	 * This method shows what are the available accounts in the bank
	 * @param String type
	 * @return String
	 */
	private String openAccount(String type) {
		Transaction transac = new Transaction();
		transac.setName(name);
		Scanner sc = new Scanner(System.in);
		System.out.print("\nPlease select the below "+type+"account \n\n");
		System.out.print("a : Chequing account\n\n");
		System.out.print("b : Saving account\n\n");
		System.out.print("c : Investment account\n\n");
		System.out.print("d : Credit card accout\n\n");
		
		String accountType = "";
			
		System.out.println(YOUR_CHOICE);
		String ch = sc.next();
		if (ch.equalsIgnoreCase("a"))
			accountType = "Chequing";
		else if (ch.equalsIgnoreCase("b"))
			accountType = "Saving";
		else if (ch.equalsIgnoreCase("c"))
			accountType = "Investment";
		else if (ch.equalsIgnoreCase("d"))
			accountType = "Credit";
		else 
			accountType = INVALID_OPTION;
		return accountType;
	}
}
