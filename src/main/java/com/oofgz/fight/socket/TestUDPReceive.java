package com.oofgz.fight.socket;

import com.oofgz.fight.dto.lombok.UdpInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@Log4j2
@Component
public class TestUDPReceive {

    /**
     * 发送监听端口
     */
    public UdpInfo testUDPReceive() {
        //int defaultPort = 8081;
        int n07Port = 8507;
        DatagramSocket datagramSocket = null;
        try {
            //监视8081端口的内容
            datagramSocket=new DatagramSocket(n07Port);
            byte[] buf=new byte[1024];

            //定义接收数据的数据包
            DatagramPacket datagramPacket = new DatagramPacket(buf, 0, buf.length);
            datagramSocket.receive(datagramPacket);

            //从接收数据包取出数据
            String data = new String(datagramPacket.getData() , 0 ,datagramPacket.getLength());
            System.out.println(data);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            datagramSocket.close();
        }
        UdpInfo udpInfo = new UdpInfo();
        udpInfo.setUdpContent("重启成功！");
        return udpInfo;
    }

    /**
     * N07
     * @return
     */
    public void testUDPReceiveN07() {
        try {
            int n07Port = 8507;
            //创建接收端的Socket服务对象，并且指定端口号
            DatagramSocket datagramSocket = new DatagramSocket(n07Port);
            while(true){
                //创建一个数据包，用于接收数据
                byte[] bys = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(bys, bys.length);

                //接收数据
                datagramSocket.receive(datagramPacket);

                //解析数据
                //获取ip地址
                String ip = datagramPacket.getAddress().getHostAddress();

                //获取数据
                String data = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                System.out.println("from " + ip + " data is " + data);
            }
            //释放资源(服务器一般永远是开着的）
            //ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //datagramSocket.close();
        }
    }


}
