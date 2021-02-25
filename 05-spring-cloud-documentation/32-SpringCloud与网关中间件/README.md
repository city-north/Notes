# 32-SpringCloud与网关中间件

> https://www.cnblogs.com/liukaifeng/p/10055867.html

ShortcutConfigurable 接口提供的默认方法，主要用于对过滤器和断言参数进行标准化处理，将表达式和生成的键进行转换。

```java
public interface ShortcutConfigurable {

	enum ShortcutType {
		DEFAULT {
			@Override
			public Map<String, Object> normalize(Map<String, String> args, ShortcutConfigurable shortcutConf, SpelExpressionParser parser, BeanFactory beanFactory) {
				Map<String, Object> map = new HashMap<>();
				int entryIdx = 0;
				for (Map.Entry<String, String> entry : args.entrySet()) {
					String key = normalizeKey(entry.getKey(), entryIdx, shortcutConf, args);
					Object value = getValue(parser, beanFactory, entry.getValue());

					map.put(key, value);
					entryIdx++;
				}
				return map;
			}
		},

		GATHER_LIST {
			@Override
			public Map<String, Object> normalize(Map<String, String> args, ShortcutConfigurable shortcutConf, SpelExpressionParser parser, BeanFactory beanFactory) {
				Map<String, Object> map = new HashMap<>();
				// field order should be of size 1
				List<String> fieldOrder = shortcutConf.shortcutFieldOrder();
				Assert.isTrue(fieldOrder != null
								&& fieldOrder.size() == 1,
						"Shortcut Configuration Type GATHER_LIST must have shortcutFieldOrder of size 1");
				String fieldName = fieldOrder.get(0);
				map.put(fieldName, args.values().stream()
						.map(value -> getValue(parser, beanFactory, value))
						.collect(Collectors.toList()));
				return map;
			}
		};

		public abstract Map<String, Object> normalize(Map<String, String> args, ShortcutConfigurable shortcutConf,
													  SpelExpressionParser parser, BeanFactory beanFactory);
	}

	static String normalizeKey(String key, int entryIdx, ShortcutConfigurable argHints, Map<String, String> args) {
	    /*************************(1)*********************************/
		// RoutePredicateFactory has name hints and this has a fake key name
		// replace with the matching key hint
		if (key.startsWith(NameUtils.GENERATED_NAME_PREFIX) && !argHints.shortcutFieldOrder().isEmpty()
				&& entryIdx < args.size() && entryIdx < argHints.shortcutFieldOrder().size()) {
			key = argHints.shortcutFieldOrder().get(entryIdx);
		}
		return key;
	}

	static Object getValue(SpelExpressionParser parser, BeanFactory beanFactory, String entryValue) {
		/*************************(2)*********************************/

		Object value;
		String rawValue = entryValue;
		if (rawValue != null) {
			rawValue = rawValue.trim();
		}
		if (rawValue != null && rawValue.startsWith("#{") && entryValue.endsWith("}")) {
		/*************************(3)*********************************/

			// assume it's spel
			StandardEvaluationContext context = new StandardEvaluationContext();
			context.setBeanResolver(new BeanFactoryResolver(beanFactory));
			Expression expression = parser.parseExpression(entryValue, new TemplateParserContext());
			value = expression.getValue(context);
		} else {
			value = entryValue;
		}
		return value;
	}

	default ShortcutType shortcutType() {
		return ShortcutType.DEFAULT;
	}

	/**
	 * Returns hints about the number of args and the order for shortcut parsing.
	 * @return
	 */
	default List<String> shortcutFieldOrder() {
	/*************************(4)*********************************/

		return Collections.emptyList();
	}

	default String shortcutFieldPrefix() {
		return "";
	}

}
```

以上代码主要做了以下工作：

1）对键进行标准化处理，因为键有可能是自动生成，当键以_ genkey_ 开头时， 表明是自动生成的。

2）获取真实值，需要传入 Spring EL 解析器、Bean工厂等工具类。

3）对传入的 entryValue 是一个表达式的情况进行处理，这里默认是 Spring EL 表达式。

4）返回有关参数数量和解析顺序的提示。