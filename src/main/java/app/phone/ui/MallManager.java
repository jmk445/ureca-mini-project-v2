package app.phone.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import app.phone.dao.PhoneDao;
import app.phone.dao.ShoppingCartDao;
import app.phone.dto.Phone;

public class MallManager extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton shoppingCartButton;
    private JButton userChangeButton;
    private JButton adminPageButton; // 관리자 페이지 버튼 추가
    private PhoneDao phoneDao = new PhoneDao();
    private ShoppingCartDao cartDao = new ShoppingCartDao();    
    private List<Phone> phoneList;
    
    public MallManager() {
        setTitle("앗 휴대폰 신발보다 싸다");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 테이블 모델 생성 (버튼을 추가할 "액션1", "액션2" 컬럼 포함)
        tableModel = new DefaultTableModel(new Object[]{"모델명", "가격(원)", "제조사", "재고", "장바구니"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // "액션1", "액션2" 열만 수정 가능 (버튼 클릭)
            }
        };

        table = new JTable(tableModel);
        
        table.getColumn("장바구니").setCellRenderer(new ButtonRenderer("장바구니"));
        table.getColumn("장바구니").setCellEditor(new ButtonEditor(new JButton("장바구니"), "장바구니"));

//        table.getColumn("자세히 보기").setCellRenderer(new ButtonRenderer("자세히 보기"));
//        table.getColumn("자세히 보기").setCellEditor(new ButtonEditor(new JButton("자세히 보기"), "자세히 보기"));

        listPhone();
//        userChangeButton = new JButton("사용자 변경");
        shoppingCartButton = new JButton("장바구니 보기");
        adminPageButton = new JButton("관리자페이지로 이동"); // 관리자 페이지 버튼 추가

        JPanel bottomPanel = new JPanel();
        
        bottomPanel.add(shoppingCartButton);
        bottomPanel.add(adminPageButton); // 버튼 추가

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        shoppingCartButton.addActionListener(e -> {
            ShoppingCartDialog cartDialog = new ShoppingCartDialog(this);
            cartDialog.setVisible(true);
        });

        // 관리자 페이지 버튼 클릭 이벤트 처리
        adminPageButton.addActionListener(e -> {
            AdminDialog adminDialog = new AdminDialog(this);
            adminDialog.setVisible(true);
        });
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    public void listPhone() {
        clearTable();
        phoneList = phoneDao.listPhone(); // phoneList 저장

        for (Phone phone : phoneList) {
            tableModel.addRow(new Object[]{
                phone.getPhoneName(),
                phone.getPhoneprice(),
                phone.getPhonemaker(),
                phone.getPhoneremain(),
                "장바구니 담기", 
                
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MallManager().setVisible(true);
        });
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    /**
     * 버튼을 편집(클릭)할 수 있도록 하는 클래스 (버튼 클릭 이벤트 처리)
     */
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private int selectedRow;

        public ButtonEditor(JButton button, String label) {
            this.button = new JButton(label);
            this.label = label;
            this.button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? button.getText() : value.toString();
            button.setText(label);
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("선택된 행: " + selectedRow + "의 '" + label + "' 버튼 클릭됨!");
//
//            if (label.equals("자세히 보기")) {
//                JOptionPane.showMessageDialog(null, 
//                        "서비스 준비 중입니다.", 
//                        "죄송합니다.", 
//                        JOptionPane.WARNING_MESSAGE);
//            } 
            if (label.equals("장바구니 담기")) {
                int phoneId = phoneList.get(selectedRow).getPhoneId(); // phone ID 가져오기
                JOptionPane.showMessageDialog(null, 
                        "장바구니에 추가되었습니다.", 
                        "완료", 
                        JOptionPane.WARNING_MESSAGE);
                moveToCart(1, phoneId);                
            }
            fireEditingStopped();
        }

    }
    
    void moveToCart(int shoppingCartId,int phoneId) {
    	int infectedRow = cartDao.addToCart(shoppingCartId, phoneId);    	
    }
}
