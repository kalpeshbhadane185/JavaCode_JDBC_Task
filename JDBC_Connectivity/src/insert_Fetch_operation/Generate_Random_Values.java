package insert_Fetch_operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Generate_Random_Values {

	public Random random = new Random();
	String alfabetCharacters = "abcdefghijklmnopqrstuvwxyz";

	// create a function each and every random generated values
	// 2.
	public String randomProductName() {
		List<String> pname = new ArrayList<>();
		pname.add("Wi-Fi");
		pname.add("Bluetooth");
		pname.add("Key_Board");
		pname.add("Mouse");
		pname.add("HeadPhones");
		pname.add("CPU");
		pname.add("Monitor");
		pname.add("USB-Cable");
		pname.add("ScreenGuard");
		pname.add("RAM");
		pname.add("GPU");
		pname.add("MotherBoard");
		pname.add("TouchPad");
		pname.add("Hard Disk");

		String random_pName = fun_For_Generating_Random_Values(pname);
		return (String) random_pName;
	}

	// 3.
	public String randomProductCompany() {
		List<String> pcompany = new ArrayList<>();
		pcompany.add("Skype");
		pcompany.add("Logitech");
		pcompany.add("Boat");
		pcompany.add("nanoboat");
		pcompany.add("Lenovo");
		pcompany.add("HP");
		pcompany.add("Acer");
		pcompany.add("Redmi");
		pcompany.add("Samsung");
		pcompany.add("Realme");
		pcompany.add("Nokia");
		pcompany.add("iball");
		pcompany.add("Asus");
		pcompany.add("Intel");
		pcompany.add("Toshiba");

		String random_pCompany = fun_For_Generating_Random_Values(pcompany);
		return random_pCompany;
	}

	public String randomColors() {
		String[] colorArray = { "black", "brown", "red", "orange", "yellow", "green", "blue", "violet", "skyblue",
				"golden", "maroon", "magneta", "teal", "silver", "raddish", "grey", "coral", "white" };
		List<String> colorList = Arrays.asList(colorArray);
		String randomColor = fun_For_Generating_Random_Values(colorList);
		return randomColor;
	}

	// 2.2
	public String generated_Random_Cust_Name() {
		int nameMinValue = 5;
		int nameMaxValue = 12;
		int range = (int) Math.floor(Math.random() * (nameMaxValue - nameMinValue + 1) + nameMinValue);
		StringBuilder s2 = new StringBuilder(range);
		for (int i = 0; i < range; i++) {
			int ch = (int) (alfabetCharacters.length() * Math.random());
			s2.append(alfabetCharacters.charAt(ch));
		}
		return s2.toString();
	}

	// 2.3 gen_Random_Cust_Email
	public String gen_Random_Cust_Email() {
		String emailAddress = "";
		while (emailAddress.length() < 5) {
			int character = (int) (Math.random() * 26);
			if (character >= 24) {
				character = random.nextInt(23);
				emailAddress += alfabetCharacters.substring(character, character + 3);
			} else {
				emailAddress += alfabetCharacters.substring(character, character + 3);
			}
			emailAddress += Integer.valueOf((int) (Math.random() * 99)).toString();
			emailAddress += "@" + "gmail.com";
		}
		return emailAddress;
	}

	// 2.4 customer city
	public String gen_Ran_Cust_City() {
		String cityNameAndNum = "";
		List<String> clist = new ArrayList<>();
		clist.add("Nashik");
		clist.add("Pune");
		clist.add("Dhule");
		clist.add("Mumbai");
		clist.add("Ahmedabad");
		clist.add("Surat");
		clist.add("Jalgaon");
		clist.add("Anand");
		clist.add("Nandurbar");
		clist.add("Kolhapur");
		clist.add("Aurangabad");
		clist.add("Thane");

		String random_City_Names = fun_For_Generating_Random_Values(clist);
		int rnum = random.nextInt(100);
		cityNameAndNum = random_City_Names + rnum;
		return cityNameAndNum;
	}

	// 2.5 Product_id
	public int gen_PID(int total_count_pro) {
		int first_Product = 1;
		int total_ProdcutInDB = total_count_pro;
		int random_Value_Of_PID = (int) Math
				.floor(Math.random() * (total_ProdcutInDB - first_Product + 1) + first_Product);
		return random_Value_Of_PID;
	}

	public Date generateRandomDate() {
		Calendar calendar = new GregorianCalendar(2015, Calendar.JANUARY, 1);
		Date startDate = calendar.getTime();

		calendar.add(Calendar.YEAR, 10);
		Date endDate = calendar.getTime();

		long startMillis = startDate.getTime();
		long endMillis = endDate.getTime();

		long randomMillisSinceEpoch = startMillis + (long) (Math.random() * (endMillis - startMillis));

		return new Date(randomMillisSinceEpoch);
	}

	String fun_For_Generating_Random_Values(List<String> l) {
		int randomitemNum = random.nextInt(l.size());
		String randomElement = (String) l.get(randomitemNum);
		return randomElement;
	}

}
