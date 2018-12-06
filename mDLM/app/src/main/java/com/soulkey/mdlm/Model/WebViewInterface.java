package com.soulkey.mdlm.Model;

import android.webkit.JavascriptInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebViewInterface {

    private final static String DAPINA_ROOT_DIRECTORY = "/dapina/";
    private final static String DOMAIN_BP = "http://v12.battlepage.com";
    private final static String DOMAIN_DD = "https://www.dogdrip.net";
    private final static String SELECTOR_BP = ".search_content";
    private final static String SELECTOR_DD = "#article_1 > div";

    private String link_url;
    private String link_title;

    public WebViewInterface(String title, String link){
        link_title = title;
        link_url = link;
    }

    @JavascriptInterface
    public void DapinaBP(String html){
        try{
            Document document = Jsoup.parse(html);
            Elements img_list = document.selectFirst(SELECTOR_BP).getElementsByTag("img");
            if(img_list.size() == 1){
                String img_url = img_list.get(0).attr("src");
                String filename = link_title.replaceAll(" ", "_")+img_url.substring(img_url.lastIndexOf('.'));
                Dapina.newInstance().getClient().files().saveUrl(DAPINA_ROOT_DIRECTORY+filename, img_url);
            } else {
                int cnt = 0;
                String directory_name = link_title.replaceAll(" ", "_");
                for(Element element : img_list){
                    cnt++;
                    String img_url = element.attr("src");
                    String save_path = DAPINA_ROOT_DIRECTORY+directory_name+"/"+String.format("%03d", cnt)+img_url.substring(img_url.lastIndexOf('.'));
                    Dapina.newInstance().getClient().files().saveUrl(save_path, img_url);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void DapinaDD(String html){
        try{
            Document document = Jsoup.parse(html);
            Elements img_list = document.selectFirst(SELECTOR_DD).getElementsByTag("img");
            if(img_list.size() == 1){
                String img_url = img_list.get(0).attr("src");
                if(img_url.startsWith("/")){    //inner Dogdrip link
                    img_url = DOMAIN_DD+img_url;
                }
                String filename = link_title.replaceAll(" ", "_")+img_url.substring(img_url.lastIndexOf('.'));
                Dapina.newInstance().getClient().files().saveUrl(DAPINA_ROOT_DIRECTORY+filename, img_url);
            } else {
                int cnt = 0;
                String directory_name = link_title.replaceAll(" ", "_");
                for(Element element : img_list){
                    cnt++;
                    String img_url = element.attr("src");
                    if(img_url.startsWith("/")){    //inner Dogdrip link
                        img_url = DOMAIN_DD+img_url;
                    }
                    String save_path = DAPINA_ROOT_DIRECTORY+directory_name+"/"+String.format("%03d", cnt)+img_url.substring(img_url.lastIndexOf('.'));
                    Dapina.newInstance().getClient().files().saveUrl(save_path, img_url);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
