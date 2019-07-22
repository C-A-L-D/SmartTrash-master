package site.nihaoa.smarttrash.rep;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper {

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://47.100.48.124:3306/dustbin?useSSL=false";
        String username = "root";
        String password = "uAiqwVwjJ8-i";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    public static List<Lese> getLeseList() throws Exception{
        Connection connection = getConn();
        if(null == connection)
            return null;
        Statement statement = connection.createStatement();
        Lese lese;
        ResultSet resultSet = statement.executeQuery("select * from lese");
        List<Lese> leseList = new ArrayList<>();
        while (resultSet.next()){
            lese = new Lese();
            lese.setId(resultSet.getInt("id"));
            lese.setLat(resultSet.getFloat("lat"));
            lese.setLon(resultSet.getFloat("lon"));
            lese.setBaidu_lat(resultSet.getFloat("baidu_lat"));
            lese.setBaidu_lon(resultSet.getFloat("baidu_lon"));
            int full = resultSet.getInt("full_0")+resultSet.getInt("full_1")+
                    resultSet.getInt("full_2")+resultSet.getInt("full_3");
            lese.setFull(full);
            leseList.add(lese);
        }
        resultSet.close();
        statement.close();
        connection.close();
        if(leseList.size() < 1)
            return null;
        return leseList;
    }

}
