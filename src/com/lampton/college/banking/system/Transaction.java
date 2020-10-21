package com.lampton.college.banking.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * This class is used to call all the transactions like Deposit, Draw, Pay etc.
 * @author SrinathS
 */
public class Transaction {
	private String transactionType;
	private String accountType;
	private String name;
	private double amount;
	private long accountNum;
	private Date date;
	private boolean error;
	Scanner sc = new Scanner(System.in);

	public void transaction(String transactionType, String accountType, String name, long accountNum, double amount) {		
		this.transactionType = transactionType;
		this.accountType = accountType;
		this.name = name;
		this.accountNum = accountNum;
		this.amount = amount;
		date = new Date();
		operation();
	}

	/**
	 * This method invokes while customer selects operation such as Opening account, Pay and Transfer etc.
	 */
	private void operation() {
		if (transactionType.equalsIgnoreCase("Opening")) {
			try {
				// Create file
				FileWriter fstream = new FileWriter(accountType+".txt", true);
				BufferedWriter out = new BufferedWriter(fstream);
				int maxId = findMaxId();
				if(maxId > 0) {
					if(findUserName(accountType)){
						System.err.println("\nError: Sorry, User name is already exist!");
						out.close();
						return;
					}
				}
				int userId = maxId + 1;
				out.write(Integer.toString(userId) + "\n");
				out.write(name + "\n");
				out.write(amount + "\n");
				out.write(date + "\n");
				out.close();
				System.out.println("\nSuccess: Your " +accountType+" account is created successfully, please find the account number for future reference = \n"+userId);				
			} catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage());
			}
		} else if (transactionType.equalsIgnoreCase("withdraw") || transactionType.equalsIgnoreCase("deposit") 
				|| transactionType.equalsIgnoreCase("transfer") || transactionType.equalsIgnoreCase("pay") ) {
			transactionUpdate();
		}

		else if (transactionType.equalsIgnoreCase("showInfo")) {
			Path path = Paths.get(accountType+".txt");
			if (Files.exists(path)) {
				findDisplay();
			} else {
				System.err.println("Error:  File not Found");
			}
		}

		else {
			System.err.println("Error: Invalid option");
			return;
		}

	}

	/**
	 * @return int maximum id from the file
	 */
	private int findMaxId() {
		try (Stream<String> lines = Files.lines(Paths.get(accountType+".txt"))) {
			Long max = lines.count();
			return max.intValue()/4;
		} catch (Exception e) {
			System.err.format("Error: Please try again later!");			
		}		
		return 0;
	}

	
	/**
	 * This method shows the Customer information like Account Number, Name and Balance etc.
	 */
	private void findDisplay() {
		try {					
			boolean found = false;
			List<String> lines = Files.readAllLines(Paths.get(accountType+".txt"), Charset.defaultCharset());
		    if(lines!=null) {
			    for(int i = 0; i < lines.size(); i++){
					if(Long.toString(accountNum).equals(lines.get(i))
							&& name.equals(lines.get(i+1))) {
						found = true;
						System.out.println("Account Number = "+lines.get(i));
						System.out.println("User Name = "+lines.get(i+1));
						System.out.println("Available Balance = "+lines.get(i+2));
						System.out.println("Creation Date = "+lines.get(i+3));
						break;
						
					}
				}
			    if(!found)
					System.err.println("\nError : Account is not exist, try again!");	   
		    } else {
		    	System.err.println("\n Error : Account is not exist, try again!");	
		    }
			}catch(Exception  e){
				System.err.format("Error : Account is not exist, try again!");
			}
	}

	
	/**
	 * This method updates all the transactions based on account selection
	 */
	private void findUpdate() {
		try {
			boolean accountFound = false;
			List<String> lines = Files.readAllLines(Paths.get(accountType + ".txt"), Charset.defaultCharset());
			FileWriter fstream = new FileWriter("TempFile.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			int j = 0;
			String str = "";
			if (lines != null) {
				Double tempAmount = null;
				for (int i = 0; i < lines.size(); i++) {
					if (Long.toString(accountNum).equals(lines.get(i)) && name.equals(lines.get(i + 1))) {
						accountFound = true;
						out.write(lines.get(i) + "\n");
						if (transactionType.equalsIgnoreCase("withdraw") || transactionType.equalsIgnoreCase("transfer")
								|| transactionType.equalsIgnoreCase("pay")) {
							if (amount > Double.parseDouble(lines.get(i + 2))) {
								System.err.println("\nError: Insufficient funds, try again!\n");
								setError(true);
								break;
							}
							
							tempAmount = Double.parseDouble(lines.get(i + 2)) - amount;
							str = "deducted from";
						} else if (transactionType.equalsIgnoreCase("deposit")) {
							tempAmount = amount + Double.parseDouble(lines.get(i + 2));
							str = "updated in";
						}
						if (tempAmount < 0) {
							System.err.println("\nError: Insufficient funds, try again!\n");
							setError(true);
							break;
						}
						j = 1;
					} else if (j == 2) {
						out.write(tempAmount + "\n");
						j = 0;
					} else {
						if (j == 1)
							j = 2;
						out.write(lines.get(i) + "\n");
					}
				}
				
				if(error) {
					return;
				}
				
				if (!accountFound)
					System.err.println("\nError: Account number is not exist, try again!");
				else
					System.out.println("\nSucccess : Amount " + Double.toString(amount) + " is " + str + " "
							+ accountType + " account, the total available balance is " + tempAmount + "\n");
			} else {
				System.err.println("\n Error: Account number is not exist, try again!");
			}
			out.close();
			fstream.close();
			// Rename the temporary file and delete original file
			File f1 = new File(accountType + ".txt");
			f1.delete();
			File f2 = new File("TempFile.txt");
			boolean b = f2.renameTo(f1);
			if (b) {
			} else {
				System.err.println("Error: Account is not exist, try again!");
			}
		} catch (Exception e) {
			System.err.format("Error: Account is not exist, try again!");
		}
	}
	
	/**
	 * This method call findUpdate function
	 */
	private void transactionUpdate() {
		Path path = Paths.get(accountType+".txt");
		if (Files.exists(path)) {
			findUpdate();
		} else {
			System.err.println("Error: Account is not exist, try again!");
		}
	}
	
	/**
	 * This method checks whether Username is exists or not in the File while Registration.
	 * @param String file name
	 * @return boolean
	 */
	public boolean findUserName(String file) {
		try {
			List<String> lines = null;
			lines = Files.readAllLines(Paths.get(file + ".txt"), Charset.defaultCharset());
			if (lines != null) {
				for (int i = 0; i < lines.size(); i=i+2) {
					if (name.equals(lines.get(i).trim())) {
						return true;						
					}
				}
				
			} else {
				return false;	
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * : This method checks whether Username is exists or not in the File for particular account.
	 * @param String file name
	 * @return boolean
	 */	
	public boolean findUserNameForAccounts(String file) {
		try {
			List<String> lines = null;
			lines = Files.readAllLines(Paths.get(file + ".txt"), Charset.defaultCharset());
			if (lines != null) {
				for (int i = 0; i < lines.size(); i++) {
					if (name.equals(lines.get(i).trim())) {
						return true;						
					}
				}
				
			} else {
				return false;	
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * This method checks whether account number exists or not in the File while transactions.
	 * @param String file name and long Account Number
	 * @return boolean
	 */
	public boolean findAcctNumber(String file, long accountNo) {
		try {
			List<String> lines = null;
			lines = Files.readAllLines(Paths.get(file + ".txt"), Charset.defaultCharset());
			if (lines != null) {
				for (int i = 0; i < lines.size(); i++) {
					if (String.valueOf(accountNo).equals(lines.get(i)) && name.equals(lines.get(i+1))) {
						return true;						
					}
				}
				
			} else {
				return false;	
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(long accountNum) {
		this.accountNum = accountNum;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}	
}
