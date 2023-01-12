package xyz.qinfengge.rsscode;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.qinfengge.rsscode.entity.Aria2Option;
import xyz.qinfengge.rsscode.utils.Aria2Util;
import xyz.qinfengge.rsscode.utils.RssHandelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lza
 * @Date 2022/12/16/11/29
 **/
@SpringBootTest
public class aria2Test {

    @Test
    public void sendUrl() {
        Aria2Option aria2Option = new Aria2Option();
        aria2Option.setOut("test4.png")
                .setReferer("*")
        ;
        String url = "https://oss.iictc.cn/image/breast/sort/20221010/benign_18-1.png";
        Aria2Util aria2Util = new Aria2Util();
        aria2Util.setId("03F51EA594BBF1A3C0F9FF5D3F81CE2B3BFC35B0")
                .setMethod(Aria2Util.METHOD_ADD_URI)
                .addParam("token:qinfengge")
                .addParam(new String[]{url})
                .addParam(aria2Option);
        ;
        String send = aria2Util.send("http://119.28.70.54:6800/jsonrpc");
        System.err.println(send);

    }

    @Test
    public void getVersion(){
        Aria2Util aria2Util = new Aria2Util();
        aria2Util.setMethod(Aria2Util.METHOD_GET_VERSION)
                .addParam("token:qinfengge")
        ;
        String send = aria2Util.send("http://119.28.70.54:6800/jsonrpc");
        System.err.println(send);
    }

    @Test
    public void tellActive() {
        String result = Aria2Util.tellActive("http://119.28.70.54:6800/jsonrpc", "qinfengge");
        System.err.println(result);
    }

    @Test
    public void testXmlParse() throws Exception {
        RssHandelUtil rssHandelUtil = new RssHandelUtil();
        List<String> list = rssHandelUtil.rssHandel("http://107.173.156.30:1200/javdb/rankings");
        List<String> codes = new ArrayList<>();
        for (String code : list) {
            String[] strings = code.split(" ");
            codes.add(strings[0]);
        }
        System.err.println(codes);
    }
}
