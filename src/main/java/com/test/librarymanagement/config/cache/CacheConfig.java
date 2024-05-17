package com.test.librarymanagement.config.cache;

import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.dto.PageableDTO;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String BOOK_PAGE_RESULT_CACHE = "bookPageCache";

    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        return new JCacheCacheManager(cacheManager());
    }

    @Bean
    public CacheManager cacheManager() {
        javax.cache.CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache(BOOK_PAGE_RESULT_CACHE, bookListCacheConfiguration());
        return cacheManager;
    }

    private MutableConfiguration<String, PageableDTO<BookDTO>> bookListCacheConfiguration() {
        return new MutableConfiguration<String, PageableDTO<BookDTO>>()
                .setTypes(String.class, (Class<PageableDTO<BookDTO>>)(Class<?>)PageableDTO.class)
                .setStoreByValue(true)
                .setStatisticsEnabled(true)
                .setManagementEnabled(true)
                .setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.HOURS, 2)));
    }
}
