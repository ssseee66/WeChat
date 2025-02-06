package WeChat;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {

    public MyObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    //对象流追加写入内容时会重复写入头部信息
    protected void writeStreamHeader() throws IOException {
        //重写读取头部信息方法：不写入头部信息
        super.reset();
    }
}