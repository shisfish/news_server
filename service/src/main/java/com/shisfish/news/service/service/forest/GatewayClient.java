package com.shisfish.news.service.service.forest;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Put;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.callback.OnProgress;

import java.io.File;
import java.util.Map;

public interface GatewayClient {

    @Request(
            url = "${0}"
    )
    String doGetNoParam(String url);

    @Request(
            url = "${0}"
    )
    String doGet(String url, @Query Map<String, Object> map);

    @Get(
            url = "${0}"
    )
    String doGet(String url, @Query Map<String, Object> map, @Header Map<String, Object> headerMap);

    @Get(
            url = "${0}"
    )
    String doGetWithHeader(String url, @Header Map<String, Object> headerMap);

    @Put(
            url = "${0}"
    )
    String doPut(String url, @Body Map<String, Object> map, @Header Map<String, Object> headerMap);


    @Request(
            url = "${0}",
            type = "post",
            headers = {"Content-Type: application/json"}
    )
    String doPostJson(String url, @Body Object object);

    @Post(
            url = "${0}"
    )
    String doPostJson1(String url, @JSONBody Map<String, Object> map);

    @Request(
            url = "${0}",
            type = "post",
            headers = {"Content-Type: application/x-www-form-urlencoded"}
    )
    String doPostForm(String url, @Query Map<String, Object> map);


    @Post(
            url = "${0}"
    )
    String doPost(String url, @Query Map<String, Object> map, @Header Map<String, Object> headerMap);

    @Post(
            url = "${0}"
    )
    String doJSONPost(String url, @JSONBody Map<String, Object> map, @Header Map<String, Object> headerMap);

    File downloadFile(String dir, String filename, OnProgress onProgress);
}
