package com.scaffold.test.utils;

import com.sun.deploy.cache.CacheEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


//@Component
public class CacheUtils {

    @Autowired
    private static CacheManager simpleCacheManager;

    public static Object getObject(String key) {
        Cache cacheData = simpleCacheManager.getCache("cacheData");
        Cache.ValueWrapper valueWrapper = cacheData.get(key);
        if (valueWrapper != null) {
            CacheEntry myCacheData = (CacheEntry) valueWrapper.get();
            System.out.println(myCacheData);
//            if (myCacheData != null) {
//                boolean expired = (new Date()).after(myCacheData.getExpirationDate());
//                if (expired) {
//                    cacheData.evict(key);
//                } else {
//                    return myCacheData.getCachedValue();
//
//                }
//            }
        }
        return null;
    }

    public static void main(String[] args) {
        getObject("1");
    }

}
