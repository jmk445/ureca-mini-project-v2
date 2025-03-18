package app.phone.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.print.Book;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import app.phone.dao.ShoppingCartDao;
import app.phone.dto.Phone;

public class PhoneDetailDialog extends JDialog {
	private JTable table;
    private DefaultTableModel cartTableModel;
    private JButton buyButton;
    private ShoppingCartDao cartDao = new ShoppingCartDao();

    public PhoneDetailDialog(MallManager parent) {
        setTitle("휴대폰 세부 정보");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        // 장바구니 전용 TableModel (재고 → 수량)
        cartTableModel = new DefaultTableModel(new Object[]{"휴대폰 이름", "가격", "제조사", "재고"}, 0) {
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














