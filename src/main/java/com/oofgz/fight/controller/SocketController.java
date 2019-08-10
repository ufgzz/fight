package com.oofgz.fight.controller;

import com.oofgz.fight.dto.lombok.UdpInfo;
import com.oofgz.fight.socket.TestUDPReceive;
import com.oofgz.fight.socket.TestUDPSend;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Socket")
public class SocketController {

    @Autowired
    TestUDPSend testUDPSend;

    @Autowired
    TestUDPReceive testUDPReceive;

    @ApiOperation(value = "测试Socket UDP Send的用法", notes = "发送Udp数据")
    @ApiImplicitParam(name = "udpInfo", value = "UdpInfo实体", required = true, dataType = "UdpInfo")
    @RequestMapping(value = "/testSendUdp", method = RequestMethod.POST)
    public UdpInfo testSendUdp(@RequestBody UdpInfo udpInfo) {
        //lombokUser.setUserName("zfgican");
        return testUDPSend.testUDPSend(udpInfo);
    }

    @ApiOperation(value = "测试Socket UDP Receive的用法", notes = "接收Udp数据，重启接收服务")
    @RequestMapping(value = "/testReceiveUdp", method = RequestMethod.POST)
    public UdpInfo testReceiveUdp() {
        //lombokUser.setUserName("zfgican");
        new Thread(new Runnable() {
            @Override
            public void run() {
                testUDPReceive.testUDPReceiveN07();
            }
        }).start();

        UdpInfo udpInfo = new UdpInfo();
        udpInfo.setUdpContent("起动UDP广播接收服务成功！");
        return udpInfo;
    }


}
