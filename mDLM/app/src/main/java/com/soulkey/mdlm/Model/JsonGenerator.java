package com.soulkey.mdlm.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class JsonGenerator {
    public JsonObject makeInsertLinkJSON(List<String> link_data) {
        JsonObject json_query = new JsonObject();
        json_query.addProperty("table_name", "tb_link_info");
        json_query.addProperty("query_type", "INSERT");
        json_query.addProperty("column", "(domain, link)");

        JsonArray value_array = new JsonArray();
        for(String link : link_data) {
            JsonArray dat = new JsonArray();
            try {
                URL url = new URL(link);
                dat.add(url.getAuthority());
            }catch(MalformedURLException mue) {
                System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
                dat.add("");
            }
            dat.add(link);
            value_array.add(dat);
        }
        json_query.add("values", value_array);
        return json_query;
    }

    public JsonObject makeInsertBMKJSON(List<String> link_data) {
        JsonObject json_query = new JsonObject();
        json_query.addProperty("table_name", "tb_bookmark_info");
        json_query.addProperty("query_type", "INSERT");
        json_query.addProperty("column", "(domain, link)");

        JsonArray value_array = new JsonArray();
        for(String link : link_data) {
            JsonArray dat = new JsonArray();
            try {
                URL url = new URL(link);
                dat.add(url.getAuthority());
            }catch(MalformedURLException mue) {
                System.err.println("URL 분석 오류 : 올바른 URL이 아닙니다.");
                dat.add("");
            }
            dat.add(link);
            value_array.add(dat);
        }
        json_query.add("values", value_array);
        return json_query;
    }
}
