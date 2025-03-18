package app.phone.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import app.phone.dao.OrderDao;
import app.phone.dao.PhoneDao;
import app.phone.dto.Order;
import app.phone.dto.Phone;

public class AdminDialog extends JDialog {

    private JTable phoneTable;
    private DefaultTableModel phoneTableModel;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    private PhoneDao phoneDao = new PhoneDao();
    private OrderDao orderDao = new OrderDao();
    private List<Phone> phoneList;
    private List<Order> orderList;
    public AdminDialog(MallManager parent) {
        super(parent, "관리자 페이지(휴대폰의 각 셀을 눌러 편집할 수 있습니다)", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);

        // 상단에 메시지 추가
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("각 셀을 눌러 수정할 수 있습니다."));
        add(topPanel, BorderLayout.NORTH);

        // 테이블 모델 설정
        phoneTableModel = new DefaultTableModel(new Object[]{"모델명", "가격(원)", "제조사", "재고"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // 모델명은 수정 불가, 나머지는 수정 가능
            }
        };
        phoneTable = new JTable(phoneTableModel);
        listPhones(); // 휴대폰 목록 불러오기

        phoneTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    updatePhoneInfo(row);
                }
            }
        });

        // 주문 테이블 모델 설정
        orderTableModel = new DefaultTableModel(new Object[]{"주문자", "주문 시간", "주문 휴대폰"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 편집 불가
            }
        };
        orderTable = new JTable(orderTableModel);
        listOrders(); // 주문 목록 불러오기

        // 두 개의 테이블을 세로로 배치하기 위해 JSplitPane 사용
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(phoneTable), BorderLayout.CENTER); // 휴대폰 테이블
        tablePanel.add(new JScrollPane(orderTable), BorderLayout.SOUTH); // 주문 테이블

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, new JScrollPane(orderTable));
        splitPane.setResizeWeight(0.5); // 위쪽(휴대폰 목록) 크기를 50% 차지하도록 설정

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        // 하단에 버튼 추가
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        JButton addPhoneButton = new JButton("휴대폰 추가");
        addPhoneButton.addActionListener(e -> addPhone());
        bottomPanel.add(addPhoneButton);
    }


    

    private void listPhones() {
        phoneTableModel.setRowCount(0);
        phoneList = phoneDao.listPhone();

        for (Phone phone : phoneList) {
            phoneTableModel.addRow(new Object[]{
                phone.getPhoneName(),
                phone.getPhoneprice(),
                phone.getPhonemaker(),
                phone.getPhoneremain()
            });
        }
    }

    private void listOrders() {
        orderTableModel.setRowCount(0);
        orderList = orderDao.listOrders(); // 주문 목록 불러오기

        for (Order order : orderList) {
            orderTableModel.addRow(new Object[]{
                order.getCustomerName(),
                order.getOrderTime(),                
                orderDao.getPhonesByOrderId(order.getOrderId()), // 버튼 텍스트
            });
        }
    }

    private void updatePhoneInfo(int row) {
        if (row < 0 || row >= phoneList.size()) return;

        Phone phone = phoneList.get(row);
        String newPrice = phoneTableModel.getValueAt(row, 1).toString();
        String newMaker = phoneTableModel.getValueAt(row, 2).toString();
        String newRemain = phoneTableModel.getValueAt(row, 3).toString();

        try {
            phone.setPhoneprice(Integer.parseInt(newPrice));
            phone.setPhonemaker(newMaker);
            phone.setPhoneremain(Integer.parseInt(newRemain));

            int result = phoneDao.updatePhone(phone);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "휴대폰 정보가 업데이트되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "업데이트 실패", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "숫자 입력 오류", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPhone() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField makerField = new JTextField();
        JTextField remainField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("모델명:"));
        panel.add(nameField);
        panel.add(new JLabel("가격(원):"));
        panel.add(priceField);
        panel.add(new JLabel("제조사:"));
        panel.add(makerField);
        panel.add(new JLabel("재고:"));
        panel.add(remainField);

        int result = JOptionPane.showConfirmDialog(this, panel, "휴대폰 추가", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int price = Integer.parseInt(priceField.getText().trim());
                String maker = makerField.getText().trim();
                int remain = Integer.parseInt(remainField.getText().trim());

                Phone phone = new Phone(name, price, maker, remain);
                int insertResult = phoneDao.insertPhone(phone);

                if (insertResult > 0) {
                    JOptionPane.showMessageDialog(this, "휴대폰이 추가되었습니다.");
                    listPhones(); // 목록 갱신
                } else {
                    JOptionPane.showMessageDialog(this, "추가 실패", "오류", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "입력값이 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
