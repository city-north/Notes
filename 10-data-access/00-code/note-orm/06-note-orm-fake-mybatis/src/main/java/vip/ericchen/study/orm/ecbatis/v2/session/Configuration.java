package vip.ericchen.study.orm.ecbatis.v2.session;

import vip.ericchen.study.orm.ecbatis.v2.binding.MapperRegistry;
import vip.ericchen.study.orm.ecbatis.v2.executor.CachingExecutor;
import vip.ericchen.study.orm.ecbatis.v2.executor.Executor;
import vip.ericchen.study.orm.ecbatis.v2.executor.SimpleExecutor;
import vip.ericchen.study.orm.ecbatis.v2.mapping.MappedStatement;
import vip.ericchen.study.orm.ecbatis.v2.plugin.Interceptor;
import vip.ericchen.study.orm.ecbatis.v2.plugin.InterceptorChain;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <p>
 * 配置文件
 * </p>
 *
 * @author EricChen 2020/02/25 21:18
 */
public class Configuration {

    public final ResourceBundle config;
    public final ResourceBundle statements;

    private MapperRegistry mapperRegistry = new MapperRegistry();
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    private InterceptorChain interceptorChain = new InterceptorChain(); // 插件


    private static final String KEY_MAPPER_PATH = "mapper.path";
    private static final String KEY_PLUGIN_PATH = "plugin.path";

    protected boolean cacheEnabled = true;


    public Configuration(String properties) {
        config = ResourceBundle.getBundle(properties);
        addMapper(mapperRegistry, config);
        addPlugin(interceptorChain, config);
        statements = ResourceBundle.getBundle("ecbatis-statement");
        if (statements != null) {
            addStatements(mappedStatementMap, statements);
        }

    }

    private void addPlugin(InterceptorChain interceptorChain, ResourceBundle config) {
        String string = config.getString(KEY_PLUGIN_PATH);
        Class<?> plugin = null;
        try {
            plugin = Class.forName(string);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object instance = null;
        try {
            instance = plugin.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (instance instanceof Interceptor) {
            interceptorChain.addInterceptor((Interceptor) instance);
        }
    }

    private void addMapper(MapperRegistry mapperRegistry, ResourceBundle config) {
        String mapperInterface = config.getString(KEY_MAPPER_PATH);
        if (!"".equals(mapperInterface)) {
            try {
                Class<?> mapper = Class.forName(mapperInterface);
                mapperRegistry.addMapper(mapper);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    //扫描注册 Mapper
    private void addStatements(Map<String, MappedStatement> mappedStatementMap, ResourceBundle statements) {
        for (String key : statements.keySet()) {
            String sql = statements.getString(key);
            if (!key.contains("resultType")) {
                String resultType = statements.getString(key + ".resultType");
                MappedStatement statement = null;
                if (!"".equals(resultType)) {
                    try {
                        Class<?> resultTypeClass = Class.forName(resultType);
                        statement = new MappedStatement(key, sql, resultTypeClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    statement = new MappedStatement(key, sql, null);
                }
                mappedStatementMap.put(key, statement);
            }
        }

    }


    /**
     * 获取 Mapper对象
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    //要开启缓存装饰器,所以放在 Configuration 里面
    public Executor newExecutor() {
        Executor simpleExecutor = new SimpleExecutor(this);
        if (cacheEnabled) {
            simpleExecutor = new CachingExecutor(simpleExecutor);
        }
        if (interceptorChain.hasPlugin()) {
            simpleExecutor = (Executor) interceptorChain.pluginAll(simpleExecutor);
        }
        return simpleExecutor;
    }

    /**
     * 根据 StatementId 获取 StatementId
     */
    public MappedStatement getMappedStatement(String statementId) {
        return mappedStatementMap.get(statementId);
    }

    public ResourceBundle getConfig() {
        return config;
    }
}
