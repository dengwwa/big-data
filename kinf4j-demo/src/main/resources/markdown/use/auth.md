# 接口鉴权

## 1. 鉴权规则
	    在 Http Request Header 添加以下属性：
            | 参数  | 值            | 说明         |
            |-------|--------------|-------------|
            | AK    | 'ak'         | 访问密钥     |
            | TIME  | '1741097280' | 时间戳       |
            | SIGN  | 'sign'       | 签名         |

#### &nbsp;&nbsp;&nbsp;&nbsp;(1) AK/SK
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;找 “徐诚毅” 申请。

#### &nbsp;&nbsp;&nbsp;&nbsp;(2) TIME
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间戳（秒），例如 “1741097280”。<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注意，中台会检查时间戳是否过期，要使用最新的时间搓。

#### &nbsp;&nbsp;&nbsp;&nbsp;(3) SIGN
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;描述：用于校验合法性的签名。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;生成方式：
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. 构造字符串："{ak}&{sk}&{time}"
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. 使用 MD5（generate_sign）加密上述字符串，即得到签名（sign）。

## 2. generate_sign 示例

####  &nbsp;&nbsp;&nbsp;&nbsp;java 示例
>		public static String generate_sign(String inputString) {
>
>	            // 创建 MD5 哈希对象
>	            MessageDigest md = MessageDigest.getInstance("MD5");
>
>	            // 更新哈希对象
>	            md.update(inputString.getBytes("UTF-8"));
>
>	            // 获取哈希值的字节数组
>	            byte[] digest = md.digest();
>	
>	            // 将字节数组转换为十六进制字符串
>	            StringBuilder hashHex = new StringBuilder();
>	            for (byte b : digest) {
>	                hashHex.append(String.format("%02x", b));
>	            }
>	
>	            return hashHex.toString();
>	    }
>
####  &nbsp;&nbsp;&nbsp;&nbsp;Python 示例
>		def generate_sign(input_string):
>		    hash_object = hashlib.md5()
>		
>		    # 更新哈希对象，注意需要编码成字节
>		    hash_object.update(input_string.encode('utf-8'))
>		
>		    # 获取十六进制哈希值
>		    hash_hex = hash_object.hexdigest()
>		    return hash_hex
>
>
####  &nbsp;&nbsp;&nbsp;&nbsp;Go 示例
>		import (
>			"crypto/md5"
>			"encoding/hex"
>			"fmt"
>		)
>		
>		func generate_sign(inputString string) string {
>			// 创建 MD5 哈希
>			hash := md5.New()
>		
>			// 写入数据
>			hash.Write([]byte(inputString))
>		
>			// 计算哈希值
>			hashBytes := hash.Sum(nil)
>		
>			// 转换为十六进制字符串
>			hashHex := hex.EncodeToString(hashBytes)
>		
>			return hashHex
>		}