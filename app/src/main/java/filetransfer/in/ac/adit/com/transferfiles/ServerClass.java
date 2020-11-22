package filetransfer.in.ac.adit.com.transferfiles;

import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClass
{
    String receivePath="";
    public ServerClass()
    {
        try
        {
            GetDetails getDetails = new GetDetails();
            ServerSocket serversocket = new ServerSocket(8888);
            Socket socket = serversocket.accept();
            InputStream inputStream = socket.getInputStream();
            receivePath = receivePath + getDetails.path + getDetails.fileName;
            FileOutputStream fos =new FileOutputStream(receivePath);
            int read;
            while((read=inputStream.read())!=-1)
            {
                fos.write(read);
            }
            inputStream.close();
            fos.close();
            serversocket.close();
            socket.close();

            getDetails.fileName = "";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
