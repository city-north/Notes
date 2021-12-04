# 010-环境仓库抽象-EnvironmentRepository

[TOC]

## 简介

EnvironmentRepository 抽象用户获取环境

```java
public interface EnvironmentRepository {

	Environment findOne(String application, String profile, String label);

	default Environment findOne(String application, String profile, String label,
			boolean includeOrigin) {
		return findOne(application, profile, label);
	}

}

```

获取环境有三个维度

1. application
2. profile
3. label

## Environment抽象

此Environment不是 core包中的Environment , 而是SpringCloudConfig自己封装的

```java
public class Environment {
	
  //Spring配置文件名, 对应 application这个维度
	private String name;
  // 生效的 active profiles . 对应 profiles 这个维度
	private String[] profiles = new String[0];
	//String 标签, 对应 label 这个维度
	private String label;
  //加载数据源集合 List<PropertySource>
	private List<PropertySource> propertySources = new ArrayList<>();
  //版本号
	private String version;
  //String状态
	private String state;
}
```

## 具体实现

- Git (JGitEnvironmentRepository)
- SVN (SvnKitEnvironmentRepository)
- File System (Native EnvironmentRespository)
- JDBC (JdbcEnvironmentRepositroy)
- Redis(RedisEnvironmentRepository)
- AWS S3 云存储( AwsS3EnvironmentRepository)

