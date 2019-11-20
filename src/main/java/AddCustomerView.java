import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AddCustomerView {
    public JFrame view;
    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Update Existing");
    public JButton btnCreate = new JButton("Create New");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);
    public JTextField txtPaymentInfo = new JTextField(20);

    public AddCustomerView() {
        this.view = new JFrame();
        view.setTitle("Add Customer");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));
        String[] labels = {"CustomerID: ", "Name: ", "Address: ", "Phone: ", "Payment Info: "};
        int numPairs = labels.length;

        JPanel panel = new JPanel((new FlowLayout()));
        panel.add(btnLoad);
        panel.add(btnSave);
        panel.add(btnCreate);
        view.getContentPane().add(panel);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("CustomerID "));
        line1.add(txtCustomerID);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Address "));
        line3.add(txtAddress);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Phone "));
        line4.add(txtPhone);

        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(new JLabel("Payment Info "));
        line5.add(txtPaymentInfo);
//Create and populate the panel.
        view.getContentPane().add(line1);
        view.getContentPane().add(line2);
        view.getContentPane().add(line3);
        view.getContentPane().add(line4);
        view.getContentPane().add(line5);


        btnLoad.addActionListener(new AddButtonListener());

        btnSave.addActionListener(new UpdateButtonListener());

        btnCreate.addActionListener(new SaveButtonListener());
    }
    public void run() {
        view.setVisible(true);
    }
    class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
            String id = txtCustomerID.getText();
            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!!");
                return;
            }
            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!!");
                return;
            }
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_CUSTOMER;
                msg.data = Integer.toString(customer.mCustomerID);
                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Customer NOT exists!");
                }
                else {
                    customer = gson.fromJson(msg.data, CustomerModel.class);
                    if (customer == null) {
                        JOptionPane.showMessageDialog(null, "Customer ID DOES NOT exists!");
                    }
                    txtName.setText(customer.mName);
                    txtAddress.setText(customer.mAddress);
                    txtPhone.setText(customer.mPhone);
                    txtPaymentInfo.setText(customer.mPaymentInfo);
                   // JOptionPane.showMessageDialog(null, "Customer added/loaded successfully!" + customer);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            /*
            // product.mProductID = Integer.parseInt(AddProductController.this.view.txtProductID.getText());
            String name = txtName.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!!");
                return;
            }
            customer.mName = name;

            String address = txtAddress.getText();
            if (address.equals("")) {
                JOptionPane.showMessageDialog(null, "Address cannot be empty!!");
                return;
            }
            customer.mAddress = address;

            String phone = txtPhone.getText();
            if (phone.equals("")) {
                JOptionPane.showMessageDialog(null, "Phone cannot be empty!!");
                return;
            }
            customer.mPhone = phone;

            String paymentInfo = txtPaymentInfo.getText();
            if (paymentInfo.equals("")) {
                JOptionPane.showMessageDialog(null, "PaymentInfo cannot be empty!!");
                return;
            }
            customer.mPaymentInfo = paymentInfo;

            JOptionPane.showMessageDialog(null, "You want to add " + customer);
            switch (StoreManager.getInstance().getDataAdapter().saveCustomer(customer)) {
                case SQLiteDataAdapter.PRODUCT_SAVED_FAILED:
                    JOptionPane.showMessageDialog(null, "Customer NOT added successfully! Duplicate Customer ID!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Customer added successfully!" + customer);
            }*/
        }
    }
    class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException a) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }
            String name = txtName.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!!");
                return;
            }
            customer.mName = name;

            String address = txtAddress.getText();
            if (address.equals("")) {
                JOptionPane.showMessageDialog(null, "Address cannot be empty!!");
                return;
            }
            customer.mAddress = address;

            String phone = txtPhone.getText();
            if (phone.equals("")) {
                JOptionPane.showMessageDialog(null, "Phone cannot be empty!!");
                return;
            }
            customer.mPhone = phone;

            String paymentInfo = txtPaymentInfo.getText();
            if (paymentInfo.equals("")) {
                JOptionPane.showMessageDialog(null, "PaymentInfo cannot be empty!!");
                return;
            }
            customer.mPaymentInfo = paymentInfo;

            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.UPDATE_CUSTOMER;
                msg.data = gson.toJson(customer);

                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_OK) {
                    JOptionPane.showMessageDialog(null, "Update Customer Success!");
                }
                /*
                output.println("PUT");
                output.println(product.mProductID);
                output.println(product.mName);
                output.println(product.mPrice);
                output.println(product.mQuantity);*/

            }
            catch (Exception m) {
                m.printStackTrace();
            }
        }
    }
    class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException a) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }
            String name = txtName.getText();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!!");
                return;
            }
            customer.mName = name;

            String address = txtAddress.getText();
            if (address.equals("")) {
                JOptionPane.showMessageDialog(null, "Address cannot be empty!!");
                return;
            }
            customer.mAddress = address;

            String phone = txtPhone.getText();
            if (phone.equals("")) {
                JOptionPane.showMessageDialog(null, "Phone cannot be empty!!");
                return;
            }
            customer.mPhone = phone;

            String paymentInfo = txtPaymentInfo.getText();
            if (paymentInfo.equals("")) {
                JOptionPane.showMessageDialog(null, "PaymentInfo cannot be empty!!");
                return;
            }
            customer.mPaymentInfo = paymentInfo;

            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.SAVE_CUSTOMER;
                msg.data = gson.toJson(customer);

                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_OK) {
                    JOptionPane.showMessageDialog(null, "New Customer Added Success!");
                }
                /*
                output.println("PUT");
                output.println(product.mProductID);
                output.println(product.mName);
                output.println(product.mPrice);
                output.println(product.mQuantity);*/

            }
            catch (Exception m) {
                m.printStackTrace();
            }
        }
    }
}


