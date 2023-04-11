package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory{
	
	private DataSource dataSource;
	
	public ConnectionFactory() {
		String url = "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC";
		String user = "root";
		String password = "admin";
		
		var pooledDataSource = new ComboPooledDataSource();
		pooledDataSource.setJdbcUrl(url);
		pooledDataSource.setUser(user);
		pooledDataSource.setPassword(password);
		pooledDataSource.setMaxPoolSize(10);
		
		this.dataSource = pooledDataSource;
	}
	
	public Connection recuperaConexion () {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
