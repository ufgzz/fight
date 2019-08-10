package com.oofgz.fight.socket;

import com.oofgz.fight.dto.lombok.UdpInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Log4j2
@Component
public class TestUDPSend {

    /**
     * 发送UDP广播
     * @param udpInfo
     */
    public UdpInfo testUDPSend(UdpInfo udpInfo) {
        int n07Port = 8507;

        String data = "hello UDP";
        if (!StringUtils.isEmpty(udpInfo.getUdpContent())) {
            data = udpInfo.getUdpContent();
        }
        DatagramSocket datagramSocket = null;
        try {
            //实例化套接字，并指定发送端口
            datagramSocket=new DatagramSocket(8089);
            //指定数据目的地的地址，以及目标端口
            InetAddress destination=InetAddress.getByName("localhost");
            DatagramPacket datagramPacket= new DatagramPacket(data.getBytes(), data.getBytes().length,destination,n07Port);
            //发送数据
            datagramSocket.send(datagramPacket);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            datagramSocket.close();
        }
        udpInfo.setUdpContent("Hello UDP：" + udpInfo.getUdpContent());
        return udpInfo;
    }
}
