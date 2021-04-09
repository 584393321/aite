package at.smarthome;

public class FileUtil {
    static {
        System.loadLibrary("atsmarthome");
    }

    public static native int checkNum(String fileName);

    /**
     * 把16进制的字符串去空格后转换成byte数组。如"37   5a"转成[0x37][0x5A]
     *
     * @param s 整数和数字的16进制都行
     * @return
     */
    public static byte[] string2bytes(String s) {
        String ss = s.replace(" ", "");
        int string_len = ss.length();
        int len = string_len / 2;
        if (string_len % 2 == 1) {
            ss = "0" + ss;
            string_len++;
            len++;
        }
        byte[] a = new byte[len];
        for (int i = 0; i < len; i++) {
            a[i] = (byte) Integer.parseInt(ss.substring(2 * i, 2 * i + 2), 16);
        }
        return a;
    }

    public static byte[] getByte(int checkNum, int length) {
        byte[] bytes = new byte[12];
        byte[] checkb = string2bytes(Integer.toHexString(checkNum));
        byte[] lengthb = string2bytes(Integer.toHexString(length));
        int checkb_len = checkb.length;
        int lengthb_len = lengthb.length;
        for (int i = 0; i < checkb_len; i++) {
            bytes[i] = checkb[checkb_len - i - 1];
        }
        for (int i = 0; i < lengthb_len; i++) {
            bytes[i + 8] = lengthb[lengthb_len - i - 1];
        }
        return bytes;
    }
}
