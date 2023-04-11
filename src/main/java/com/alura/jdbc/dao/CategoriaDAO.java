package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class CategoriaDAO {
	
	private Connection con;

	public CategoriaDAO(Connection con) {
		this.con = con;
	}

	public List<Categoria> listar() {
		List<Categoria> resultado = new ArrayList<>();
		
		try {
			var querySelect ="SELECT id, nombre FROM tbcategoria";
			System.out.println(querySelect);
			
			final PreparedStatement statement = con.prepareStatement(
					querySelect);
			
			try(statement){
				final ResultSet resultSet = statement.executeQuery();
				try (resultSet) {
					while (resultSet.next()) {
						var categoria = new Categoria(resultSet.getInt("ID"),
								resultSet.getString("NOMBRE"));
						
						resultado.add(categoria);
					}
				};
			}	
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
		return resultado;
	}

	public List<Categoria> listarConProductos() {
		List<Categoria> resultado = new ArrayList<>();
		
		try {
			var querySelect ="SELECT ca.id, ca.nombre, pr.id, pr.nombre, pr.cantidad "
					+ "FROM tbcategoria ca "
					+ "INNER JOIN tbproducto pr ON ca.id = pr.categoria_id";
			System.out.println(querySelect);
			
			final PreparedStatement statement = con.prepareStatement(
					querySelect);
			
			try(statement){
				final ResultSet resultSet = statement.executeQuery();
				try (resultSet) {
					while (resultSet.next()) {
						Integer categoriaId = resultSet.getInt("ca.ID");
						String categoriaNombre = resultSet.getString("ca.NOMBRE");
						
						var categoria = resultado
								.stream()
								.filter(cat -> cat.getId().equals(categoriaId))
								.findAny().orElseGet(() -> {
									Categoria cat = new Categoria(categoriaId,
											categoriaNombre);
									
									resultado.add(cat);
									
									return cat;
								});
						
						Producto producto = new Producto(resultSet.getInt("pr.id"),
								resultSet.getString("pr.nombre"),
								resultSet.getInt("pr.cantidad"));
						
						categoria.agregar(producto);
					}
				};
			}	
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
		return resultado;
	}

}
