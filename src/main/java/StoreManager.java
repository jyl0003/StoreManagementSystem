import javax.swing.*;

public class StoreManager extends JFrame{
    public static final String DBMS_SQ_LITE = "SQLite";
    public static final String DB_FILE = "C:\\Users\\jlee9\\StoreManagementSystem\\Activity10.db";

    IDataAdapter adapter = null;
    private static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            String dbfile = DB_FILE;
           /* JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                dbfile = fc.getSelectedFile().getAbsolutePath();
            }*/
            instance = new StoreManager(DBMS_SQ_LITE, dbfile);
        }
        return instance;
    }

    private StoreManager(String dbms, String dbFile) {
        if (dbms.equals("Oracle")) {
            adapter = new OracleDataAdapter();
        }
        else
        if (dbms.equals("SQLite"))
            adapter = new SQLiteDataAdapter();

        adapter.connect(dbFile);
    }

    public IDataAdapter getDataAdapter() {
        return adapter;
    }

    public void setDataAdapter(IDataAdapter a) {
        adapter = a;
    }
    public void run() {
        MenuUI ui = new MenuUI();
        ui.view.setVisible(true);
    }
    public static void main(String[] args) throws ClassNotFoundException {
        StoreManager.getInstance().run();
    }
}
