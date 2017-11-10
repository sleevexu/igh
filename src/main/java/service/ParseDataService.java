package service;

import util.ByteUtil;

import java.util.ArrayList;

public class ParseDataService {

    public static void parseData(ArrayList<Byte> address, byte[] data) {
        System.out.println("address size"+address.size());
        for (int j = 0;j<address.size();++j){
            System.out.println("address:"+address.get(j));
        }
        for (int i =0;i<data.length;++i){
            System.out.println(data[i]&0xff);
        }
        int dataType = data[0];
        if (dataType == 0x30) {
            System.out.println("Parse Data of Type 30");
        }
        if (dataType == 0x01) {
            System.out.println("Parse Data of Type 1");
            RefreshNodeService.saveNodeAddress(address, data[1]);
            System.out.println(ByteUtil.bytesToHexString(data));
        }
    }
}
