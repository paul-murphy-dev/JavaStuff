package com.pm.data.nfa;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.*;

import org.joda.time.DateTime;

import com.sun.swing.internal.plaf.metal.resources.metal;

public final class DataLayer {
	
	private static String Server;
	private static String Username;
	private static String Password;
	private static String Instance;
	private static String Database;
	private static DatabaseType dbType = DatabaseType.SQLServer;
	private static boolean _registered = false;	
	
	public static void Init(DatabaseType dbType, String server, String database, String userName, String password){
		Init(dbType, server, null, database, -1, userName, password);
	}
	
	public static void Init(DatabaseType dbType, String server, String instance, String database, String userName, String password){
		Init(dbType, server, instance, database, -1, userName, password);
	}
	
	public static void Init(DatabaseType dbType, String server, String instance, String database, int port, String userName, String password){
		DataLayer.setDbType(dbType);
    	DataLayer.Server = server;
    	DataLayer.Database = database;
    	DataLayer.Instance = instance;
    	DataLayer.Username = userName;
    	DataLayer.Password = password;
    	DataLayer.Port = port;
	}
	
	
	public static DatabaseType getDbType() {
		return dbType;
	}

	public static void setDbType(DatabaseType dbType) {
		DataLayer.dbType = dbType;
		
		if(_registered) return;
		
		_registered = true;
		switch(dbType){
			case SQLServer:
				RegisterMsDriver();
			break;
			
			case MYSQL:
				RegisterOracleDriver();
			break;
		}
	
	}

	private static int Port = -1;
	
	public static int defaultPort;
	
	public static int getDefaultPort(){
	
		switch(getDbType()){
		case SQLServer:
			return 1433;
		case MYSQL:
			return 3306;
		}
		return -1;
	}
	
	private static void RegisterMsDriver()
	{	
		String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		try {
		    Class.forName(dbDriver).newInstance();
		} catch (Exception ex) {
		    System.out.println("failure");
		}
	}
	
	private static void RegisterOracleDriver(){
		String dbDriver = "com.mysql.jdbc.Driver";

		try {
		    Class.forName(dbDriver).newInstance();
		} catch (Exception ex) {
		    System.out.println("failure");
		}
	}
	
	private static Connection getConnection(){
		
		Connection conn = null;
		String dbURL = null;
		String urlTemplate = getUrlTemplate();
		
		if(Port == -1)
			Port = getDefaultPort();
		
		dbURL = String.format(urlTemplate, Server, Instance, Port, Database);
        
        try {
			conn = DriverManager.getConnection(dbURL, Username, Password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return conn;
	}

	private static String getUrlTemplate() {
		switch(getDbType()){
		case  SQLServer:
			if(Instance == null)
				return "jdbc:sqlserver://%s:%s;databaseName=%s;";
			else
				return "jdbc:sqlserver://%s\\%s:%s;databaseName=%s;";
			
		case MYSQL:
			if(Instance == null)
				return "jdbc:mysql://%s:%s;databaseName=%s;";
			else
				return "jdbc:mysql://%s\\%s:%s;databaseName=%s;";
			
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> GetObjectList(String sql, Class<T> t){
		
		Connection conn = getConnection();
		
		if(conn!=null){
			Statement command = null;
			try {
				command = conn.createStatement();
				ResultSet rs = command.executeQuery(sql);				
				
				List<T> objects = new ArrayList<T>();
				
				try {
					T someType = (T) t.newInstance();
					
					List<Method> methods = Arrays.stream(t.getMethods()).filter(m->m.getName().startsWith("set") && m.getParameterTypes().length == 1).collect(Collectors.toList());
					
					while(rs.next()){
						for(Method m : methods){
							String name = m.getName().substring(3);
							
							int idx = rs.findColumn(name);
							if(idx>=0){
								Class type = m.getParameterTypes()[0];
								String typeName = type.getName().toLowerCase();
								
								if(typeName.equals("int")){
									m.invoke(someType, rs.getInt(idx));
									
								}else if(typeName.equals("java.sql.date")){
									m.invoke(someType, rs.getDate(idx));
									
								}else if(typeName.equals("org.joda.time.datetime")){
									m.invoke(someType, new DateTime(rs.getTimestamp(idx)));
									
								}else if(typeName.equals("float")){
									m.invoke(someType, rs.getFloat(idx));
									
								}else if(typeName.equals("double")){
									m.invoke(someType, rs.getDouble(idx));
									
								}else if(typeName.equals("java.lang.string")){
									m.invoke(someType, rs.getString(idx));
								}							
							}
						}
						objects.add(someType);
					}
					rs.close();
					
					return objects;
					
				} catch (Exception e) {
					e.printStackTrace();
				}				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					command.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
		
	
		return null;
	}
}
