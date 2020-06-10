# JWT token

JWT (JSON WEB TOKEN ) 是一种使用 JSON 格式来规约TOKEN 或者 SESSION 的协议,由于传统认证方式免不了会生成一个凭证,这个凭证可以是 token或者是 session,保存于服务端或者其他持久化工具中,这样一来,凭证的存取就显得非常麻烦,JWT 的出现打破了这个瓶颈,实现了"客户端 SESSION"的愿景

- Header 头部 :  指定 JWT 使用的签名算法
- payload 荷载 :  包含一些自定义与非自定义的认证信息
- signature 签名 : 将头部与载荷使用"."连接,使用头部的签名算法生成签名信息并拼装到末尾

![img](assets/jwt.png)



至此，三个部分，都解释完了，那么按照header.payload.signature这个格式串起来就行了，串之前注意，header和payload也要做一个base64url encoded的转换。那么最终拼出来的一个例子是：



eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiMDhmODZhZi0zNWRhLTQ4ZjItOGZhYi1jZWYzOTA0NjYwYmQifQ.-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM
