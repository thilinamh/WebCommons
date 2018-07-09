package com.nwised.javax.commons.mbeans.commonbeans;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thilina_h on 8/2/2017.
 */
@RequestScoped
public class SessionData {
    private Map<String,Object> data_set=new HashMap<>();

    public void putData(String key, Object data){
        data_set.put(key, data);
    }

    public <T> T getData(String key){
        return (T)data_set.get(key);
    }


    public static SessionData getCurrentContext() {
        return CDI.current().select(SessionData.class).get();
    }
}
