package filetransfer.in.ac.adit.com.transferfiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Index extends AppCompatActivity {

    Typeface typeface;
    Intent intent;

    ListView listView;
    Button button1,button2,button3,button4,button5;
    TextView connectionStatus,textView;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiManager wifiManager;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String deviceNameArray[];
    WifiP2pDevice deviceArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/rtm.ttf");

        initialWork();
        exqListener();
    }

    private void initialWork()
    {
        textView = (TextView)findViewById(R.id.txt1);
        textView.setTypeface(typeface);
        textView.setTextColor(Color.parseColor("#000000"));

        listView = (ListView)findViewById(R.id.listView);
        connectionStatus = (TextView)findViewById(R.id.connectionStatus);
        connectionStatus.setVisibility(View.INVISIBLE);

        button1 = (Button)findViewById(R.id.wifiButton);
        button1.setBackgroundColor(Color.parseColor("#FF0000"));
        button1.setTypeface(typeface);

        button2 = (Button)findViewById(R.id.discoverButton);
        button2.setTypeface(typeface);
        button2.setBackgroundColor(Color.parseColor("#000000"));

        button3 = (Button)findViewById(R.id.chooseFile);
        button3.setTypeface(typeface);
        button3.setBackgroundColor(Color.parseColor("#000000"));
        button3.setVisibility(View.INVISIBLE);

        button4 = (Button)findViewById(R.id.sendButton);
        button4.setTypeface(typeface);
        button4.setBackgroundColor(Color.parseColor("#000000"));
        button4.setVisibility(View.INVISIBLE);

        button5 = (Button)findViewById(R.id.receiveButton);
        button5.setTypeface(typeface);
        button5.setBackgroundColor(Color.parseColor("#000000"));
        button5.setVisibility(View.INVISIBLE);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this,getMainLooper(),null);

        mReceiver = new WifiDirectBroadcastReceiver(mManager,mChannel,this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];

                int index = 0;
                for(WifiP2pDevice device : peerList.getDeviceList())
                {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);
            }

            if(peers.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Device Found!!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    private void exqListener()
    {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wifiManager.isWifiEnabled())
                {
                    wifiManager.setWifiEnabled(true);
                    button1.setText("Wifi On");
                    button1.setBackgroundColor(Color.parseColor("#008B00"));
                    Toast.makeText(getApplicationContext(), "Wifi Is Enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    wifiManager.setWifiEnabled(false);
                    button1.setText("Wifi Off");
                    button1.setBackgroundColor(Color.parseColor("#FF0000"));
                    Toast.makeText(getApplicationContext(), "Wifi Is Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Discovery Started!!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Discovery Failed!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 10);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new GetDetails().count=1;
                new ClientClass();
                Toast.makeText(getApplicationContext(),"Waiting For The Receiver To Press Receive Button",Toast.LENGTH_SHORT).show();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new ServerClass();

                   // new GetDetails().count=0;
                    Toast.makeText(getApplicationContext(),"File Received Successfully",Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device = deviceArray[i];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                mManager.connect(mChannel,config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Connected To "+device.deviceName,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Failed To Connect",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 10:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();

                    Toast.makeText(getApplicationContext(),"File Selected Successfully",Toast.LENGTH_SHORT).show();
                    new GetDetails().sendPath=PathHolder;
                    button3.setBackgroundColor(Color.parseColor("#008B00"));

                }
                break;

        }
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                connectionStatus.setVisibility(View.VISIBLE);
                connectionStatus.setText("Connection Status : Server");
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);

            }
            else if(wifiP2pInfo.groupFormed)
            {
                connectionStatus.setVisibility(View.VISIBLE);
                connectionStatus.setText("Connection Status : Client");
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected  void onPause()
    {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


}
