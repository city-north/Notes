# 030-Spring内建PropertyEditor拓展

[TOC]

## 内建拓展(org.springframework.beans.propertyeditors包下)

| 转换场景            | 实现类                                                       |
| ------------------- | ------------------------------------------------------------ |
| String ->  Byte数组 | org.springframework.beans.propertyeditors.ByteArrayPropertyEditor |
| String -> Char      | org.springframework.beans.propertyeditors.CharacterEditor    |
| String -> Char数组  | org.springframework.beans.propertyeditors.CharArrayPropertyEditor |
| String -> Charset   | org.springframework.beans.propertyeditors.CharsetEditor      |
| String -> Currency  | org.springframework.beans.propertyeditors.CurrencyEditor     |

