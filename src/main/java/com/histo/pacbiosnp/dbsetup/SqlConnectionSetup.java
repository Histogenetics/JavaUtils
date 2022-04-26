package com.histo.pacbiosnp.dbsetup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class SqlConnectionSetup {
//	@Value("${app.datasource.histos.url}")
//	public String histoServerURL;
//	@Value("${app.datasource.histos.username}")
//	public String histoServerUsername;
//	@Value("${app.datasource.histos.password}")
//	public String histoServerPassword;
	
	public static String url;
	public static String Uname;
	public static String Pswd;
	
	
//	@Value("${app.datasource.histos.url}")
//	public void setHistoServerURL(String histoServerURL) {
//		SqlConnectionSetup.url = histoServerURL;
//	}
//	@Value("${app.datasource.histos.username}")
//	public void setHistoServerUsername(String histoServerUsername) {
//		SqlConnectionSetup.Uname = histoServerUsername;
//	}
//	@Value("${app.datasource.histos.password}")
//	public void setHistoServerPassword(String histoServerPassword) {
//		SqlConnectionSetup.Pswd = histoServerPassword;
//	}
	
	private static Connection sqlcon = null;
	
//	@PostConstruct
	private static Connection getSqlConnection() {
		try {
			getURLProperities();
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			sqlcon = DriverManager.getConnection(url, Uname, Pswd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sqlcon;
	}
	
	public static synchronized Connection getConnection() {
		if(sqlcon == null)
			getSqlConnection();
		return sqlcon;
		
	}
	public synchronized static Properties getURLProperities() {
		Properties prop = new Properties();
		try {
			String path = System.getProperty("user.dir");
	        String dbConfig = path + File.separator + "database" + File.separator
	                + "dbconnectionsconfiguration.properties";
//			InputStream input = new FileInputStream(dbConfig);
	        InputStream input = SqlConnectionSetup.class.getClassLoader().getResourceAsStream("pacbioSNP_dbconnectionsconfiguration.properties");
            prop.load(input);
            url = prop.getProperty("histos.url");
            Uname = prop.getProperty("histos.url.userName");
            Pswd = prop.getProperty("histos.url.password");
            		

        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return prop;
		
	}

}
