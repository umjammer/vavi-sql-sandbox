import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * �e�X�g�v���O����
 */
public class Tester {
    Connection conn = null;

    public static void main(String[] args) throws Exception {
        new Tester().testVault();
    }

    public void testVault() throws Exception {
        DriverManager.registerDriver(new vavi.sql.jdbc.Driver());
        conn = DriverManager.getConnection("jdbc:vavi:jdbc:" + "ODBCSOURCE", "USER", "PASS");
        
        testCreateTable();
        
        testInsertByStatement();
        testInsertByPreparedStatement();
        
        testSelectByStatement();
        testSelectByPreparedStatement();
    }

    public void testCreateTable() throws Exception {
        Statement stmt = null;

        stmt = conn.createStatement();
        stmt.execute("DROP TABLE MyTest");
        stmt.close();
        
        PreparedStatement pstmt = null;
        
        pstmt = conn.prepareStatement("CREATE TABLE MyTest (mycode INT NOT NULL,mystring VARCHAR(64) NULL)");
        pstmt.execute();
        pstmt.close();
        
        pstmt = conn.prepareStatement("CREATE UNIQUE INDEX IxMyTest ON MyTest (mycode) WITH PRIMARY");
        pstmt.execute();
        pstmt.close();
    }

    /**
     * SQL�����ߍ��ݕ�����ɂ�� INSERT�� ����� setInt setString�ɂ��
     * INSERT�����e�X�g���܂��B
     * ����́A���ڔԍ���(1,2,...)��setXXX �݂̂ɂ����Ή����Ă��܂���B
     */
    public void testInsertByStatement() throws Exception {
        Statement stmt = null;

        stmt = conn.createStatement();
        stmt.execute("INSERT INTO MyTest VALUES (1, '������')");
        stmt.close();
    }
    
    /**
     * SQL�����ߍ��ݕ�����ɂ�� INSERT�� ����� setInt setString�ɂ��
     * INSERT�����e�X�g���܂��B
     * ����́A���ڔԍ���(1,2,...)��setXXX �݂̂ɂ����Ή����Ă��܂���B
     */
    public void testInsertByPreparedStatement() throws Exception {
        PreparedStatement pstmt = null;
        
        pstmt = conn.prepareStatement("INSERT INTO MyTest VALUES (?,?)");
        pstmt.setInt(1, 2);
        pstmt.setString(2, "�����񂻂�2");
        pstmt.execute();
        pstmt.close();
    }

    /**
     * SELECT�̃e�X�g
     * ResultSet �� getInt() getString() ����������Ă��܂��B
     * ���̂ق��ɁAResultSetMetaData���ꕔ��������Ă��܂��B
     */
    public void testSelectByStatement() throws Exception {
        Statement stmt = null;

        stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM MyTest");
        
        for (int index = 0; rs.next(); index++) {
            System.out.println("��������:" + rs.getInt(1) + ",'" +
                    rs.getString(2) + "'");
        }
        
        rs.close();
        stmt.close();
    }

    /**
     * SELECT�̃e�X�g
     */
    public void testSelectByPreparedStatement() throws Exception {
        PreparedStatement pstmt = null;

        pstmt = conn.prepareStatement("SELECT * FROM MyTest WHERE mycode=? AND mystring=?");
        pstmt.setInt(1, 2);
        pstmt.setString(2, "�����񂻂�2");
        
        ResultSet rs = pstmt.executeQuery();
        
        for (int index = 0; rs.next(); index++) {
            System.out.println("��������:" + rs.getInt(1) + ",'" + rs.getString(2) + "'");
        }
        
        rs.close();
        pstmt.close();
    }
}
