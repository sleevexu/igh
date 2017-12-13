package util;

public class CRC16 {

    public static int DoCRC16(int data[], int len) {
        int CRC_16 = 0xffff;
        for (int i = 0; i < len; i++) {
            CRC_16 ^= data[i];
            for (int j = 0; j < 8; j++) {
                if ((CRC_16 & 1) == 1) {
                    CRC_16 >>= 1;
                    CRC_16 ^= 0XA001;
                } else {
                    CRC_16 >>= 1;
                }
            }
        }
        return CRC_16;
    }

    public static boolean checkCRC16(byte[] input) {
        int[] data = new int[14];
        for (int i = 0; i < 14; i++) {
            int index = i+3;
            data[i] = input[index] &0xff;
        }
        int crc16_check = (input[17] << 8) &0xff00 | (input[18]&0x00ff);
        int crc16 = DoCRC16(data, data.length);
        if (crc16 == crc16_check) {
            return true;
        } else return false;
    }
}
