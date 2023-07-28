package com.logilite.stringconst;

public class SQLQueries
{
	public static final String	INSERT_TRANSACTION_ACTIVITY			= "INSERT INTO tr_activity (tr_Date, tr_type, account_balance, amount, user_id) VALUES (?, ?, ?, ?, ?);";
	public static final String	SELECT_ACCOUNT_BALANCE_BY_USER_ID	= "SELECT account_balance FROM bank_user b JOIN tr_activity t ON t.user_id = b.user_id WHERE t.user_id = ? ORDER BY tr_date DESC;";
	public static final String	DELETE_USER_BY_USER_ID				= "DELETE FROM bank_user WHERE user_id = ?;";

	public static final String	TR_ACTIVITY_BY_USER_ID				= "SELECT * FROM tr_activity WHERE user_id = ? ORDER BY tr_date DESC;";
	public static final String	TR_ACTIVITY_BY_MONTH				= "SELECT * FROM tr_activity WHERE user_id = ? AND DATE_TRUNC('month', tr_date) = DATE_TRUNC('month', CURRENT_DATE) ORDER BY tr_date DESC;";
	public static final String	TR_ACTIVITY_BY_QUARTER				= "SELECT * FROM tr_activity WHERE user_id = ? AND tr_date >= DATE_TRUNC('quarter', CURRENT_DATE) AND tr_date < DATE_TRUNC('quarter', CURRENT_DATE) + INTERVAL '3 months' ORDER BY tr_date DESC;";

	public static final String	TR_ACTIVITY_BY_DATE_RANGE			= "SELECT * FROM tr_activity WHERE user_id = ? AND tr_date BETWEEN ? AND ? ORDER BY tr_date DESC;";

	public static final String INSERT_USER_DETAILS = "INSERT INTO bank_user (username, password, user_type, account_no, email, mobile_no, gender, parent_id) VALUES(?,?,?,?,?,?,?,?);";
	
	public static final String USER_AUTHENTICATE ="select * from bank_user where username = ? AND password = ?";
	
	public static final String	USER_ACCOUNT_BALANCE = "select * from bank_user b join tr_activity t on t.user_id = b.user_id where t.user_id = ? order by  tr_date desc";
}
