package com.logilite.bean;

import java.sql.Timestamp;

public class Transaction_Activity
{

	private int transaction_id;
	private Timestamp transaction_date;
	private String transaction_type;
	private double accountBalance;
	private double ammount;
	private int user_id;
	
	public Transaction_Activity()
	{
	}
	
	public int getTransaction_id()
	{
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id)
	{
		this.transaction_id = transaction_id;
	}
	public Timestamp getTransaction_date()
	{
		return transaction_date;
	}
	public void setTransaction_date(Timestamp transaction_date)
	{
		this.transaction_date = transaction_date;
	}
	public String getTransaction_type()
	{
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type)
	{
		this.transaction_type = transaction_type;
	}
	public double getAccountBalance()
	{
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance)
	{
		this.accountBalance = accountBalance;
	}
	public double getAmmount()
	{
		return ammount;
	}
	public void setAmmount(double ammount)
	{
		this.ammount = ammount;
	}
	public int getUser_id()
	{
		return user_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	@Override
	public String toString()
	{
		return "Transaction_Activity [transaction_id=" + transaction_id + ", transaction_date=" + transaction_date
				+ ", transaction_type=" + transaction_type + ", accountBalance=" + accountBalance + ", ammount="
				+ ammount + ", user_id=" + user_id + "]";
	}
}
