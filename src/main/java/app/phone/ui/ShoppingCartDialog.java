package app.phone.ui;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.OrderDao;
import app.phone.dao.ShoppingCartDao;
import app.phone.dto.Phone;


public class ShoppingCartDialog extends JDialog {
    private JTable table;
    private DefaultTableModel cartTableModel;
    private JButton buyButton;
    private ShoppingCartDao cartDao = new ShoppingCartDao();
    private OrderDao orderDao = new OrderDao();

    public ShoppingCartDialog(MallManager parent) {
        setTitle("장바구니");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        // 장바구니 전용 TableModel (재고 → 수량)
        cartTableModel = new DefaultTableModel(new Object[]{"휴대폰 이름", "가격", "제조사", "수량"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀을 수정 불가능하게 설정
            }
        };

        table = new JTable(cartTableModel);
        table.setFillsViewportHeight(true);

        // 스크롤 가능하게 만들기
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        listPhonesInCart();

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel();
        buyButton = new JButton("구매하기");
        buttonPanel.add(buyButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        
        buyButton.addActionListener(e -> processPurchase(parent));

    }
    
    private void processPurchase(MallManager parent) {
        List<Phone> phoneList = cartDao.listPhonesInCart(1); // 장바구니 목록 가져오기

        if (phoneList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "장바구니가 비어 있습니다!", "구매 불가", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Phone phone : phoneList) {
        	System.out.println(phone.getPhoneremain());
            int phoneId = phone.getPhoneId();
            int cartCount = cartDao.getCnt(1, phoneId); // 장바구니에 담긴 수량
            int stock = phone.getPhoneremain(); // 현재 재고

            if (cartCount > stock) {
                JOptionPane.showMessageDialog(this, phone.getPhoneName() + "의 재고가 부족합니다!", "구매 실패", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 구매 처리 (재고 감소 & 장바구니 비우기)
        for (Phone phone : phoneList) {
            int phoneId = phone.getPhoneId();
            int buyCount = cartDao.getCnt(1, phoneId);

            cartDao.updateRemain(1, phoneId, buyCount); // 실제 구매 처리 (재고 감소)
        }

        cartDao.clearCart(1); // 장바구니 비우기
        listPhonesInCart(); // UI 업데이트
        JOptionPane.showMessageDialog(this, "구매가 완료되었습니다!", "구매 성공", JOptionPane.INFORMATION_MESSAGE);
        
        parent.listPhone();
        
        orderDao.insertOrder(1,phoneList);
    }

    public void listPhonesInCart() {
        cartTableModel.setRowCount(0); // 기존 데이터 초기화

        List<Phone> phoneList = cartDao.listPhonesInCart(1);
        for (Phone phone : phoneList) {
            cartTableModel.addRow(new Object[]{
                phone.getPhoneName(),
                phone.getPhoneprice(),
                phone.getPhonemaker(),
                cartDao.getCnt(1, phone.getPhoneId()) // '수량' 데이터 삽입
            });
        }
    }
    
    
}
