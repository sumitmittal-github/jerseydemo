package com.sumit.jerseydemo.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sumit.jerseydemo.model.Product;

public class ProductRepository {
	
	private Connection conn = null;
	public ProductRepository(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jerseydemodb","root","root");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Product> getProductList() {
		List<Product> list = new ArrayList<Product>();
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("select * from product");
			while(rs.next()) {
				Product p = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("image_url"), rs.getDouble("price"), 
										rs.getInt("quantity"), rs.getDate("created_at"));
				list.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }
	
	public Product getProductById(long id) {
		Product p = null;
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("select * from product where id="+id);
			if(rs.next()) {
				p = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("image_url"), rs.getDouble("price"), 
						rs.getInt("quantity"), rs.getDate("created_at"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
    }

	public void createProduct(Product product) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("insert into product (name, image_url, price, quantity) values(?,?,?,?)");
			stmt.setString(1, product.getName());
			stmt.setString(2, product.getImageUrl());
			stmt.setDouble(3, product.getPrice());
			stmt.setInt(4, product.getQuantity());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}