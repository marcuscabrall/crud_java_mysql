package br.com.szbetter.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	
	// Nome do usuário do mysql
	private static final String USERNAME = "root";
	
	// Senha do DB
	private static final String PASSWORD = "12345";
	
	//Caminho do DB, porta, nome do DB
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3308/usuario";
			
	//Conexão com o DB
	public static Connection createConnectionToMySQL() throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
		
		return connection;
	}
	
	public static void main(String[] args) throws Exception { 
		
		// Recuperar uma conexão com o DB
		Connection con = createConnectionToMySQL();
		
		// Testar se a conexão é nula
		if(con != null) {
			System.out.println("Conexão estabelecida com sucesso!");
			con.close();
		}
	}
	
}
