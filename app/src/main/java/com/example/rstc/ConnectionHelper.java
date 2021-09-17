package com.example.rstc;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {

    String ip, db, un, ps;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public Connection connectionClass(){
         ip = "103.14.97.220";
         db = "RSRTC";
         un = "MtechGPRS";
         ps = "Mtech@GPRS$";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String ConnectionURL = null;

        try {

          //  Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + "/" + db + ";user=" + un + ";password=" + ps + ";";
            connection = DriverManager.getConnection(ConnectionURL);

        } catch (Exception e) {
            Log.e("error here 1 : ", e.getMessage());
        }
        return connection;
    }
}
