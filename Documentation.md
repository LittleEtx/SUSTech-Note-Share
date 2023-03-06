# Project前后端接口文档

NOTE:若接口标题处没有标注`（无Token）`，则表示该接口访问请求会被拦截，
需要请求头中包含`token`并且验证成功后才能成功请求。验证成功后，token的有效期会被刷新
（`60分钟`或者`7天`，基于该token生成时是否选择了记住用户）。

## 登录相关接口

* ### 密码登录（无Token）

`/api/auth/login/password-login`

#### 参数

| 参数名      | 类型     | 说明     |
|----------|--------|--------|
| email    | string | 用户邮箱   |
| password | string | 密码     |
| remember | bool   | 是否记住用户 |

#### 说明

若登录成功，返回由SWT生成的token。
如果该用户已经存在一个有效的token，将其置为无效。
如果`remember`的值为`true`，将token的有效期设置为`7天`，
否则设置`60分钟`。

* ### 获取验证码（无Token）

`/api/auth/login/get-email-code`

#### 参数

| 参数名   | 类型     | 说明   |
|-------|--------|------|
| email | string | 用户邮箱 |

#### 说明

向对应的邮箱发送验证码，验证码有效期为`15分钟`。
需要验证邮箱后缀是否为南科大校内邮箱（即`@mail.sustech.edu.cn`或者`@sustech.edu.cn`）。
向一个邮箱发送验证码后，需至少间隔`60秒`才能再次向该发送验证码。

* ### 邮箱验证码登录（无Token）

`/api/auth/login/email-code-login`

#### 参数

| 参数名      | 类型     | 说明     |
|----------|--------|--------|
| email    | string | 用户邮箱   |
| code     | string | 验证码    |
| remember | bool   | 是否记住用户 |

#### 说明

如果该邮箱未注册，将自动注册一个新用户。新用户的用户名会被后端指定。

若登录或者注册成功，返回由SWT生成的token。
如果该用户已经存在一个有效的token，将其置为无效。
如果`remember`的值为`true`，将token的有效期设置为`7天`，
否则设置`60分钟`。

* ### 登出

`/api/auth/logout`

#### 说明

无参数。收到请求后，将token置为无效。

## 找回密码相关接口

* ### Step1 确认邮箱存在（无Token）

`/api/auth/reset-password/confirm-email`

#### 参数

| 参数名   | 类型     | 说明   |
|-------|--------|------|
| email | string | 用户邮箱 |

#### 说明

返回该邮箱是否存在，即是否有已注册账户使用邮箱。
若邮箱存在，向该邮箱发送重置密码专用的验证码。验证码有效期为`15分钟`。
向一个邮箱发送验证码后，需至少间隔`60秒`才能再次发送验证码。

* ### Step2 验证邮箱（无Token）

`/api/auth/reset-password/verify-email`

#### 参数

| 参数名   | 类型     | 说明    |
|-------|--------|-------|
| email | string | 用户邮箱  |
| code  | string | 邮箱验证码 |

#### 说明

验证邮箱以及对应的验证码是否正确。正确的话会返回一个重置密码的专用token。
token的有效期为`15分钟`。若该邮箱还存在其他未过期的token，将其置为无效。

* ### Step3 重置密码（无Token）

`/api/auth/reset-password/reset-password`

#### 参数

| 参数名      | 类型     | 说明           |
|----------|--------|--------------|
| token    | string | 重置密码的专用token |
| password | string | 新密码          |

#### 说明

返回重置密码是否成功。若成功重置密码，将该token置为无效。
若密码不符合标准（如和原密码相同、长度小于6位等），将返回失败。