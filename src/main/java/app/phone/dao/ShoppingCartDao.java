package app.phone.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.phone.common.DBManager;
import app.phone.dto.Phone;
import app.phone.dao.PhoneDao;

public class ShoppingCartDao {
	private PhoneDao phoneDao = new PhoneDao();
	public int insertPhonesInCart() {
		return 0;
	}
	public List<Phone> listPhonesInCart(int shoppingcartid) {
		// TODO Auto-generated method stub
		List<Phone> list = new ArrayList<>();
		  
		String sql = "select * from phone p,shoppingcart_phone sp where sp.shoppingcartid = ? and sp.phoneid = p.phoneid;";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, shoppingcartid);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Phone phone = new Phone();
				phone.setPhoneId(rs.getInt("phoneid"));
				phone.setPhoneName(rs.getString("phonename"));
				phone.setPhoneprice(rs.getInt("phoneprice"));
				phone.setPhonemaker(rs.getString("phonemaker"));
				phone.setPhoneremain(rs.getInt("phoneremain"));
				list.add(phone);
			}			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.releaseConnection(pstmt, con);
		}
		
		return list;
	}

	public int getCnt(int shoppingcart_id,int phoneid) {
		int cnt = 0;
		String sql = "select * from shoppingcart_phone where shoppingcartid = ? and phoneid = ?;";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, shoppingcart_id);
			pstmt.setInt(2, phoneid);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				cnt = rs.getInt("buycnt");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.releaseConnection(pstmt, con);
		}
		
		return cnt;	
	}
	
	
	public int addToCart(int cartId, int phoneId) {
		//선택된 cart와 - phone Id 튜플을 insert
		int ret = -1;
		String sql = "Insert into shoppingcart_phone values (?,?,?);";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, cartId);			
			pstmt.setInt(2, phoneId);			
			pstmt.setInt(3, 1);
			ret = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			if(e.getErrorCode() == 1062) { //dup 이 일어났을 때 즉, 이미 같은 상품을 넣어놨을 때.
				updateCart(cartId,phoneId);
				System.out.println("이미 구매하였습니다. 수정하겠습니다.");
			}else {
				e.printStackTrace();
			}
			
		}finally {
			DBManager.releaseConnection(pstmt, con);
		}
		return ret;
	}
	
	
	public void updateCart(int cartId, int phoneId) {
		int ret = -1;		
		String sql = "update shoppingcart_phone set buycnt = ? where shoppingcartid = ? and phoneid = ?; ";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);
			int updatedCnt = getCnt(cartId,phoneId) + 1;
			pstmt.setInt(1, updatedCnt);
			pstmt.setInt(2, cartId);
			pstmt.setInt(3, phoneId);
			
			ret = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.releaseConnection(pstmt, con);
		}				
	}
	public void updateRemain(int cartId, int phoneId, int quantity) {
	    
		//각 휴대폰의 재고 값 update
	    String sql = "update phone set phoneremain = ? where phoneid = ?;";
	    System.out.println(phoneId);
	    Connection con = null;
		PreparedStatement pstmt = null;
		
	    try {	         
	    	con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);
			
	    	int curRemain = phoneDao.detailPhone(phoneId).getPhoneremain();
	    	int updatedRemain = curRemain - quantity;
	    	System.out.println(updatedRemain);
	        pstmt.setInt(1, updatedRemain);	        
	        pstmt.setInt(2, phoneId);	
	        
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void clearCart(int shoppingCartId) {
		// TODO Auto-generated method stub
		//truncate table shoppingcart_phone 
		int ret = -1;
		String sql = "delete from shoppingcart_phone";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBManager.getConnection();			
			pstmt = con.prepareStatement(sql);			
			
			ret = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.releaseConnection(pstmt, con);
		}
		
		
	}
	
		
}
