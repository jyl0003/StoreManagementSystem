import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OldAddProductView {
    public JFrame view;

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    public OldAddProductView() {
        this.view = new JFrame();
        view.setTitle("Add Product");
        view.setSize(600,400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));
        String[] labels = {"ProductId: ", "Name: ", "Price: ", "Quantity: "};
        //int numPairs = labels.length;

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("ProductID "));
        line1.add(txtProductID);
        view.getContentPane().add(line1);
        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);
        view.getContentPane().add(line2);
        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Price "));
        line3.add(txtPrice);
        view.getContentPane().add(line3);
        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);

        JPanel panel = new JPanel((new FlowLayout()));
        panel.add(btnLoad);
        panel.add(btnSave);
        view.getContentPane().add(panel);

        btnLoad.addActionListener(new AddButtonListener());

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "You click on Cancel button!!!");
            }
        });
    }
    public void run() {
        view.setVisible(true);
    }

    class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            String id = txtProductID.getText();
            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!!");
                return;
            }
            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!!");
                return;
            }
            // product.mProductID = Integer.parseInt(AddProductController.this.view.txtProductID.getText());
            String name = txtName.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!!");
                return;
            }
            product.mName = name;

            String price = txtPrice.getText();
            try {
                product.mPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is Invalid!!");
                return;
            }

            String quantity = txtQuantity.getText();
            try {
                product.mQuantity = Integer.parseInt(quantity);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is Invalid!!");
                return;
            }
            JOptionPane.showMessageDialog(null, "You want to add " + product);
            switch (StoreManager.getInstance().getDataAdapter().saveProduct(product)) {
                case SQLiteDataAdapter.PRODUCT_SAVED_FAILED:
                    JOptionPane.showMessageDialog(null, "Product NOT added successfully! Duplicate product ID!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Product added successfully!" + product);
            }
        }
    }

}


