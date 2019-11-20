import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AddProductView {
    public JFrame view;

    public JButton btnLoad = new JButton("Load Product");
    public JButton btnSave = new JButton("Update Product");
    public JButton btnCreate = new JButton("New Product");
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    public AddProductView() {
        this.view = new JFrame();
        view.setTitle("Update Product Information");
        view.setSize(600,400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));
       // String[] labels = {"ProductId: ", "Name: ", "Price: ", "Quantity: "};
        //int numPairs = labels.length;

        JPanel panel = new JPanel((new FlowLayout()));
        panel.add(btnLoad);
        panel.add(btnSave);
        panel.add(btnCreate);
        view.getContentPane().add(panel);

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


        btnLoad.addActionListener(new LoadButtonListener());
        btnSave.addActionListener(new UpdateListener());
        btnCreate.addActionListener(new SaveButtonListener());

    }
    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            Gson gson = new Gson();
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

            //DO CLIENT/SERBER SIDE
            // product.mProductID = Integer.parseInt(AddProductController.this.view.txtProductID.getText());
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_PRODUCT;
                msg.data = Integer.toString(product.mProductID);
                output.println(gson.toJson(msg));

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Product NOT exists!");
                }
                else {
                    product = gson.fromJson(msg.data, ProductModel.class);
                    txtName.setText(product.mName);
                    txtPrice.setText(Double.toString(product.mPrice));
                    txtQuantity.setText(Double.toString(product.mQuantity));
                }
                /*output.println("GET");
                output.println(product.mProductID);
*/
                /*product.mName = input.nextLine();

                if (product.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Product NOT exists!");
                    return;
                }

                txtName.setText(product.mName);

                product.mPrice = input.nextDouble();
                txtPrice.setText(Double.toString(product.mPrice));

                product.mQuantity = input.nextDouble();
                txtQuantity.setText(Double.toString(product.mQuantity));*/

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class UpdateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            Gson gson = new Gson();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty!");
                return;
            }

            product.mName = name;

            String price = txtPrice.getText();
            try {
                product.mPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is invalid!");
                return;
            }

            String quant = txtQuantity.getText();
            try {
                product.mQuantity = Double.parseDouble(quant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            //DO CLIENT/SERBER SIDE
            // product.mProductID = Integer.parseInt(AddProductController.this.view.txtProductID.getText());
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.UPDATE_PRODUCT;
                msg.data = gson.toJson(product);

                output.println(gson.toJson(msg));
                msg = gson.fromJson(input.nextLine(), MessageModel.class);
                /*
                output.println("PUT");
                output.println(product.mProductID);
                output.println(product.mName);
                output.println(product.mPrice);
                output.println(product.mQuantity);*/
                if (msg.code == MessageModel.OPERATION_OK) {
                    JOptionPane.showMessageDialog(null, "Update Product Success!");
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            Gson gson = new Gson();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty!");
                return;
            }

            product.mName = name;

            String price = txtPrice.getText();
            try {
                product.mPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is invalid!");
                return;
            }

            String quant = txtQuantity.getText();
            try {
                product.mQuantity = Double.parseDouble(quant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            //DO CLIENT/SERBER SIDE
            // product.mProductID = Integer.parseInt(AddProductController.this.view.txtProductID.getText());
            try {
                Socket socket = new Socket("localhost", 1000);
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.SAVE_PRODUCT;
                msg.data = gson.toJson(product);

                output.println(gson.toJson(msg));
                msg = gson.fromJson(input.nextLine(), MessageModel.class);
                /*
                output.println("PUT");
                output.println(product.mProductID);
                output.println(product.mName);
                output.println(product.mPrice);
                output.println(product.mQuantity);*/
                if (msg.code == MessageModel.OPERATION_OK) {
                    JOptionPane.showMessageDialog(null, "New Product Added Success!");
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


