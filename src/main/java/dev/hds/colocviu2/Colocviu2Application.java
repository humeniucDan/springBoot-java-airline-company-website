package dev.hds.colocviu2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class Colocviu2Application {

	//                       jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
	static String connURL = "jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;";
	public static Connection conn = null;

	public static void main(String[] args) throws SQLException {
		getDBConn(connURL);
		SpringApplication.run(Colocviu2Application.class, args);
	}

	public static void getDBConn(String connURL) throws SQLException {
		DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

		conn = DriverManager.getConnection(connURL);
	}

	@Component
	public static class ServerPortCustomizer
			implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

		@Override
		public void customize(ConfigurableWebServerFactory factory) {
			factory.setPort(8000);
		}
	}

	@Component
	public static class CorsFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
										final FilterChain filterChain) throws ServletException, IOException {
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
			response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
			response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addIntHeader("Access-Control-Max-Age", 10);
			filterChain.doFilter(request, response);
		}
	}

	@GetMapping("/avioane")
	public String getAvioaneJSON() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null, ret2 = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select idav, av.numeav as numeav, gama_croaziera, numtr_piloti\n" +
								"from aeronave av join\n" +
								"\t(SELECT a.numeav, COUNT(c.idan) AS numtr_piloti\n" +
								"\tFROM Aeronave a\n" +
								"\tLEFT JOIN Certificare c ON a.idav = c.idav\n" +
								"\tLEFT JOIN Angajati an ON c.idan = an.idan\n" +
								"\tGROUP BY a.numeav) av2\n" +
								"\ton av.numeav = av2.numeav;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
//					Statement stmnt2 = conn.createStatement();
//					String inter2 =
//							"SELECT a.numeav, COUNT(c.idan) AS numtr_piloti\n" +
//									"FROM Aeronave a\n" +
//									"LEFT JOIN Certificare c ON a.idav = c.idav\n" +
//									"LEFT JOIN Angajati an ON c.idan = an.idan\n" +
//									"WHERE a.numeav LIKE '"+ret.getString("numeav")+"'\n" +
//									"GROUP BY a.numeav;";
//					ret2 = stmnt.executeQuery(inter2);
//					ret2.next();

					retStr += "{\"id\":\"" + ret.getString("idav") + "\", \"numeav\":\"" + ret.getString("numeav") + "\", \"gama\":\"" + ret.getString("gama_croaziera") +
							"\", \"nr_pil\":\"" + ret.getString("numtr_piloti") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}


	@GetMapping("/angajati")
	public String getAngajatiJSON() throws SQLException {
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select * from \n" +
								"angajati;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					System.out.println(ret.getString("numean") + " " + ret.getString("functie"));
					//retStr += ret.getString("numean") + " " + ret.getString("numeav") + "<br>";
					retStr += "{\"id\":\""+ ret.getString("idan") + "\", \"numean\":\"" + ret.getString("numean") + "\", \"functie\":\"" + ret.getString("functie") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@PostMapping(value = "/zboruri", consumes = "application/json", produces = "application/json")
	public String postZbor(@RequestBody Ruta ruta, HttpServletResponse response) throws SQLException {

		Zbor zbor = ruta.zboruri.get(0);
		int nrFlights = ruta.nrZboruri;
		ArrayList<Ruta> rute = new ArrayList<Ruta>();

		// print lines for debugging
		//System.out.println(zbor+ ", " + zbor.timpPlecare.ziuaSapt + ", " + nrFlights);

		for(int n = 1; n <= nrFlights; n++) {

			String inter = "select * from Zboruri z1 ";
//							"join Zboruri z2 on z1.la = z2.de_la and z1.sosire < z2.plecare and z1.zi = z2.zi " +
			for(int i = 2; i <= n; i++){
				inter += "join Zboruri z" + i + " on " +
						"z" + (i-1) + ".la = z"+ i +".de_la and " +
						"z" + (i-1) +".zi = z"+ i +".zi and " +
						"z" + (i-1) +".sosire < z"+ i +".plecare ";
			}

			inter += "where z1.de_la like '" + zbor.from + "' and z" + n + ".la like '" + zbor.to + "' ORDER BY z1.plecare ASC;";
			// actualy getting the response from the DB after interogation
			ResultSet ret = conn.createStatement().executeQuery(inter);

			//putting the data in 2 lists based on the time
			while(ret.next()){
				rute.add(Ruta.makeRutaFromResult(ret, n, ruta.zboruri.get(0).timpPlecare));
			}
		}

		System.out.println("Current String\n");
		String curString = "[";
		for(Ruta r: rute) {
			curString += r + ",";
		}

		curString = curString.substring(0, curString.length()-1) + "]";

		System.out.println(curString);

		return curString;

	}

	@PostMapping(value = "/avioanePilot", produces = "application/json")
	public String postAvion(@RequestBody int id, HttpServletResponse response) throws SQLException {

		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select * from Certificare c join Aeronave a on a.idav = c.idav where idan = " +
								id +
								";";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					//System.out.println(ret.getString("numean") + " " + ret.getString("functie"));
					//retStr += ret.getString("numean") + " " + ret.getString("numeav") + "<br>";
					retStr += "{\"id\":\""+ ret.getString("idav") + "\", \"numeav\":\"" + ret.getString("numeav") + "\", \"gc\":\"" + ret.getString("gama_croaziera") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@PostMapping(value = "/pilotiAvion", produces = "application/json")
	public String postPilot(@RequestBody int id, HttpServletResponse response) throws SQLException {

		ResultSet ret = null;
		String retStr = "[";
		System.out.println(id);

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select * from Certificare c join Angajati a on a.idan = c.idan where idav = " +
								id +
								";";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					//System.out.println(ret.getString("numean") + " " + ret.getString("functie"));
					//retStr += ret.getString("numean") + " " + ret.getString("numeav") + "<br>";
					retStr += "{\"id\":\""+ ret.getString("idan") + "\", \"numean\":\"" + ret.getString("numean") + "\", \"sal\":\"" + ret.getString("salariu") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@PostMapping(value = "/avioaneSpeciale", produces = "application/json")
	public String postAvioanSpecial(@RequestBody String numeav, HttpServletResponse response) throws SQLException {

		ResultSet ret = null;
		String retStr = "[";
		System.out.println(numeav);

		try {
//                                              jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
//			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select * from Aeronave where numeav like '" +
								numeav +
								"%';";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					//System.out.println(ret.getString("numean") + " " + ret.getString("functie"));
					//retStr += ret.getString("numean") + " " + ret.getString("numeav") + "<br>";
					retStr += "{\"id\":\""+ ret.getString("idav") + "\", \"numeav\":\"" + ret.getString("numeav") + "\", \"gc\":\"" + ret.getString("gama_croaziera") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@GetMapping("/3a")
	public String p3a() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"SELECT *\n" +
								"FROM Zboruri\n" +
								"WHERE distanta BETWEEN 500 AND 1000;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"nrz\":\"" + ret.getString("nrz") + "\", \"de_la\":\"" + ret.getString("de_la") + "\", \"dist\":\"" + ret.getString("distanta") + "\", \"la\":\"" + ret.getString("la")
							+ "\", \"zi\":\"" + ret.getString("zi") +"\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@GetMapping("/3b")
	public String p3b() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"SELECT *\n" +
								"FROM Zboruri\n" +
								"WHERE zi IN ('Ma', 'Vi')\n" +
								"ORDER BY plecare ASC;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"nrz\":\"" + ret.getString("nrz") + "\", \"de_la\":\"" + ret.getString("de_la") + "\", \"dist\":\"" + ret.getString("distanta") + "\", \"la\":\"" + ret.getString("la")
							+ "\", \"zi\":\"" + ret.getString("zi") +"\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@GetMapping("/4a")
	public String p4a() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"SELECT *\n" +
								"FROM Zboruri z\n" +
								"JOIN Aeronave a ON z.distanta < a.gama_croaziera AND a.numeav LIKE 'BOEING 787';";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"nrz\":\"" + ret.getString("nrz") + "\", \"de_la\":\"" + ret.getString("de_la") + "\", \"dist\":\"" + ret.getString("distanta") + "\", \"la\":\"" + ret.getString("la")
							+ "\", \"zi\":\"" + ret.getString("zi") +"\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@GetMapping("/4b")
	public String p4b() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"SELECT distinct c1.idav as av1, c2.idav as av2\n" +
								"FROM Certificare c1 JOIN Certificare c2\n" +
								"ON c1.idan = c2.idan\n" +
								"where c1.idav < c2.idav\n" +
								";";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"av1\":\"" + ret.getString("av1") + "\", \"av2\":\"" + ret.getString("av2") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@PostMapping(value = "/5a", produces = "application/json")
	public String p5a(@RequestBody String numeav, HttpServletResponse response) throws SQLException {

		ResultSet ret = null;
		String retStr = "[";
		System.out.println(numeav);

		try {
//                                              jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
//			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"select top 1 salariu from\n" +
								"angajati an \n" +
								"join certificare ce on an.idan = ce.idan\n" +
								"join aeronave av on ce.idav = av.idav\n" +
								"where numeav like '"+numeav+"%'\n" +
								"order by salariu desc ;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"salMax\":\""+ ret.getString("salariu") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}

	@GetMapping("/6b")
	public String p6b() throws SQLException {
		System.out.println("acc");
		ResultSet ret = null;
		String retStr = "[";

		try {
//                                                  jdbc:sqlserver://server:port; jdbc:sqlserver://localhost:1433
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;Database=colocviu;User=inPoint1;Password=daria;Trusted_Connection=true;encrypt=false;trustServerCertificate=true;authenticationScheme=NTLM;");//domain=myDomain");
			if (conn != null) {
				Statement stmnt = conn.createStatement();
				String inter =
						"SELECT functie, AVG(salariu) AS salariu_mediu\n" +
								"FROM Angajati\n" +
								"GROUP BY functie;";
				ret = stmnt.executeQuery(inter);

				while(ret.next()){
					retStr += "{\"functie\":\"" + ret.getString("functie") + "\", \"salariu\":\"" + ret.getString("salariu_mediu") + "\"}, ";
				}
				retStr = retStr.substring(0, retStr.length()-2);
				retStr += "]";

				//System.out.println(retStr);

				return retStr;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					System.out.println("close connection");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return "Whoops an error made it impossible to connect to our database ";
	}
}
