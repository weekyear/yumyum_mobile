# User

### 1. 회원가입

```kotlin
POST ("/user/signup")

1) **프로필 사진을 업로드 할 때**
**{
	"email": "ssafy@gmail.com"
	"nickname": "김싸피"
	"introduction": "한줄로 자신을 소개해주세요"
	"profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
}

2) 프로필 사진을 업로드 하지 않을 때
{
	"email": "ssafy@gmail.com"
	"nickname": "김싸피"
	"introduction": "안녕하세요"
	"profilePath": null
}**
```

```kotlin
1) **성공 (프로필 있음)
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}

2**) **성공 (프로필 없음)
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": ""
  }
}

3**) **입력값 유효하지 않음 (예: 이메일)
{
  "status": 200,
	"error": "Invalid Parameter",
	"code": "C001",
  "message": "Email is Invalid"
}

4**) **이메일 중복
{
  "status": 200,
	"error": "Entity is Duplicated",
	"code": "C004",
  "message": "Email is Duplicated"
}**
```

### 2. 이메일 중복 확인

```kotlin
GET ("/user/email/{email}")
```

```kotlin
1) **성공 (중복 없음)
{
  "status": "200",
  "message": "success",
  "data": {
		"existence": false
	}
}

2**) **성공 (중복 있음)
{
  "status": "200",
  "message": "success",
  "data": {
		"existence": true
	}
}**
```

### 3. 로그인

```kotlin
GET ("/user/login/{email}")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 4. 프로필 이미지 업로드

```kotlin
POST ("/user/profile")
```

```kotlin
1) **성공
{
  "status": "200",
  "message": "success",
  "data": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
}**
```

### 5. 회원 수정

```kotlin
PUT ("/user")

{
  "id": 1,
  "**nickname": "김싸피"
	"introduction": "한줄로 자신을 소개해주세요"
	"profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"**
}
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 6. 회원 조회

```kotlin
GET ("/user/{userId}")
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": {
    "id": 1,
    "email": "ssafy@naver.com",
    "nickname": "김싸피",
    "introduction": "한줄로 자신을 소개해주세요.",
    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
  }
}**
```

### 7. 회원 삭제

```kotlin
DELETE ("/user/{userId}")
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 8. 팔로우 목록 조회

```kotlin
GET ("/user/follow/list/{userId}")
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": [
    {
	    "id": 1,
	    "email": "ssafy@naver.com",
	    "nickname": "김싸피",
	    "introduction": "한줄로 자신을 소개해주세요.",
	    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
	  },
		{
	    "id": 2,
	    "email": "hong@naver.com",
	    "nickname": "홍길동",
	    "introduction": "안녕하세요.",
	    "profilePath": ""
	  },
	]
}**
```

### 9. 팔로워 목록 조회

```kotlin
GET ("/user/follow/follower/{userId}")
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": [
    {
	    "id": 1,
	    "email": "ssafy@naver.com",
	    "nickname": "김싸피",
	    "introduction": "한줄로 자신을 소개해주세요.",
	    "profilePath": "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
	  },
		{
	    "id": 2,
	    "email": "hong@naver.com",
	    "nickname": "홍길동",
	    "introduction": "안녕하세요.",
	    "profilePath": ""
	  },
	]
}**
```

### 10. 팔로우

```kotlin
POST ("/user/follow")

{
	"hostId": 1,
	"followerId": 2
}
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": null
}**
```

### 11. 팔로우 취소

```kotlin
DELETE ("/user/follow/follower/{hostId}/{followerId}")
```

```kotlin
1) **성공한 경우
{
  "status": "200",
  "message": "success",
  "data": null
}**
```
