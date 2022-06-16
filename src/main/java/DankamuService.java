import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class DankamuService implements Runnable{
    private final String url, clientHead, clientBody, heartByte;
    private WebSocket client;

    /**
     * 创建一个获取弹幕的服务
     * @param roomId B站房间号
     */
    public DankamuService(String roomId) {
        this.url = "wss://broadcastlv.chat.bilibili.com:2245/sub";
        this.clientBody = "{\"uid\":0,\"roomid\":{roomId},\"protover\":1,\"platform\":\"web\",\"clientver\":\"1.5.10.1\",\"type\":2}".replace("{roomId}", roomId);
        this.clientHead = "000000{replce}001000010000000700000001".replace("{replce}", Integer.toHexString(clientBody.getBytes().length + 16));
        this.heartByte = "00000010001000010000000200000001";
    }

    public void run() {
        // 建立连接
        client = new WebSocket(url);
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 发送连接参数
        byte[] head = hexToByteArray(clientHead);
        byte[] body = clientBody.getBytes(StandardCharsets.UTF_8);
        byte[] requestCode = byteMerger(head, body);
        client.send(requestCode);

        runHeartTimer(); // 定时发送心跳包
    }

    /**
     * 定时发送心跳包
     */
    private void runHeartTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!client.isClosed()) {
                    client.send(hexToByteArray(heartByte));
                }
            }
        }, 10 * 1000L, 10 * 1000L);
    }

    /**
     * @param hexStr Hex 字符串
     * @return byte[]
     */
    private byte[] hexToByteArray(String hexStr) {
        if (hexStr.length() % 2 == 1) {
            hexStr = "0" + hexStr;
        }

        int hexlen = hexStr.length();
        byte[] result = new byte[(hexlen / 2)];

        for (int i = 0, j = 0; i < hexlen; i += 2, j++) {
            result[j] = (byte) Integer.parseInt(hexStr.substring(i, i + 2), 16);
        }
        return result;
    }

    /**
     * @param byteL left
     * @param byteR right
     * @return left + right
     */
    private byte[] byteMerger(byte[] byteL, byte[] byteR) {
        byte[] byteArr = new byte[byteL.length + byteR.length];
        System.arraycopy(byteL, 0, byteArr, 0, byteL.length);
        System.arraycopy(byteR, 0, byteArr, byteL.length, byteR.length);
        return byteArr;
    }
}
