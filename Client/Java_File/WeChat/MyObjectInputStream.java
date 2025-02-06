package WeChat;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class MyObjectInputStream extends ObjectInputStream {
    public MyObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected void readStreamHeader() {
        //重写读取头部信息方法：什么也不做
    }
}
