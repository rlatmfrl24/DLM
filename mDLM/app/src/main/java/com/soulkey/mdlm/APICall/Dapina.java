package com.soulkey.mdlm.APICall;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class Dapina {
    private DbxRequestConfig mDbxRequestConfig;
    private DbxClientV2 client;
    private final static String AccessToken = "FMwnuyPWJdsAAAAAAABzAyZ1u1lAOczChSN0XSXJNfHmmUKUPrk-gS49cfTtSdg0";

    public Dapina(){
        try{
            mDbxRequestConfig = DbxRequestConfig.newBuilder("dropbox/dapina").build();
            client = new DbxClientV2(mDbxRequestConfig, AccessToken);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Dapina newInstance(){
        Dapina dapina = new Dapina();
        return dapina;
    }

    public DbxClientV2 getClient(){
        return client;
    }
}
