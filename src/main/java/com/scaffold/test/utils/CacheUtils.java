package com.scaffold.test.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;


//@Component
public class CacheUtils {

    @Autowired
    private static CacheManager simpleCacheManager;

//    public static Object getObject(String key) {
//        Cache cacheData = simpleCacheManager.getCache("cacheData");
//        Cache.ValueWrapper valueWrapper = cacheData.get(key);
//        if (valueWrapper != null) {
//            CacheEntry myCacheData = (CacheEntry) valueWrapper.get();
//            System.out.println(myCacheData);
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        getObject("1");
//    }

}
