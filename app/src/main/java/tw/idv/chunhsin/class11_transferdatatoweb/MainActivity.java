package tw.idv.chunhsin.class11_transferdatatoweb;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText id,name,phone;
    //TextView response;
    ListView listView;
    Button btnSend;
    Handler handler;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText)findViewById(R.id.Id);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        btnSend = (Button)findViewById(R.id.btnSend);
        listView = (ListView)findViewById(R.id.listView);
        //response = (TextView)findViewById(R.id.response);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listView.setAdapter(simpleAdapter);
            }
        };
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
        String strUrl = "http://10.0.2.2/yii2-dev/web/user/list";

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


            ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

            JSONObject jo=new JSONObject(sb.toString());
            JSONArray ja=jo.getJSONArray("data");
            for(int i=0;i<ja.length();i++){
                HashMap<String,String> items=new HashMap<>();
                JSONArray jarows=ja.getJSONArray(i);
                items.put("Id",jarows.getString(0));
                items.put("Name",jarows.getString(1));
                items.put("Phone",jarows.getString(2));
                items.put("IP",jarows.getString(3));

                arrayList.add(items);
            }
            simpleAdapter=new SimpleAdapter(
                    this,
                    arrayList,
                    R.layout.list_items,
                    new String[]{"Id","Name","Phone","IP"},
                    new int[]{R.id.textView4,R.id.textView5,R.id.textView6,R.id.textView7}
            );
            handler.sendEmptyMessage(0);
            //listView.setAdapter(simpleAdapter);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
