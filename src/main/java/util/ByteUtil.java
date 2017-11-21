package util;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class ByteUtil {

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static int byteToInt(byte[] a){
        System.out.println((int)a[0]);  //这里输出的是-49
        System.out.println((int)(a[0]&0xff));//这里输出是207
        int rs0=(int)((a[0]&0xff)<<0*8);
        int rs1=(int)((a[1]&0xff)<<1*8);
        int rs2=(int)((a[2]&0xff)<<2*8);
        int rs3=(int)((a[3]&0xff)<<3*8);
        int result=rs0+rs1+rs2+rs3;
        return result;
    }
}
