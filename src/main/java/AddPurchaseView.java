import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

public class AddPurchaseView {

    public static final double TAX_RATE = .10;

    public JFrame view;

    //Buttons that allow configuration of purchases
    public JButton btnLoad = new JButton("Load");
    public JButton update = new JButton("Update Existing");
    public JButton save = new JButton("Add New Purchase");

    //TextFields
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    public JLabel labPrice = new JLabel("Product Price: ");
    public JLabel labDate = new JLabel("Date of Purchase: ");

    public JLabel labCustomerName = new JLabel("Customer Name: ");
    public JLabel labProductName = new JLabel("Product Name: ");

    public JLabel labCost = new JLabel("Cost: ");
    public JLabel labTax = new JLabel("Tax: ");
    public JLabel labTotalCost = new JLabel("Total Cost: ");

    //Constructor that allows population of UI
    public AddPurchaseView() {
        this.view = new JFrame();
        view.setTitle("Add Purchase");
        view.setSize(600,400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));
        String[] labels = {"PurchaseID: ", "ProductID: ", "CustomerID: ", "Quantity: "};
        //int numPairs = labels.length;

        JPanel panel = new JPanel((new FlowLayout()));
        panel.add(btnLoad);
        panel.add(update);
        panel.add(save);
        view.getContentPane().add(panel);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("PurchaseID "));
        line1.add(txtPurchaseID);
        line1.add(labDate);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("ProductID "));
        line2.add(txtProductID);
        line2.add(labProductName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("CustomerID "));
        line3.add(txtCustomerID);
        line3.add(labCustomerName);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        line4.add(labPrice);
        view.getContentPane().add(line4);

        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(labCost);
        line5.add(labTax);
        line5.add(labTotalCost);
        view.getContentPane().add(line5);

        /*JPanel panel = new JPanel((new FlowLayout()));
        panel.add(button);
        panel.add(cancel);
        view.getContentPane().add(panel);*/

        save.addActionListener(new SaveButtonListener());

        btnLoad.addActionListener(new LoadActionListener());

        update.addActionListener(new UpdateButtonListener());
    }
    public void run() {
        labDate.setText("Date of purchase: " + java.time.LocalDateTime.now());
        view.setVisible(true);
    }

    /*public static int okcancel(String theMessage) {
        return JOptionPane.showConfirmDialog((Component) null, theMessage,
                "alert", JOptionPane.OK_CANCEL_OPTION);
    }*/

    //Load button class and function
    class LoadActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PurchaseModel purchase = new PurchaseModel();
            Gson gson = new Gson();
            CustomerModel customerModel;
            ProductModel productModel;
            String purchaseID = txtPurchaseID.getText();
            if (purchaseID.equals("")) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }
            try {
                purchase.mPurchaseId = Integer.parseInt(purchaseID);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.LOAD_PURCHASE;
                msg.data = Integer.toString(purchase.mPurchaseId);
                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);
                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Purchase DOES NOT exists!");
                }
                else {
                    purchase = gson.fromJson(msg.data, PurchaseModel.class);
                    customerModel = gson.fromJson(msg.customerData, CustomerModel.class);
                    productModel = gson.fromJson(msg.productData, ProductModel.class);
                   // StoreManager.getInstance().getDataAdapter().disconnect();
                 //   if (productModel.mName == null || customerModel.mName == null) {
                   //     JOptionPane.showMessageDialog(null, "Invalid CustomerID or ProductID");
                  //  } else {
                        txtProductID.setText(Integer.toString(purchase.mProductID));
                        txtCustomerID.setText(Integer.toString(purchase.mCustomerID));
                        txtQuantity.setText(Integer.toString(purchase.mQuantity));
                        DecimalFormat df = new DecimalFormat("$###,###,###.00");
                        labCustomerName.setText("Customer Name: " + customerModel.mName);
                        labProductName.setText("Product Name: " + productModel.mName);
                        labPrice.setText("Price: " + df.format(productModel.mPrice));
                        double costNoTax = productModel.mPrice * purchase.mQuantity;
                        double tax = costNoTax * TAX_RATE;
                        labTax.setText("Tax: " + df.format(tax));
                        labCost.setText("Cost: " + df.format(productModel.mPrice * purchase.mQuantity));
                        // double costNoTax = productModel.mPrice * purchase.mQuantity;
                        double totalCost = costNoTax + costNoTax * TAX_RATE;
                        labTotalCost.setText("Total Cost: " + df.format(totalCost));
                  // }
                    /*txtName.setText(customer.mName);
                    txtAddress.setText(customer.mAddress);
                    txtPhone.setText(customer.mPhone);
                    txtPaymentInfo.setText(customer.mPaymentInfo);*/
                    // JOptionPane.showMessageDialog(null, "Customer added/loaded successfully!" + customer);
                }
            }
            catch (Exception d) {
                d.printStackTrace();
            }

        }
    }

    //Update button listener
    class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PurchaseModel purchase = new PurchaseModel();
            Gson gson = new Gson();
            CustomerModel customerModel;
            ProductModel productModel;
            String purchaseID = txtPurchaseID.getText();
            if (purchaseID.equals("")) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }
            try {
                purchase.mPurchaseId = Integer.parseInt(purchaseID);
            } catch (NumberFormatException a) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }

            String productID = txtProductID.getText();
            if (productID.equals("")) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be empty!!");
                return;
            }
            purchase.mProductID = Integer.parseInt(productID);
            // labProductName.setText(StoreManager.getInstance().getDataAdapter().loadProductName(Integer.parseInt(productID)));
            String customerID = txtCustomerID.getText();
            if (customerID.equals("")) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be empty!!");
                return;
            }
            purchase.mCustomerID = Integer.parseInt(customerID);
            //labCustomerName.setText(StoreManager.getInstance().getDataAdapter().loadCustomerID_NAME(Integer.parseInt(customerID)));
            String purchaseQuantity = txtQuantity.getText();
            try {
                purchase.mQuantity = Integer.parseInt(purchaseQuantity);
            } catch (NumberFormatException el) {
                JOptionPane.showMessageDialog(null, "Quantity is Invalid!!");
                return;
            }
            if (purchase.mQuantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity is Invalid!!");
            }
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.UPDATE_PURCHASE;
                msg.data = gson.toJson(purchase);

                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);
                //purchase = gson.fromJson(msg.data, PurchaseModel.class);
                if (msg.code==MessageModel.OPERATION_OK) {

                   customerModel = gson.fromJson(msg.customerData, CustomerModel.class);
                    productModel = gson.fromJson(msg.productData, ProductModel.class);
                   // StoreManager.getInstance().getDataAdapter().disconnect();
                   // if (productModel.mName == null || customerModel.mName == null) {
                        //JOptionPane.showMessageDialog(null, "Invalid CustomerID or ProductID");
                   //} else {
                        DecimalFormat df = new DecimalFormat("$###,###,###.00");
                        labCustomerName.setText("Customer Name: " + customerModel.mName);
                        labProductName.setText("Product Name: " + productModel.mName);
                        labPrice.setText("Price: " + df.format(productModel.mPrice));
                        double costNoTax = productModel.mPrice * purchase.mQuantity;
                        double tax = costNoTax * TAX_RATE;
                        labTax.setText("Tax: " + df.format(tax));
                        labCost.setText("Cost: " + df.format(productModel.mPrice * purchase.mQuantity));
                        // double costNoTax = productModel.mPrice * purchase.mQuantity;
                        double totalCost = costNoTax + costNoTax * TAX_RATE;
                        labTotalCost.setText("Total Cost: " + df.format(totalCost));
                   // }
                    JOptionPane.showMessageDialog(null, "Purchase Updated Successfully");
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

    //Save button listener
    class SaveButtonListener implements ActionListener {

        public void printReceipt(PurchaseModel purchaseModel, CustomerModel customerModel, ProductModel productModel) {
            JOptionPane.showMessageDialog(null, "Date: " + java.time.LocalDateTime.now()
                                                                + "\nPurchaseID: " + purchaseModel.mPurchaseId
                                                                + "\nCustomerID: " + customerModel.mCustomerID +
                                                                "       Customer Name: " + customerModel.mName
                                                                + "\nProductID: " + productModel.mProductID
                                                                + "     Product Name: " + productModel.mName
                                                                + "\n" + labCost.getText() + "       " + labTax.getText()
                                                                + "\n" + labTotalCost.getText(), "Receipt", JOptionPane.PLAIN_MESSAGE);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PurchaseModel purchase = new PurchaseModel();
            Gson gson = new Gson();
            CustomerModel customerModel;
            ProductModel productModel;
            String purchaseID = txtPurchaseID.getText();
            if (purchaseID.equals("")) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }
            try {
                purchase.mPurchaseId = Integer.parseInt(purchaseID);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!!");
                return;
            }

            String productID = txtProductID.getText();
            if (productID.equals("")) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be empty!!");
                return;
            }
            purchase.mProductID = Integer.parseInt(productID);
           // labProductName.setText(StoreManager.getInstance().getDataAdapter().loadProductName(Integer.parseInt(productID)));
            String customerID = txtCustomerID.getText();
            if (customerID.equals("")) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be empty!!");
                return;
            }
            purchase.mCustomerID = Integer.parseInt(customerID);
            //labCustomerName.setText(StoreManager.getInstance().getDataAdapter().loadCustomerID_NAME(Integer.parseInt(customerID)));
            String purchaseQuantity = txtQuantity.getText();
            try {
                purchase.mQuantity = Integer.parseInt(purchaseQuantity);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is Invalid!!");
                return;
            }
            if (purchase.mQuantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity is Invalid!!");
            }
            else {
                try {
                    Socket socket = new Socket("localhost", 1000);
                    Scanner input = new Scanner(socket.getInputStream());
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                    MessageModel msg = new MessageModel();
                    msg.code = MessageModel.SAVE_PURCHASE;
                    msg.data = gson.toJson(purchase);

                    output.println(gson.toJson(msg));

                    msg = gson.fromJson(input.nextLine(), MessageModel.class);

                    if(msg.code == MessageModel.OPERATION_OK) {
                        customerModel = gson.fromJson(msg.customerData, CustomerModel.class);
                        productModel = gson.fromJson(msg.productData, ProductModel.class);
                        StoreManager.getInstance().getDataAdapter().disconnect();
                        if (productModel.mName == null || customerModel.mName == null) {
                            JOptionPane.showMessageDialog(null, "Invalid CustomerID or ProductID");
                        } else {
                            DecimalFormat df = new DecimalFormat("$###,###,###.00");
                            labCustomerName.setText("Customer Name: " + customerModel.mName);
                            labProductName.setText("Product Name: " + productModel.mName);
                            labPrice.setText("Price: " + df.format(productModel.mPrice));
                            double costNoTax = productModel.mPrice * purchase.mQuantity;
                            double tax = costNoTax * TAX_RATE;
                            labTax.setText("Tax: " + df.format(tax));
                            labCost.setText("Cost: " + df.format(productModel.mPrice * purchase.mQuantity));
                            // double costNoTax = productModel.mPrice * purchase.mQuantity;
                            double totalCost = costNoTax + costNoTax * TAX_RATE;
                            labTotalCost.setText("Total Cost: " + df.format(totalCost));
                            //JOptionPane.showMessageDialog(null, "You want to add " + purchase + "?");
                           // int number = okcancel("Are you sure?");
                            //if (number == 0) {
                               // int result = StoreManager.getInstance().getDataAdapter().savePurchase(purchase);
                                    JOptionPane.showMessageDialog(null, "Purchase added successfully!" + purchase);
                                    printReceipt(purchase, customerModel, productModel);

                        }
                    }

                 }
                catch (Exception m) {
                    m.printStackTrace();
                }

            }
        }
        //public void printReceipt(purchase)
    }

}