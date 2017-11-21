package service;

import java.util.ArrayList;

public class RefreshNodeService {

    public static void saveNodeAddress(ArrayList<Byte> nodeAddress, byte newAddress){
        if (nodeAddress.size()==0){
            nodeAddress.add(newAddress);
            System.out.println("Add Node Address:"+newAddress);
        }
        for (int i =0;i<nodeAddress.size();i++){
            if (!nodeAddress.contains(newAddress)){
                System.out.println("Add Node Address: "+newAddress);
                nodeAddress.add(newAddress);
                break;
            }
        }
    }
}
