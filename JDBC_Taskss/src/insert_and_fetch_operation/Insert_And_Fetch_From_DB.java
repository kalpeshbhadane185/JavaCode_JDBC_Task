package insert_and_fetch_operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Insert_And_Fetch_From_DB {
	Generate_Random_Values generate_Random_Values = new Generate_Random_Values();
	Random random = new Random();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		Insert_And_Fetch_From_DB insert_Records = new Insert_And_Fetch_From_DB();

		System.out.println("Press 1 to Insert Records In ProductTable ");
		System.out.println("Press 2 Insert Records In Customer Table");
		System.out.println("Press 3 To Show The All Records");
		System.out.println("Press 4 To Show The Records Records using join");
		System.out.println("Press 5 to EXIT");
		System.out.println("Enter Your Choice");
		int caseNo = scanner.nextInt();

		switch (caseNo) {
		case 1:
			insert_Records.insertRecordsInProductTable();
			break;
		case 2:
			insert_Records.insertRecordsInCustomerTable();
			break;
		case 3:
			insert_Records.fetchAllRecords();
			break;
		case 4:
			insert_Records.fetchRecordsUsingJoin_2();
			break;
		case 5:
			System.out.println("now you are exit from system");
			System.exit(0);
			break;
		default:
			System.out.println("You are Entering wrong number");
			break;
		}
	}

	public void insertRecordsInProductTable() {

		try {

			Connections.query = "insert into product(p_name,p_company,p_age,p_color, p_price, p_mfg_date)"
					+ " values(?,?,?,?,?,?)";

			Connections.createDBConnection();

			PreparedStatement preparedStatement = Connections.preparedStatement;

			System.out.println("Enter the num which u want to add");
			int entry = scanner.nextInt();

			int count = 0;

			for (int i = 1; i <= entry; i++) {

				String p_name = generate_Random_Values.randomProductName();

				String p_company = generate_Random_Values.randomProductCompany();

				int p_age = random.nextInt(20);

				String p_color = generate_Random_Values.randomColors();

				int p_price = random.nextInt(1000);

				Date randomDate = generate_Random_Values.generateRandomDate();

				java.sql.Date sqlDate = new java.sql.Date(randomDate.getTime());

				// set the values
				preparedStatement.setString(1, p_name);
				preparedStatement.setString(2, p_company);
				preparedStatement.setInt(3, p_age);
				preparedStatement.setString(4, p_color);
				preparedStatement.setInt(5, p_price);
				preparedStatement.setDate(6, sqlDate);

				// now we required the execute the query
				int result = preparedStatement.executeUpdate();
				if (result == 0) {
					System.out.println("Something Wrong Happened in Program");
				} else {
					count++;
				}
			}
			System.out.println(count + " product details are inserted !!!!!!!");

			Connections.close_Connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertRecordsInCustomerTable() {
		try {
			Connections.query = "insert into customer(c_name,c_email,c_city,p_id)" + " values(?,?,?,?)";

			Connections.createDBConnection();

			PreparedStatement preparedStatement = Connections.preparedStatement;

			int total_count_product = total_Count_Product();

			System.out.println("Enter the num which u want to add");
			int entry = scanner.nextInt();

			int count = 0;

			for (int i = 1; i <= entry; i++) {

				String c_name = generate_Random_Values.generated_Random_Cust_Name();

				String c_email = generate_Random_Values.gen_Random_Cust_Email();

				String c_city = generate_Random_Values.gen_Ran_Cust_City();

				int p_id = generate_Random_Values.gen_PID(total_count_product);

				// set the values
				preparedStatement.setString(1, c_name);
				preparedStatement.setString(2, c_email);
				preparedStatement.setString(3, c_city);
				preparedStatement.setInt(4, p_id);

				// now we required the execute the query
				int result = preparedStatement.executeUpdate();
				if (result == 0) {
					System.out.println("Something Wrong Happened in Program");
					break;
				} else {
					count++;
				}
			}
			System.out.println(count + " Customer details are inserted !!!!!!!");

			Connections.close_Connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int total_Count_Product() {
		try {
			Connections.query = "select count(*) from product";

			Connections.createDBConnection();

			PreparedStatement preparedStatement = Connections.preparedStatement;

			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.next();

			int total_records_count = resultSet.getInt(1);

			Connections.close_Connection();

			return total_records_count;
		} catch (Exception e) {
			return 0;
		}

	}

	public void fetchAllRecords() {
		try {
			Connections.query = "select * from product p join customer c on c.p_id = p.p_id";

			String queryy = Connections.query;

			Connection createDBConnection = Connections.createDBConnection();

			PreparedStatement prepareStatement = createDBConnection.prepareStatement(queryy);

			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {

				int int1 = resultSet.getInt(1);
				String string = resultSet.getString(2);
				String string2 = resultSet.getString(3);
				int int2 = resultSet.getInt(4);
				String string3 = resultSet.getString(5);
				int int3 = resultSet.getInt(6);
				String string4 = resultSet.getString(7);
				int int4 = resultSet.getInt(8);
				String string5 = resultSet.getString(9);
				String string7 = resultSet.getString(10);
				String string8 = resultSet.getString(11);
				int int5 = resultSet.getInt(12);

				System.out.println(int1 + " " + string + " " + string2 + " " + int2 + " " + string3 + " " + int3 + " "
						+ string4 + " ");
				System.out.println(int4 + " " + string5 + " " + " " + string7 + " " + string8 + " " + int5);
				System.out.println();
			}
			Connections.close_Connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fetchRecordsUsingJoin_2() {
		try {
			Connections.query = "select p.p_id as pid, p.p_name as pname, c.c_id as cid, c.c_name as firesultSett_name,\n"
					+ "c.p_id as pidIn_customer\n" + "from product p right join customer c on c.p_id = p.p_id \n"
					+ "where p_price > 500;";

			String queryy = Connections.query;

			Connection createDBConnection = Connections.createDBConnection();

			PreparedStatement prepareStatement = createDBConnection.prepareStatement(queryy);

			ResultSet resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				int int1 = resultSet.getInt("pid");
				String string = resultSet.getString("pname");
				String string2 = resultSet.getString("cid");
				String string3 = resultSet.getString("firesultSett_name");
				int int3 = resultSet.getInt("pidIn_customer");
				System.out.println(int1 + " " + string + " " + string2 + " " + string3 + " " + int3);
				System.out.println("*******---------*******-----------************-------------");
			}
			Connections.close_Connection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
