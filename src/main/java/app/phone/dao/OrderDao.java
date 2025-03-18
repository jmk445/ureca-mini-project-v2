package app.phone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.phone.common.DBManager;
import app.phone.dto.Order;
import app.phone.dto.Phone;

public class OrderDao {

    private PhoneDao phoneDao = new PhoneDao();
    private CustomerDao customerDao = new CustomerDao();
    
    public OrderDao(){
    	
    }
    public List<Order> listOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT order_id, customer_id, ordertime FROM orders";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                int customerId = rs.getInt("customer_id");
                String customerName = customerDao.getCustomerName(customerId);
                order.setCustomerName(customerName);
                order.setCustomerId(customerId);
                                
                order.setCustomerId(rs.getInt("customer_id"));
                
                order.setOrderTime(rs.getTimestamp("ordertime")); // order_time 컬럼이 DATETIME or TIMESTAMP라고 가정

                // 주문한 휴대폰 목록 가져오기
                List<Phone> phoneList = new ArrayList<Phone>(); 
                		//getPhonesByOrderId(order.getOrderId());
                
                order.setOrderedPhones(phoneList);

                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }

        return list;
    }
    

    public String getPhonesByOrderId(int orderId) {
        List<Phone> phoneList = new ArrayList<>();
        String phones = "";
        String sql = "SELECT phone_id FROM orders_phone WHERE order_id = ?"; // 주문과 휴대폰 매핑 테이블

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int phoneId = rs.getInt("phone_id");
                Phone phone = phoneDao.detailPhone(phoneId); // PhoneDao 활용
                if (phone != null) {
                    phoneList.add(phone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(pstmt, con);
        }
        StringBuilder sb = new StringBuilder();

        HashMap<String, Integer> phone_cnt = new HashMap<>();

        // phoneList에서 각 모델명에 해당하는 전화기 개수를 센다
        for (Phone phone : phoneList) {
            String name = phone.getPhoneName();
            
            if (phone_cnt.containsKey(name)) {
                phone_cnt.put(name, phone_cnt.get(name) + 1);
            } else {
                phone_cnt.put(name, 1);
            }
        }
        
     // StringBuilder로 결과를 정리
        StringBuilder result = new StringBuilder();

        // phone_cnt HashMap 순회
        for (Map.Entry<String, Integer> entry : phone_cnt.entrySet()) {
            String phoneName = entry.getKey();  // 모델명
            int count = entry.getValue();       // 개수

            // 모델명과 개수를 결과에 추가
            result.append(phoneName).append(": ").append(count).append("개\n");
        }

        // 결과 출력
//        System.out.println(result.toString());

        
        return result.toString();
    }

    /**
     * 주문을 추가하는 메서드
     * @param customerId 주문자의 ID
     * @param phoneIds 주문한 휴대폰 ID 목록
     * @return 성공 여부 (true: 성공, false: 실패)
     */
    public boolean insertOrder(int shoppingCartId, List<Phone> phoneList) {
    	
    	
        String insertOrderSQL = "INSERT INTO orders (customer_id, ordertime) VALUES (?, NOW())";
        String insertOrderPhoneSQL = "INSERT INTO orders_phone (order_id, phone_id) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement orderPstmt = null;
        PreparedStatement orderPhonePstmt = null;
        ResultSet generatedKeys = null;

        try {
            con = DBManager.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // 1. orders 테이블에 주문 추가
            orderPstmt = con.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            
            int customerId = customerDao.getCustomerIdByCart(shoppingCartId);
            orderPstmt.setInt(1, customerId);            
            int affectedRows = orderPstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("주문 추가 실패");
            }

            // 생성된 주문 ID 가져오기
            generatedKeys = orderPstmt.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("주문 ID 조회 실패");
            }

            // 2. order_phone 테이블에 주문-휴대폰 매핑 추가
            orderPhonePstmt = con.prepareStatement(insertOrderPhoneSQL);
            for(Phone phone : phoneList) {
            	int phoneId = phone.getPhoneId();
            	orderPhonePstmt.setInt(1, orderId);
                orderPhonePstmt.setInt(2, phoneId);
                orderPhonePstmt.addBatch(); // 배치 실행을 위해 추가
            }
            
            orderPhonePstmt.executeBatch(); // 한 번에 실행

            con.commit(); // 트랜잭션 커밋
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback(); // 오류 발생 시 롤백
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
//            DBManager.releaseConnection(orderPhonePstmt,con);
//            DBManager.releaseConnection(generatedKeys,con);
            DBManager.releaseConnection(orderPstmt, con);
        }
    }
}
