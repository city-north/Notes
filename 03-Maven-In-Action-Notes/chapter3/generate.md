## 使用generate命令指定仓库

例子：

```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate  -D archetypeGroupId=com.hand.hls -D archetypeArtifactId=hls-custom-parent-archetype -D archetypeVersion=1.0.1-RELEASE -D groupId=com.hand.hls.custom -D artifactId=hls-custom -D package=com.hand.hls.custom -D version=1.0.0-RELEASE -D archetypeRepository=仓库地址
```

* -D archetypeGroupId=模板的groupId
* -D archetypeArtifactId=模板的唯一ID
* -D archetypeVersion=模板版本号
* -D groupId=要生成项目的groupId
* -D artifactId=要生成项目的唯一ID
* -D package=自动生成项目的代码package结构
* -D version=要生成项目的版本
* -D archetypeRepository=模板的仓库地址



