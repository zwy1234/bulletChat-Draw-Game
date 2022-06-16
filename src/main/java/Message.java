import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class Message {
    private String typeName; // 类型名
    private JSONArray infoArray; // 数据列表

    /**
     * 所有消息对象的父类
     *
     * @param message 经过解压拆分后的JSON消息数据
     * @throws JSONException 屏蔽无效字符串转换JSON的错误
     */
    public Message(String message) throws JSONException {
        JSONObject msg = JSONObject.parseObject(message);

        if (msg.containsKey("cmd") && msg.containsKey("info")) {
            this.typeName = msg.getString("cmd");
            this.infoArray = msg.getJSONArray("info");
        }
    }

    public String getType() {
        return typeName;
    }

    public JSONArray getInfo() {
        return infoArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return typeName.equals(message.typeName) && infoArray.equals(message.infoArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, infoArray);
    }

    @Override
    public String toString() {
        return "Message{" +
                "typeName='" + typeName + '\'' +
                ", infoArray=" + infoArray +
                '}';
    }
}
