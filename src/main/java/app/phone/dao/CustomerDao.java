package app.phone.dao;

import java.sql.*;
import app.phone.common.DBManager;

public class CustomerDao {
	public CustomerDao() {
		
	}
	
	public int getCustomerIdByCart(int cartId) {
		int customerId = 0;
		String sql = "select customer_id from shoppingcart where shoppingcart_id = ?";
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, cartId); // customer_id 값을 바인딩

            rs = pstmt.executeQuery();

            if (rs.next()) {
                customerId = rs.getInt("customer_id"); // name 컬럼 값 가져오기
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return customerId;    
	}
	
    public String getCustomerName(int customerId) {
        String customerName = null;
        String sql = "SELECT name FROM customer WHERE customer_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerId); // customer_id 값을 바인딩

            rs = pstmt.executeQuery();

            if (rs.next()) {
                customerName = rs.getString("name"); // name 컬럼 값 가져오기
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return customerName;
    }
}

