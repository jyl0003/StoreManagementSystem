public class ProductModel {
    public int mProductID;
    public String mName;
    public double mPrice, mQuantity;
    //public int code = 1;

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(mProductID).append(",");
        sb.append("\"").append(mName).append("\"").append(",");
        sb.append(mPrice).append(",");
        sb.append(mQuantity).append(")");
        return sb.toString();
    }
}
