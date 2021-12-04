# 012-JDBC环境抽象实现-JdbcEnvironmentRepository

[TOC]

## 简介

JdbcEnvironmentRepository 顾名思义, 基于JDBC存储到数据库的 EnvironmentRepository

## 源码

```java
public class JdbcEnvironmentRepository implements EnvironmentRepository, Ordered {
	private final JdbcTemplate jdbc;
	private final PropertiesResultSetExtractor extractor = new PropertiesResultSetExtractor();
	private int order;
	private String sql;
	...

	@Override
	public Environment findOne(String application, String profile, String label) {
		String config = application;
		if (StringUtils.isEmpty(label)) {
			label = "master"; // 如果label不传, 默认用master
		}
		if (StringUtils.isEmpty(profile)) {
			profile = "default"; // 如果 profile 不传, 默认用 default
		}
		if (!profile.startsWith("default")) {
			profile = "default," + profile; // 如果 profile 参数有传递且不易default开头, 则加上 "default"前缀, 后续以"," 分隔, 这里相当于在原有的 profile 参数上再添加一个 default profile
		}
		String[] profiles = StringUtils.commaDelimitedListToStringArray(profile);
		Environment environment = new Environment(application, profiles, label, null,
				null);
		if (!config.startsWith("application")) {
			config = "application," + config;
		}
		List<String> applications = new ArrayList<String>(new LinkedHashSet<>(
				Arrays.asList(StringUtils.commaDelimitedListToStringArray(config))));
		List<String> envs = new ArrayList<String>(
				new LinkedHashSet<>(Arrays.asList(profiles)));
		Collections.reverse(applications);
		Collections.reverse(envs);
		for (String app : applications) {
			for (String env : envs) {
				Map<String, String> next = (Map<String, String>) this.jdbc.query(this.sql,
						new Object[] { app, env, label }, this.extractor);
				if (!next.isEmpty()) {
					environment.add(new PropertySource(app + "-" + env, next));
				}
			}
		}
		return environment;
	}
}
```

