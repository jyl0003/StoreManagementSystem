public class OracleDataAdapter implements IDataAdapter {
    public int connect(String dbFile) {
        //...
        return OPEN_CONNECTION_OK;
    }

    public int disconnect() {
        // ...
        return CLOSE_CONNECTION_OK;

    }

    public ProductModel loadProduct(int id) {
        return null;
    }
    public String loadProductName(int id) {return null;}
    public int saveProduct(ProductModel model) {
        return PRODUCT_SAVED_OK;
    }
    public CustomerModel loadCustomer(int id) {
        return null;
    }
    public String loadCustomerID_NAME(int id) {return null;}
    public int saveCustomer(CustomerModel model) {
        return CUSTOMER_SAVED_OK;
    }
    public PurchaseModel loadPurchase(int id) {return null; }
    public int savePurchase(PurchaseModel purchaseModel) {return 0; }
}
