package com.logilite.bean;

public class User
{
	@Override
	public String toString()
	{
		return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", user_type="
				+ user_type + ", account_no=" + account_no + ", email=" + email + ", mob_no=" + mob_no + ", gender="
				+ gender + ", parent_id=" + parent_id + "]";
	}
	public int user_id;
	public String username;
	public String password;
	public String user_type;
	public Long account_no;
	public String email;
	public Long mob_no;
	public String gender;
	public int parent_id;
	
	public int getUser_id()
	{
		return user_id;
	}
	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUser_type()
	{
		return user_type;
	}
	public void setUser_type(String user_type)
	{
		this.user_type = user_type;
	}
	public Long getAccount_no()
	{
		return account_no;
	}
	public void setAccount_no(Long account_no)
	{
		this.account_no = account_no;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public Long getMob_no()
	{
		return mob_no;
	}
	public void setMob_no(Long mob_no)
	{
		this.mob_no = mob_no;
	}
	public String getGender()
	{
		return gender;
	}
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	public int getParent_id()
	{
		return parent_id;
	}
	public void setParent_id(int parent_id)
	{
		this.parent_id = parent_id;
	}
}
