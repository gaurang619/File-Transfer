package filetransfer.in.ac.adit.com.transferfiles;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientClass
{
    Intent intent;
    WifiP2pInfo wifiP2pInfo;
    public ClientClass()
    {
        try {
            wifiP2pInfo = (WifiP2pInfo) intent.getExtras().get("wifiP2pInfo");
            if (wifiP2pInfo.isGroupOwner)
            {
                InetAddress inetAddress = wifiP2pInfo.groupOwnerAddress;

                Socket socket = new Socket(inetAddress, 8888);
                File file = new File(new GetDetails().sendPath);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = socket.getOutputStream();
                byte[] contents;
                long fileLength = file.length();
                long current = 0;

                long start = System.nanoTime();
                while (current != fileLength) {
                    int size = 10000;
                    if (fileLength - current >= size)
                        current += size;
                    else {
                        size = (int) (fileLength - current);
                        current = fileLength;
                    }
                    contents = new byte[size];
                    bis.read(contents, 0, size);
                    os.write(contents);
                    //System.out.print("Sending file ... "+(current*100)/fileLength+"% complete!");
                }

                os.close();
                fis.close();
                bis.close();
                socket.close();
                new GetDetails().sendPath = "";
            }
        }
        catch(Exception e)
            {
                e.printStackTrace();
            }

    }

}
