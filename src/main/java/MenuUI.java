
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUI {
    public JFrame view;

    public JButton btnAddProduct = new JButton("Configure Product");
    public JButton btnAddCustomer = new JButton("Configure Customer");
    public JButton btnAddPurchase = new JButton("Configure Purchase");

  //  public JButton btnUpdateProduct = new JButton("Update Product");
    public MenuUI() {
        this.view = new JFrame();
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System");
        view.setSize(1000,600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");
        title.setFont(title.getFont().deriveFont(24.0f));
        view.getContentPane().add(title);

        JPanel panel = new JPanel((new FlowLayout()));
        panel.add(btnAddProduct);
        panel.add(btnAddCustomer);
        panel.add(btnAddPurchase);
        //panel.add(btnUpdateProduct);
        view.getContentPane().add(panel);

        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AddProductView ui = new AddProductView();
               ui.run();
            }
        });

        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCustomerView addCustomerView = new AddCustomerView();
                addCustomerView.run();
            }
        });

        btnAddPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPurchaseView addPurchaseView = new AddPurchaseView();
                addPurchaseView.run();
            }
        });

     /*   btnUpdateProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateProductUI ui = new UpdateProductUI();
                ui.run();
            }
        });*/
    }
}
