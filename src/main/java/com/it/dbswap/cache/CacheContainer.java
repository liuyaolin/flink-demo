//package com.it.dbswap.cache;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.it.dbswap.util.EsClientPool;
//import com.sf.bdp.rdmp.common.domain.*;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.*;
//
///**
// * @description: 公用的 CacheContainer
// * @author: huangchm-01328365
// * @date: 2020-04-16 10:50
// */
//public class CacheContainer {
//
//    private static Logger log = LoggerFactory.getLogger(CacheContainer.class);
//
//    /**
//     * 并发数
//     */
//    private static final int CURRENCY_LEVEL = 10;
//
//
//    private static final int MAX_DEPARTMENT_SIZE = 70000;
//
//    /**
//     * 员工数据缓存大小
//     */
//    private static final int MAX_EMPLOYEE_SIZE = 1200000;
//
//    /**
//     * 产品数据缓存大小
//     */
//    private static final int MAX_PRODUCT_SIZE = 500;
//
//    private static final int MAX_TEAMCODE_SIZE = 300000;
//
//    private static final int MAX_BATCH_SIZE = 50000;
//
//    private static final int MAX_CUSTOMER_SIZE = 200000;
//
//    private static final int MAX_STAYWHY_SIZE = 500;
//
//    private static final int MAX_FEETYPE_SIZE = 500;
//
//    private static final int MAX_TIMEWHEEL_SIZE = 400000;
//
//    private static final int MAX_SEASON_SIZE = 2000;
//
//    private static final int MAX_AOI_SIZE = 1;
//
//    private static final int MAX_NIGHT_BATCH_SIZE=20000;
//
//    private final Cache<String, Optional<Department>> departmentCache;
//    private final Cache<String, Optional<EmployeeInfo>> employeeCache;
//    private final Cache<String, Optional<BatchInfo>> batchInfoCache;
//    private final Cache<String, Optional<ProductInfo>> productCache;
//    private final Cache<String, Optional<DepartmentTeam>> teamCache;
//    private final Cache<String, Optional<Customer>> customerCache;
//    private final Cache<String, Optional<BarStayWhy>> barStayWhyCache;
//    private final Cache<String, Optional<FeeType>> feeTypeCache;
//    private LoadingCache<String, Integer> timeWheelHashCodeCache;
//    private final Cache<String, Optional<String>> seasonFoodCache;
//    private final Cache<String,Optional<String>> aoiAreaCache;
//    private final Cache<String,Optional<String>> nightBatchCache;
//
//    private static CacheContainer instance;
//
//    private CacheContainer() {
//        this.departmentCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_DEPARTMENT_SIZE)
//                .maximumSize(MAX_DEPARTMENT_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.employeeCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_EMPLOYEE_SIZE)
//                .maximumSize(MAX_EMPLOYEE_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.batchInfoCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_BATCH_SIZE)
//                .maximumSize(MAX_BATCH_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.productCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_PRODUCT_SIZE)
//                .maximumSize(MAX_PRODUCT_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.teamCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_TEAMCODE_SIZE)
//                .maximumSize(MAX_TEAMCODE_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.customerCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_CUSTOMER_SIZE)
//                .maximumSize(MAX_CUSTOMER_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.barStayWhyCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_STAYWHY_SIZE)
//                .maximumSize(MAX_STAYWHY_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//        this.feeTypeCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_FEETYPE_SIZE)
//                .maximumSize(MAX_FEETYPE_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//
//
//        this.timeWheelHashCodeCache = CacheBuilder.newBuilder()
//                .maximumSize(MAX_TIMEWHEEL_SIZE)
//                .recordStats()
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build(new CacheLoader<String, Integer>() {
//            @Override
//            public Integer load(String key) throws Exception {
//                //当key对应的value不存在时，返回 null;
//                return 0;
//            }
//        });
//
//        this.seasonFoodCache = CacheBuilder.newBuilder()
//                .initialCapacity(MAX_SEASON_SIZE)
//                .maximumSize(MAX_SEASON_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//
//        this.aoiAreaCache=CacheBuilder.newBuilder()
//                .initialCapacity(MAX_AOI_SIZE)
//                .maximumSize(MAX_AOI_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//
//        this.nightBatchCache=CacheBuilder.newBuilder()
//                .initialCapacity(MAX_NIGHT_BATCH_SIZE)
//                .maximumSize(MAX_NIGHT_BATCH_SIZE)
//                .concurrencyLevel(CURRENCY_LEVEL)
//                .expireAfterWrite(25, TimeUnit.HOURS)
//                .build();
//    }
//
//    public LoadingCache<String, Integer> getTimeWheelHashCodeCache() {
//        return timeWheelHashCodeCache;
//    }
//
//    public void setTimeWheelHashCodeCache(LoadingCache<String, Integer> timeWheelHashCodeCache) {
//        this.timeWheelHashCodeCache = timeWheelHashCodeCache;
//    }
//
//    public synchronized static CacheContainer getInstance() {
//        if (instance == null) {
//            instance = new CacheContainer();
//        }
//
//        return instance;
//    }
//
//    public Optional<Department> fetchDepartment(String orgCode) {
//        try {
//
//            if (StringUtils.isBlank(orgCode)) {
//                return Optional.empty();
//            }
//
//            //return departmentCache.get(orgCode, Optional::empty);
//            Optional<Department> optional = departmentCache.getIfPresent(orgCode);
//           //return departmentCache.get(orgCode,()->{return Optional.empty();});
//            return optional != null ? optional : Optional.empty();
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<String> fetchSeasonFood(String consigment) {
//        try {
//
//            if (StringUtils.isBlank(consigment)) {
//                return Optional.empty();
//            }
//
//            Optional<String> optional = seasonFoodCache.getIfPresent(consigment);
//            return optional != null ? optional : Optional.empty();
//            //return seasonFoodCache.get(consigment, Optional::empty);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//    public Optional<Department> fetchNeedESDepartment(String orgCode) {
//        try {
//
//            if (StringUtils.isBlank(orgCode)) {
//                return Optional.empty();
//            }
//
//            return departmentCache.get(orgCode, () -> {
//                GetResponse resp = EsClientPool.getEsClient(EsConstant.TAG_DEPARTMENT)
//                        .prepareGet(EsConstant.INDEX_DEPARTMENT,
//                                EsConstant.TYPE_DEPARTMENT, orgCode).get();
//                if (resp != null && resp.isExists()) {
//                    Optional<Department> department = Optional.of(new Department(resp.getSource()));
//                    departmentCache.put(orgCode, department);
//                    return department;
//                }
//                return Optional.empty();
//            });
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//
//    public Optional<EmployeeInfo> fetchEmployee(String empCode) {
//        try {
//            if (StringUtils.isBlank(empCode)) {
//                return Optional.empty();
//            }
//            //return employeeCache.get(empCode,()->{ return Optional.empty();});
//            Optional<EmployeeInfo> optional = employeeCache.getIfPresent(empCode);
//            return optional != null ? optional : Optional.empty();
//            //return employeeCache.get(empCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<EmployeeInfo> fetchNeedESEmployee(String empCode) {
//        try {
//            if (StringUtils.isBlank(empCode)) {
//                return Optional.empty();
//            }
//
//            return employeeCache.get(empCode, () -> {
//                GetResponse resp = EsClientPool.getEsClient(EsConstant.TAG_EMPLOYEE)
//                        .prepareGet(EsConstant.INDEX_EMPLOYEE,
//                                EsConstant.TYPE_EMPLOYEE, empCode).get();
//                if (resp != null && resp.isExists()) {
//                    Optional<EmployeeInfo> employeeInfo = Optional.of(new EmployeeInfo(resp.getSource()));
//                    employeeCache.put(empCode, employeeInfo);
//                    return employeeInfo;
//                }
//                return Optional.empty();
//            });
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<BatchInfo> fetchBatchInfo(String key) {
//        try {
//
//            if (StringUtils.isBlank(key)) {
//                return Optional.empty();
//            }
//
//            //return batchInfoCache.get(key,()->{return Optional.empty();});
//            Optional<BatchInfo> optional = batchInfoCache.getIfPresent(key);
//            return optional != null ? optional : Optional.empty();
//            //return batchInfoCache.get(key, Optional::empty);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<ProductInfo> fetchProduct(String productCode) {
//        try {
//            if (StringUtils.isBlank(productCode)) {
//                return Optional.empty();
//            }
//
//            //return productCache.get(productCode,()->{return Optional.empty();});
//            Optional<ProductInfo> optional = productCache.getIfPresent(productCode);
//            return optional != null ? optional : Optional.empty();
//            //return productCache.get(productCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<DepartmentTeam> fetchDepartmentTeam(String teamCode) {
//        try {
//            if (StringUtils.isBlank(teamCode)) {
//                return Optional.empty();
//            }
//            //return teamCache.get(teamCode,()->{ return Optional.empty();});
//            Optional<DepartmentTeam> optional = teamCache.getIfPresent(teamCode);
//            return optional != null ? optional : Optional.empty();
//            //return teamCache.get(teamCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//    public Optional<DepartmentTeam> fetchNeedESDepartmentTeam(String teamCode) {
//        try {
//            if (StringUtils.isBlank(teamCode)) {
//                return Optional.empty();
//            }
//
//            return teamCache.get(teamCode, () -> {
//                GetResponse resp = EsClientPool.getEsClient(EsConstant.TAG_TEAM)
//                        .prepareGet(EsConstant.INDEX_TEAM,
//                                EsConstant.TYPE_TEAM, teamCode).get();
//                if (resp != null && resp.isExists()) {
//                    Optional<DepartmentTeam> departmentTeam = Optional.of(new DepartmentTeam(resp.getSource()));
//                    teamCache.put(teamCode, departmentTeam);
//                    return departmentTeam;
//                }
//                return Optional.empty();
//            });
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<Customer> fetchCustomer(String customerAccCode) {
//        try {
//            if (StringUtils.isBlank(customerAccCode)) {
//                log.warn("fetchCustomer() 入参customerAccCode为空");
//                return Optional.empty();
//            }
//
//           // return customerCache.get(customerAccCode,()->{ return Optional.empty();});
//            Optional<Customer> optional = customerCache.getIfPresent(customerAccCode);
//            return optional != null ? optional : Optional.empty();
//            //return customerCache.get(customerAccCode, Optional::empty);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//
//    public Optional<Customer> fetchNeedESCustomer(String customerAccCode) {
//        try {
//            if (StringUtils.isBlank(customerAccCode)) {
//                log.warn("fetchCustomer() 入参customerAccCode为空");
//                return Optional.empty();
//            }
//            QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                    .filter(QueryBuilders.termQuery("companycustno", customerAccCode))
//                    .filter(QueryBuilders.termsQuery("custtype","01","02","07"));
//                     return customerCache.get(customerAccCode, () -> {
//                         SearchResponse resp =  EsClientPool.getEsClient(EsConstant.TAG_CUSTOMER).prepareSearch()
//                                 .setIndices(EsConstant.INDEX_CUSTOMER)
//                                 .setQuery(queryBuilder) .setSize(1)
//                                 .get();
//
//                if (resp != null) {
//                    SearchHit[] hits = resp.getHits().getHits();
//                    if(hits!=null && hits.length>0){
//                        Optional<Customer> customer = Optional.of(new Customer(hits[0].getSource()));
//                        customerCache.put(customerAccCode, customer);
//                        return customer;
//                    }
//
//                }
//                return Optional.empty();
//            });
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<BarStayWhy> fetchBarStayWhhy(String barStayWhyCode) {
//        try {
//            if (StringUtils.isBlank(barStayWhyCode)) {
//                log.warn("fetchBarStayWhhy() 入参barStayWhyCode为空");
//                return Optional.empty();
//            }
//            //return barStayWhyCache.get(barStayWhyCode,()->{ return Optional.empty();});
//            Optional<BarStayWhy> optional = barStayWhyCache.getIfPresent(barStayWhyCode);
//            return optional != null ? optional : Optional.empty();
//            //return barStayWhyCache.get(barStayWhyCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<FeeType> fetchFeeType(String serviceCode) {
//        try {
//            if (StringUtils.isBlank(serviceCode)) {
//                log.warn("fetchFeeType() 入参serviceCode为空");
//                return Optional.empty();
//            }
//           // return feeTypeCache.get(serviceCode,()->{ return Optional.empty();});
//            Optional<FeeType> optional = feeTypeCache.getIfPresent(serviceCode);
//            return optional != null ? optional : Optional.empty();
//            //return feeTypeCache.get(serviceCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Integer fetchTimeWhellCacheHashCode(String key){
//        if(StringUtils.isBlank(key)){
//            log.warn("fetchTimeWheelCacheCode()入参key为空");
//            return null;
//        }else{
//            return timeWheelHashCodeCache.getIfPresent(key);
//        }
//    }
//
//    public void setTimeWhelCacheHashCode(String key, Integer hashCode){
//        if(StringUtils.isBlank(key) || null == hashCode){
//            log.warn("setTimeWheelCacheHashCode() 入参不全法，key:{}, hashCode:{} ", key,hashCode);
//        }else{
//            timeWheelHashCodeCache.put(key,hashCode);
//        }
//    }
//
//    public Optional<String> fetchAoiType(String aoiCode) {
//        try {
//            if (StringUtils.isBlank(aoiCode)) {
//                log.warn("fetchAoiType() 入参aoiCode为空");
//                return Optional.empty();
//            }
//
//            //return aoiAreaCache.get(aoiCode,()->{ return Optional.empty();});
//            Optional<String> optional = aoiAreaCache.getIfPresent(aoiCode);
//            return optional != null ? optional : Optional.empty();
//            //return aoiAreaCache.get(aoiCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    public Optional<String> fetchNightBatch(String batchCode) {
//        try {
//            if (StringUtils.isBlank(batchCode)) {
//                log.warn("fetchNightBatch() 入参batchCode为空");
//                return Optional.empty();
//            }
//            /*return nightBatchCache.get(batchCode, Optional::empty);*/
//            Optional<String> optional = nightBatchCache.getIfPresent(batchCode);
//            return optional != null ? optional : Optional.empty();
//            //return nightBatchCache.get(batchCode, Optional::empty);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return Optional.empty();
//    }
//
//    // init method begins
//
//    public long initDepartmentCacheFromEs() {
//        long total = 0L;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_DEPARTMENT);
//
//        String indexName = EsConstant.INDEX_DEPARTMENT;
//        String typeName = EsConstant.TYPE_DEPARTMENT;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<Department> department = Optional.of(new Department(resp.getSource()));
//                departmentCache.put(resp.getId(), department);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initEmployeeCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_EMPLOYEE);
//
//        String indexName = EsConstant.INDEX_EMPLOYEE;
//        String typeName = EsConstant.TYPE_EMPLOYEE;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                //.setSource(searchSourceBuilder)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<EmployeeInfo> employeeInfo = Optional.of(new EmployeeInfo(resp.getSource()));
//                employeeCache.put(resp.getId(), employeeInfo);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public void initBatchCacheFromEs() {
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_BATCH);
//
//        String indexName = EsConstant.INDEX_BATCH;
//        String typeName = EsConstant.TYPE_BATCH;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<BatchInfo> batchInfo = Optional.of(new BatchInfo(resp.getSource()));
//                batchInfoCache.put(resp.getId(), batchInfo);
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//    }
//
//    public long initProductCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_PRODUCT);
//
//        String indexName = EsConstant.INDEX_PRODUCT;
//        String typeName = EsConstant.TYPE_PRODUCT;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<ProductInfo> productInfo = Optional.of(new ProductInfo(resp.getSource()));
//                productCache.put(resp.getId(), productInfo);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initDepartmentTeamCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_TEAM);
//
//        String indexName = EsConstant.INDEX_TEAM;
//        String typeName = EsConstant.TYPE_TEAM;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<DepartmentTeam> departmentTeam = Optional.of(new DepartmentTeam(resp.getSource()));
//                teamCache.put(resp.getId(), departmentTeam);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initCustomerCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_CUSTOMER);
//
//        String indexName = EsConstant.INDEX_CUSTOMER;
//        String typeName = EsConstant.TYPE_CUSTOMER;
//
//        QueryBuilder bool = QueryBuilders.boolQuery()
//                             .must(QueryBuilders.termsQuery("custtype", Arrays.asList("01", "02", "07")));
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                .setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<Customer> customer = Optional.of(new Customer(resp.getSource()));
//                customerCache.put(resp.getId(), customer);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initBarStayWhyCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_STAYCODE);
//
//        String indexName = EsConstant.INDEX_STAYCODE;
//        String typeName = EsConstant.TYPE_STAYCODE;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<BarStayWhy> barStayWhy = Optional.of(new BarStayWhy(resp.getSource()));
//                barStayWhyCache.put(resp.getId(), barStayWhy);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initFeeTypeCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_FEE);
//
//        String indexName = EsConstant.INDEX_FEE;
//        String typeName = EsConstant.TYPE_FEE;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Optional<FeeType> feeType = Optional.of(new FeeType(resp.getSource()));
//                feeTypeCache.put(resp.getId(), feeType);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//
//    public long initSeasonFoodtCacheFromEs() {
//        long total = 0L;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_SEASON_FOOD);
//
//        String indexName = EsConstant.INDEX_SEASON_FOOD;
//        String typeName = EsConstant.TYPE_SEASON_FOOD;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Map<String, Object> source = resp.getSource();
//                String consignment = (String) source.get("consignment");
//                String content = (String) source.get("content");
//                String[] contents = content.split(",|，");
//
//                for (String con : contents) {
//                    Optional<String> consignment1 = Optional.of(consignment);
//                    seasonFoodCache.put(con, consignment1);
//                    total++;
//                }
//
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initAoiAreaCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_AOI);
//
//        String indexName = EsConstant.INDEX_AOI;
//        String typeName = EsConstant.TYPE_AOI;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Map<String, Object> source = resp.getSource();
//                Optional<String> aoiAreaType =  Optional.of((String) source.get("aoi_area_type"));
//                aoiAreaCache.put(resp.getId(), aoiAreaType);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    public long initNightBatchCacheFromEs() {
//        long total = 0;
//
//        TransportClient esClient = EsClientPool.getEsClient(EsConstant.TAG_NIGHT_BATCH);
//
//        String indexName = EsConstant.INDEX_NIGHT_BATCH;
//        String typeName = EsConstant.TYPE_NIGHT_BATCH;
//
//        SearchResponse scrollResponse = esClient.prepareSearch(indexName)
//                //.setQuery(bool)
//                .setTypes(typeName)
//                .setFrom(0).setSize(1000).setScroll(TimeValue.timeValueMinutes(10)).execute().actionGet();
//
//        SearchHit[] searchHits = scrollResponse.getHits().getHits();
//
//        while (searchHits != null && searchHits.length > 0) {
//            for (SearchHit resp : searchHits) {
//                Map<String, Object> source = resp.getSource();
//                Optional<String> batchCode =  Optional.of((String) source.get("batch_code"));;
//                nightBatchCache.put(resp.getId(), batchCode);
//                total++;
//            }
//
//            String scrollId = scrollResponse.getScrollId();
//            scrollResponse = esClient.prepareSearchScroll(scrollId)
//                    .setScroll(TimeValue.timeValueMinutes(10))
//                    .execute().actionGet();
//
//            searchHits = scrollResponse.getHits().getHits();
//        }
//
//        return total;
//    }
//
//    /**
//     * 启动的时候，一次性加载所有缓存
//     */
//    public void initCacheFromEs(final boolean schedule) throws InterruptedException {
//        log.info("initCacheFromEs_begin...");
//        long begin = System.currentTimeMillis();
//
//        // 用线程池并发加载HBaes数据，写入缓存
//        ExecutorService service = Executors.newFixedThreadPool(5);
//
//        List<String> list = Arrays.asList("initDepartmentCacheFromEs", "initEmployeeCacheFromEs", "initProductCacheFromEs",
//                "initDepartmentTeamCacheFromEs", "initCustomerCacheFromEs", "initBarStayWhyCacheFromEs", "initFeeTypeCacheFromEs",
//                "initSeasonFoodtCacheFromEs",  "initNightBatchCacheFromEs");
//
//        /**
//         * 等待所有的线程执行完成
//         */
//        final CountDownLatch cdl = new CountDownLatch(list.size());
//
//        for(final String methodName : list) {
//
//            service.submit(new Runnable() {
//                @Override
//                public void run() {
//                    long total = 0;
//                    long begin = System.currentTimeMillis();
//
//                    boolean loadSuccess = false;
//                    String errMsg = "";
//
//                    /**
//                     * 重试3次，每次睡眠20秒钟
//                     */
//                    for (int i = 0; i < 3; i++) {
//                        try {
//                            if("initDepartmentCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initDepartmentCacheFromEs();
//                            } else if("initEmployeeCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initEmployeeCacheFromEs();
//                            } else if("initProductCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initProductCacheFromEs();
//                            } else if("initDepartmentTeamCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initDepartmentTeamCacheFromEs();
//                            } else if("initCustomerCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initCustomerCacheFromEs();
//                            } else if("initBarStayWhyCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initBarStayWhyCacheFromEs();
//                            } else if("initFeeTypeCacheFromEs".equals(methodName)) {
//                                total = CacheContainer.this.initFeeTypeCacheFromEs();
//                            }else if ("initSeasonFoodtCacheFromEs".equals(methodName)){
//                                total = CacheContainer.this.initSeasonFoodtCacheFromEs();
//                            }else if ("initAoiAreaCacheFromEs".equals(methodName)){
//                                total = CacheContainer.this.initAoiAreaCacheFromEs();
//                            }else if ("initNightBatchCacheFromEs".equals(methodName)){
//                                total = CacheContainer.this.initNightBatchCacheFromEs();
//                            }
//
//                            loadSuccess = true;
//                            break;
//                        } catch (Exception e) {
//                            errMsg = e.getMessage();
//                            log.error("第{}次缓存加载失败_{},再试一次_{}",i,methodName,errMsg);
//                            try {
//                                TimeUnit.SECONDS.sleep(20);
//                            } catch (Exception e1) {
//                                log.error("线程休息失败_{}",e1.getMessage());
//                            }
//                        }
//                    }
//
//                    if (!loadSuccess) {
//                        log.error("缓存加载失败_{}_{}", methodName, errMsg);
//                    }
//
//                    log.info( "{}====={}_total__cost_{}", Thread.currentThread().getName(),methodName,total,(System.currentTimeMillis()-begin));
//
//
//                    cdl.countDown();
//                }
//            });
//        }
//
//        cdl.await();
//        service.shutdown();
//
//        printCacheStats();
//
//        log.info("initCacheFromEs_end_cost_{}", (System.currentTimeMillis() - begin));
//    }
//
//    public void printCacheStats() {
//        log.info("departmentCache_{}_MAX_DEPARTMENT_SIZE_{}", departmentCache.size(), MAX_DEPARTMENT_SIZE);
//        log.info("employeeCache_{}_MAX_EMPLOYEE_SIZE_{}", employeeCache.size(), MAX_EMPLOYEE_SIZE);
//        log.info("batchInfoCache_{}_MAX_BATCH_SIZE_{}", batchInfoCache.size(), MAX_BATCH_SIZE);
//        log.info("productCache_{}_MAX_PRODUCT_SIZE_{}", productCache.size(), MAX_PRODUCT_SIZE);
//        log.info("teamCache_{}_MAX_TEAMCODE_SIZE_{}", teamCache.size(), MAX_TEAMCODE_SIZE);
//        log.info("customerCache_{}_MAX_CUSTOMER_SIZE_{}", customerCache.size(), MAX_CUSTOMER_SIZE);
//        log.info("barStayWhyCache_{}_MAX_STAYWHY_SIZE_{}", barStayWhyCache.size(), MAX_STAYWHY_SIZE);
//        log.info("feeTypeCache_{}_MAX_FEETYPE_SIZE_{}", feeTypeCache.size(), MAX_FEETYPE_SIZE);
//        log.info("timeWheelHashCodeCache_{}_MAX_TIMEWHEEL_SIZE_{}", timeWheelHashCodeCache.size(), MAX_TIMEWHEEL_SIZE);
//        log.info("seasonFoodtCache_{}_MAX_SEASON_SIZE_{}" , seasonFoodCache.size(),MAX_SEASON_SIZE);
//
//
//        if(departmentCache.size() == 0) {
//            log.error("departmentCacheSize0_{}_MAX_DEPARTMENT_SIZE_{}" ,departmentCache.size(), MAX_DEPARTMENT_SIZE);
//        }
//
//        if(employeeCache.size() == 0) {
//            log.error("employeeCacheSize0_{}_MAX_EMPLOYEE_SIZE_{}",employeeCache.size(), MAX_EMPLOYEE_SIZE);
//        }
//
//        if(batchInfoCache.size() == 0) {
//            //log.error("batchInfoCacheSize0_" + batchInfoCache.size() + "_MAX_BATCH_SIZE_" + MAX_BATCH_SIZE);
//        }
//
//        if(productCache.size() == 0) {
//            log.error("productCacheSize0_{}_MAX_PRODUCT_SIZE_{}" ,productCache.size(), MAX_PRODUCT_SIZE);
//        }
//
//        if(teamCache.size() == 0) {
//            log.error("teamCacheSize0_{}_MAX_TEAMCODE_SIZE_{}",teamCache.size(),MAX_TEAMCODE_SIZE);
//        }
//
//        if(customerCache.size() == 0) {
//            log.error("customerCacheSize0_{}_MAX_CUSTOMER_SIZE_{}" ,customerCache.size(), MAX_CUSTOMER_SIZE);
//        }
//
//        if(barStayWhyCache.size() == 0) {
//            log.error("barStayWhyCacheSize0_{}_MAX_STAYWHY_SIZE_{}" ,barStayWhyCache.size(),MAX_STAYWHY_SIZE);
//        }
//
//        if(feeTypeCache.size() == 0) {
//            log.error("feeTypeCacheSize0_{}_MAX_FEETYPE_SIZE_{}" ,feeTypeCache.size(), MAX_FEETYPE_SIZE);
//        }
//
//        if(seasonFoodCache.size() == 0) {
//            log.error("seasonFoodCacheSize0_{}_MAX_SEASON_SIZE_{}" ,seasonFoodCache.size() ,  MAX_SEASON_SIZE);
//        }
//
//        if(timeWheelHashCodeCache.size() == 0) {
//            //log.error("timeWheelHashCodeCacheSize0_" + timeWheelHashCodeCache.size() + "_MAX_TIMEWHEEL_SIZE_" + MAX_TIMEWHEEL_SIZE);
//        }
//    }
//
//    public static void main(String[] args) {
//
//    }
//
//}
