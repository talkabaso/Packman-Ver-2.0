package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This function takes some information from the data base about our score relative to other players
 *  and write it into a csv file
 * @author aric and tal
 *
 */

public class saveStatistics {
	public static void main(String[] args)
	{
		exploitData();
	}

	public static String exploitData() {

		String jdbcUrl="jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		String jdbcUser="student";
		String jdbcPassword="student";
		String resultsToWrite="";
		guiGame demo =new guiGame();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);		

			Statement statement = connection.createStatement();			

			String allCustomersQuery = "SELECT * FROM logs WHERE FirstID="+205357619+" OR SecondID="+204613863;

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			System.out.println("FirstID\t\tSecondID\tThirdID\t\tLogTime\t\t\t\tPoint\t\tSomeDouble");

			//write titles 
			resultsToWrite += "FirstID,SecondID,ThirdID,LogTime,Point,configuration name(if zero its private)"+"\n";	
			while(resultSet.next())
			{
				//write content
				resultsToWrite+=resultSet.getInt("FirstID")+"," +resultSet.getInt("SecondID")+"," +
						resultSet.getInt("ThirdID")+"," +
						resultSet.getTimestamp("LogTime") +"," +
						resultSet.getDouble("Point") +"," +
						checkkonfigure(resultSet.getDouble("SomeDouble"))+",";

				resultsToWrite+="\n";								
			}
			demo.writeResults(resultsToWrite);

			//avg
			allCustomersQuery = "SELECT AVG(Point) FROM logs WHERE SomeDouble="+1577914705;
			resultSet = statement.executeQuery(allCustomersQuery);
			float avg=0;
			if(resultSet.next())
				avg = (float)(resultSet.getFloat(1));

			allCustomersQuery = "SELECT * FROM logs WHERE SomeDouble="+1577914705+" AND FirstID="+205357619;
			resultSet = statement.executeQuery(allCustomersQuery);
			double ourPoints=0;
			while(resultSet.next())
			{
				ourPoints+=resultSet.getDouble("Point");
			}

			resultsToWrite+="the avg point for example 5 is: "+avg+"\n";
			resultsToWrite+="our result for example 5 is: "+ourPoints+"\n";

			//max
			allCustomersQuery = "SELECT MAX(Point) FROM logs WHERE FirstID="+205357619;
			resultSet = statement.executeQuery(allCustomersQuery);
			float max=0;
			if(resultSet.next())
				max = (float)(resultSet.getFloat(1));
			resultsToWrite+="the max point we achieved: "+max+"\n"; 

			//min
			allCustomersQuery = "SELECT MIN(Point) FROM logs WHERE FirstID="+205357619;
			resultSet = statement.executeQuery(allCustomersQuery);
			float min=0;
			if(resultSet.next())
				min = (float)(resultSet.getFloat(1));
			resultsToWrite+="the min point we achieved: "+min+"\n"; 

		}catch(Exception e) {

		}

		return resultsToWrite;
	}

	private static double checkkonfigure(double nameFile) {

/*Configure numbers:
1. 2128259830
2. 1149748017
3. -683317070
4. 1193961129
5. 1577914705
6. -1315066918
7. -1377331871
8. 306711633
9. 919248096*/
		
		if (nameFile==2.12825983E9) {

			return 1;
		}
		if (nameFile==1.149748017E9) {

			return 2;
		}
		if (nameFile==-6.8331707E8) {

			return 3;
		}
		if (nameFile==1.193961129E9) {

			return 4;
		}
		if (nameFile==1.577914705E9) {

			return 5;
		}
		if (nameFile==-1.315066918E9) {

			return 6;
		}
		if (nameFile==-1.377331871E9) {

			return 7;
		}
		if (nameFile==3.06711633E8) {

			return 8;
		}
		if (nameFile==9.19248096E8) {

			return 9;
		}
		return 0;
	}
}