package io.cex.test.framework.mock;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

/**
 * @author shenqingyan
 * @create 2018/11/14 14:30
 * @desc 调用wiremock
 **/
public class MockServer {
    /**
     * @desc 连接wiremock服务，并更新对应配置信息
     * @param
     **/
    public static void main(String[] args){
        //wiremock服务器的IP及mock的端口号
        WireMock.configureFor("127.0.0.1", 3289);
        //清除已有的配置信息
        WireMock.removeAllMappings();
        try {
            mockGet("/test/mock/get", "getTest");
            mockPost("/test/mock/post", "postTest");
            mockPostWithBody("/test/mock/post/body", "postBodyTest","type");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @desc  处理get请求
     * @param  url 需要mock的uri
     * @param  file 对应返回body的文件名
     **/
    private static void mockGet(String url, String file) throws IOException{
        ClassPathResource resource = new ClassPathResource("mock/response/"+file+".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(), "UTF-8").toArray(), "\n");
        WireMock.stubFor(
                // 收到/user/1请求时的处理
                WireMock.get(WireMock.urlEqualTo(url))
                        .willReturn(
                                WireMock.aResponse()
                                        // 返回的状态码
                                        .withStatus(200)
                                        // 返回的JSON串
                                        .withBody(content)));

    }
    /**
     * @desc 处理post请求,任意body
     * @param  url 需要mock的uri
     * @param  file 对应返回body的文件名
     **/
    private static void mockPost(String url, String file) throws IOException{
        ClassPathResource resource = new ClassPathResource("mock/response/" + file +".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray(), "\n");
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo(url))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withBody(content)
                        )
        );
    }
    /**
     * @desc 处理post请求,body中包含某字符串
     * @param  url 需要mock的uri
     * @param  file 对应返回body的文件名
     * @param  body 请求体中包含的字符串
     **/
    private static void mockPostWithBody(String url, String file, String body) throws IOException{
        ClassPathResource resource = new ClassPathResource("mock/response/" + file +".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray(), "\n");
        ContainsPattern containsPattern = new ContainsPattern(body);
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo(url))
                        .withRequestBody(containsPattern)
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withBody(content)
                        )
        );
    }
}
