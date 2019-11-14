public interface IDataAdapter {
    public static final int PRODUCT_SAVED_OK = 1;
    public static final int PRODUCT_SAVED_FAILED = 0;
    public  static final int CUSTOMER_SAVED_OK = 1;
    public  static final int CUSTOMER_SAVED_FAILED = 0;
    public  static final int PURCHASE_SAVED_OK = 1;
    public  static final int PURCHASE_SAVED_FAILED = 0;
    public static final int OPEN_CONNECTION_OK = 100;
    public static final int OPEN_CONNECTION_FAILED = 101;
    public static final int CLOSE_CONNECTION_OK = 100;
    public static final int CLOSE_CONNECTION_FAILED = 101;
    public int connect(String dbFile);
    public int disconnect();

    public ProductModel loadProduct(int id);
    public  String loadProductName(int id);
    public int saveProduct(ProductModel model);

    public CustomerModel loadCustomer(int id);
    public int saveCustomer(CustomerModel model);
    public String loadCustomerID_NAME(int id);
    public PurchaseModel loadPurchase(int id);
    public int savePurchase(PurchaseModel model);
    //public int loadPurchase(int id);
    //public int savePurchase(PurchaseModel model);
}
