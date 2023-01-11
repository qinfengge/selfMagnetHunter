package xyz.qinfengge.rsscode.utils;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lza
 * @Date 2022/12/16/15/17
 **/
@Component
public class RssHandelUtil {

        public List<String> rssHandel(String url) throws Exception {

            // 创建一个 URL 对象
            URL feedUrl = new URL(url);

            // 创建一个新的 SyndFeedInput 对象
            SyndFeedInput input = new SyndFeedInput();

            // 使用 SyndFeedInput 对象来解析 RSS 订阅
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            // 获取订阅中的所有文章
            List<SyndEntry> entries = feed.getEntries();
            List<String> code = new ArrayList<>();
            // 遍历所有文章，并输出标题和 HTML 内容
            for (SyndEntry entry : entries) {
//                // 获取文章的标题
                String title = entry.getTitle();
//                // 获取文章的发布日期
//                String date = entry.getPublishedDate().toString();
//                // 获取文章的引用
//                List<SyndEnclosure> enclosures = entry.getEnclosures();
//                String manget = enclosures.get(0).getUrl();
//
                // 获取GUID
//                String uri = entry.getUri();
                code.add(title);
//                // 获取文章的链接
//                String link = entry.getLink();
//                // 获取文章的作者
//                String author = entry.getAuthor();
//                // 获取文章的分类
//                List<SyndCategory> categories = entry.getCategories();
//                List<String> list = entry.getCategories().stream().map(SyndCategory::getName).collect(Collectors.toList());
//
//                // 获取文章的 HTML 内容
//                String html = entry.getDescription().getValue();
//                // 使用 jsoup 库将 HTML 内容解析成 Document 对象
//                Document doc = Jsoup.parse(html);
//                List<Map<String, Object>> mgList = new ArrayList<>();
//                doc.getElementsByTag("tr").forEach(element -> {
//                    String[] split = element.text().split(" ");
//                    Map<String, Object> map = new HashMap<>();
//                    element.getElementsByTag("a").forEach(a -> {
//                        map.put("name", a.text());
//                        map.put("href", a.attr("href"));
//                    });
//                    map.put("mgSize", split[1]);
//                    map.put("mgDate", split[2]);
//                    mgList.add(map);
//                });
//                list.remove(0);

            }
            return code;
        }
}
