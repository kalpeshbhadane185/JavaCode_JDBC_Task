package com.logilite.bean;

import java.sql.Date;
import java.util.List;

public class Transaction_Activity
{

	public int transaction_id;
	public Date transaction_date;
	public String transaction_type;
	public double accountBalance;
	public double ammount;
	public int user_id;
	
	public int getTransaction_id()
	{
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id)
	{
		this.transaction_id = transaction_id;
	}
	public Date getTransaction_date()
	{
		return transaction_date;
	}
	public void setTransaction_date(Date transaction_date)
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
}
