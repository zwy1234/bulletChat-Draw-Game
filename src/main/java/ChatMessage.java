import com.alibaba.fastjson.JSONArray;


import java.util.Objects;

public class ChatMessage extends Message {
    private String userUid; // 用户UID
    private String userName; // 用户名
    private String chatMsg; // 聊天消息

//    private String

    /**
     * 直播用户发送的消息对象
     *
     * @param message 经过解压拆分后的JSON消息数据
     */
    public ChatMessage(String message) {
        super(message);

        if (super.getInfo() != null) {
            JSONArray userInfo = getInfo().getJSONArray(2);

            if (userInfo != null) {
                this.userUid = userInfo.getString(0);
                this.userName = userInfo.getString(1);
                this.chatMsg = getInfo().getString(1);
            }
        }
    }

    public String getUid() {
        return userUid;
    }

    public String getName() {
        return userName;
    }

    public String getMsg() {
        return chatMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChatMessage that = (ChatMessage) o;
        return userUid.equals(that.userUid) && userName.equals(that.userName) && chatMsg.equals(that.chatMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userUid, userName, chatMsg);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "userUid='" + userUid + '\'' +
                ", userName='" + userName + '\'' +
                ", chatMsg='" + chatMsg + '\'' +
                '}';
    }
}
