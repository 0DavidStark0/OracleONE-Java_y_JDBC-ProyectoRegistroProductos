package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {
	
	final private Connection con;
	
	public ProductoDAO(Connection con) {
		this.con = con;
	}
	
	public void guardar(Producto producto) {
		try {
			final PreparedStatement statement = con.prepareStatement(
						"INSERT INTO tbproducto "
						+ "(nombre, descripcion, cantidad, categoria_id)" 
						+ "VALUES (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			try (statement) {
				ejecutaRegistro(producto, statement);
			} 

		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {

		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());
		statement.setInt(4, producto.getCategoriaId());

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();

		try (resultSet) {
			while (resultSet.next()) {
				producto.setId(resultSet.getInt(1));
				System.out.println(String.format("Fue insertado el producto de ID %s", producto));
			}
		}
	}

	public List<Producto> listar() {
		List<Producto> resultado = new ArrayList<>();
		try {
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM tbproducto");
			try (statement) {
				statement.execute();
				
				final ResultSet resultSet = statement.getResultSet();
				try(resultSet){
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"),
												resultSet.getString("NOMBRE"),
												resultSet.getString("DESCRIPCION"),
												resultSet.getInt("CANTIDAD"));
						
						resultado.add(fila);
					}
				}
			}
			
			return resultado;
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public int eliminar(Integer id) {
		try {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM tbproducto WHERE id = ?");
			
			try (statement) {
				statement.setInt(1, id);
				statement.execute();

				return statement.getUpdateCount();
			}
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int modificar (String nombre, String descripcion, int cantidad, Integer id) {
		try {
			final PreparedStatement statement = con.prepareStatement(
							"UPDATE tbproducto SET " 
							+ "nombre = ?" 
							+ ", descripcion = ?" 
							+ ", cantidad = ?" 
							+ " WHERE id = ?");
			
			try (statement) {
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);

				statement.execute();

				return statement.getUpdateCount();
			}
		}catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}

	public List<Producto> listar(Integer categoriaId) {
		List<Producto> resultado = new ArrayList<>();
		try {
			var querySelect = "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD "
					+ "FROM tbproducto "
					+ "WHERE categoria_id = ?";
			System.out.println(querySelect);
			
			final PreparedStatement statement = con.prepareStatement(
					querySelect);
			try (statement) {
				statement.setInt(1, categoriaId);
				statement.execute();
				
				final ResultSet resultSet = statement.getResultSet();
				try(resultSet){
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"),
												resultSet.getString("NOMBRE"),
												resultSet.getString("DESCRIPCION"),
												resultSet.getInt("CANTIDAD"));
						
						resultado.add(fila);
					}
				}
			}
			
			return resultado;
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
