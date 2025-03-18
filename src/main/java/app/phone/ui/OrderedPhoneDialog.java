package app.phone.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.phone.dto.Order;
import app.phone.ui.AdminDialog;
public class OrderedPhoneDialog extends JDialog {
    private Order order;
    
    public OrderedPhoneDialog(AdminDialog parent, Order order) {
        super(parent, "구매한 휴대폰 목록", true);
        this.order = order;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        // 주문 정보 표시
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel("주문자: " + order.getCustomerName()), BorderLayout.NORTH);
        panel.add(new JLabel("주문 시간: " + order.getOrderTime()), BorderLayout.CENTER);
//        panel.add(new JLabel("주문 휴대폰: " + order.getPhoneName()), BorderLayout.SOUTH);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton, BorderLayout.PAGE_END);

        add(panel);
    }
}
