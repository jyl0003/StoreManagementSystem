public class PurchaseModel {
    public int mPurchaseId;
    public int mProductID;
    public int mCustomerID;
    public int mQuantity;

    public String toString() {
        StringBuilder purchaseSB = new StringBuilder("(");
        purchaseSB.append(mPurchaseId).append(",");
        purchaseSB.append(mProductID).append(",");
        purchaseSB.append(mCustomerID).append(",");
        purchaseSB.append(mQuantity).append(")");
        return purchaseSB.toString();
    }
}
