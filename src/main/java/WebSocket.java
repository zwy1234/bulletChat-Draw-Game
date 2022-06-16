import com.alibaba.fastjson.JSONException;
//import net.unkown.genshin.logger.PrintLogger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

public class WebSocket extends WebSocketClient {

    /**
     * 通过 WebSocket 获取直播弹幕之类的数据
     *
     * @param serverURI 目标服务器地址 wss://
     */
    public WebSocket(String serverURI) {
        super(URI.create(serverURI));
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("已成功连接B站直播间!");
    }

    @Override
    public void onMessage(ByteBuffer message) {
        try {
            MessageHandle.message(message);
        } catch (DataFormatException e) {
            e.printStackTrace();
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("已断开B站直播间的连接!");
        new Thread(() -> {
            try {
                reconnectBlocking(); // 自动重连
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onMessage(String message) {
    }

    public static void main(String[] args) throws InterruptedException {

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
}
