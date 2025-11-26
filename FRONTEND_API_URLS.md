# 프론트엔드 API URL 가이드

## 📌 기본 정보

- **API Gateway Base URL**: `http://localhost:8080`
- **프론트엔드 Base URL**: `http://localhost:3000`
- **CORS**: 설정 완료 (`localhost:3000`, `127.0.0.1:3000` 허용)

---

## 🔗 API 엔드포인트 목록

### 1. Health Check

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `http://localhost:8080/api/health` | 서버 상태 확인 |

**응답 예시**:
```json
{
  "status": "UP",
  "message": "API Gateway is running"
}
```

---

### 2. 카카오 로그인

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `http://localhost:8080/api/auth/kakao/auth-url` | 카카오 로그인 URL 생성 |
| POST | `http://localhost:8080/api/auth/kakao/login` | 카카오 로그인 처리 |
| GET | `http://localhost:8080/api/auth/kakao/callback` | 카카오 로그인 콜백 (자동 처리) |

**인증 URL 요청 응답**:
```json
{
  "authUrl": "https://kauth.kakao.com/oauth/authorize?client_id=...&redirect_uri=..."
}
```

**콜백 URL** (카카오 개발자 콘솔에 등록 필요):
```
http://localhost:8080/api/auth/kakao/callback
```

**프론트엔드 콜백 경로**:
```
http://localhost:3000/auth/kakao
```

---

### 3. 네이버 로그인

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `http://localhost:8080/api/auth/naver/auth-url` | 네이버 로그인 URL 생성 |
| POST | `http://localhost:8080/api/auth/naver/login` | 네이버 로그인 처리 |
| GET | `http://localhost:8080/api/auth/naver/callback` | 네이버 로그인 콜백 (자동 처리) |

**인증 URL 요청 응답**:
```json
{
  "authUrl": "https://nid.naver.com/oauth2.0/authorize?client_id=...&redirect_uri=..."
}
```

**콜백 URL** (네이버 개발자 센터에 등록 필요):
```
http://localhost:8080/api/auth/naver/callback
```

**프론트엔드 콜백 경로**:
```
http://localhost:3000/auth/naver
```

---

### 4. 구글 로그인

| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `http://localhost:8080/api/auth/google/auth-url` | 구글 로그인 URL 생성 |
| POST | `http://localhost:8080/api/auth/google/login` | 구글 로그인 처리 |
| GET | `http://localhost:8080/api/auth/google/callback` | 구글 로그인 콜백 (자동 처리) |

**인증 URL 요청 응답**:
```json
{
  "authUrl": "https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=..."
}
```

**콜백 URL** (구글 클라우드 콘솔에 등록 필요):
```
http://localhost:8080/api/auth/google/callback
```

**프론트엔드 콜백 경로**:
```
http://localhost:3000/auth/google
```

---

## 🚀 빠른 시작 예제

### React/Next.js 예제

```typescript
// API Base URL
const API_BASE_URL = 'http://localhost:8080';

// 카카오 로그인
const handleKakaoLogin = async () => {
  try {
    // 1. 인증 URL 가져오기
    const response = await fetch(`${API_BASE_URL}/api/auth/kakao/auth-url`);
    const { authUrl } = await response.json();
    
    // 2. 새 창으로 로그인 페이지 열기
    window.open(authUrl, 'kakao-login', 'width=500,height=600');
    
    // 3. 콜백 메시지 수신
    window.addEventListener('message', (event) => {
      if (event.origin !== 'http://localhost:3000') return;
      
      const { success, accessToken, refreshToken, provider } = event.data;
      if (success && provider === 'kakao') {
        // 로그인 성공 처리
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        // 리다이렉트 또는 상태 업데이트
      }
    });
  } catch (error) {
    console.error('카카오 로그인 오류:', error);
  }
};

// 네이버 로그인
const handleNaverLogin = async () => {
  const response = await fetch(`${API_BASE_URL}/api/auth/naver/auth-url`);
  const { authUrl } = await response.json();
  window.open(authUrl, 'naver-login', 'width=500,height=600');
};

// 구글 로그인
const handleGoogleLogin = async () => {
  const response = await fetch(`${API_BASE_URL}/api/auth/google/auth-url`);
  const { authUrl } = await response.json();
  window.open(authUrl, 'google-login', 'width=500,height=600');
};
```

---

## 📋 URL 요약표

### 인증 URL 요청
| Provider | URL |
|----------|-----|
| 카카오 | `GET http://localhost:8080/api/auth/kakao/auth-url` |
| 네이버 | `GET http://localhost:8080/api/auth/naver/auth-url` |
| 구글 | `GET http://localhost:8080/api/auth/google/auth-url` |

### 콜백 URL (백엔드)
| Provider | URL |
|----------|-----|
| 카카오 | `http://localhost:8080/api/auth/kakao/callback` |
| 네이버 | `http://localhost:8080/api/auth/naver/callback` |
| 구글 | `http://localhost:8080/api/auth/google/callback` |

### 프론트엔드 콜백 경로
| Provider | 경로 |
|----------|-----|
| 카카오 | `http://localhost:3000/auth/kakao` |
| 네이버 | `http://localhost:3000/auth/naver` |
| 구글 | `http://localhost:3000/auth/google` |

---

## ⚙️ 환경 변수 설정

프론트엔드 `.env` 파일에 다음 변수를 설정하세요:

```env
# API Base URL
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080

# 카카오 (필요시)
NEXT_PUBLIC_KAKAO_CLIENT_ID=your_kakao_client_id

# 네이버 (필요시)
NEXT_PUBLIC_NAVER_CLIENT_ID=your_naver_client_id
```

---

## 🔒 CORS 설정

- **허용된 Origin**: 
  - `http://localhost:3000`
  - `http://127.0.0.1:3000`
- **허용된 메서드**: GET, POST, PUT, DELETE, OPTIONS, PATCH
- **허용된 헤더**: `*` (모든 헤더)
- **Credentials**: 허용

---

## 📝 중요 사항

1. **소셜 로그인 개발자 콘솔 설정**
   - 각 소셜 로그인 제공자의 개발자 콘솔에서 위의 콜백 URL을 등록해야 합니다.
   - 카카오: [Kakao Developers](https://developers.kakao.com/)
   - 네이버: [Naver Developers](https://developers.naver.com/)
   - 구글: [Google Cloud Console](https://console.cloud.google.com/)

2. **postMessage 보안**
   - 반드시 `event.origin`을 확인하여 신뢰할 수 있는 origin에서만 메시지를 수신하세요.
   - 현재 설정: `http://localhost:3000`

3. **토큰 저장**
   - 예시에서는 `localStorage`를 사용했지만, 프로덕션에서는 보안을 고려하여 `httpOnly` 쿠키 사용을 권장합니다.

---

## 🔍 Swagger UI

API 문서 및 테스트:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

---

## 📞 문의

문제가 발생하거나 추가 기능이 필요한 경우 백엔드 팀에 문의해주세요.

