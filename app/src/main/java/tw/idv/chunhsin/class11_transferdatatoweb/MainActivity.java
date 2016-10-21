package tw.idv.chunhsin.class11_transferdatatoweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText id,name,phone;
    TextView response;
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText)findViewById(R.id.Id);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        btnSend = (Button)findViewById(R.id.btnSend);
        response = (TextView)findViewById(R.id.response);
    }

    public void OnClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendData();
            }
        }).start();
    }

    void sendData(){
        String strId = id.getText().toString();
        String strName = name.getText().toString();
        String strPhone = phone.getText().toString();
        String strUrl = "http://172.16.2.6:8080/myweb/mydb.jsp?Id="+strId+"&name="+strName+"&phone="+strPhone;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            //謮取網站的回應 response
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line="";

            while((line=br.readLine())!=null){
                sb.append(line).append("\n");
            }

            final String text=sb.toString();
            response.post(new Runnable() {
                @Override
                public void run() {
                    response.setText(text);
                }
            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
