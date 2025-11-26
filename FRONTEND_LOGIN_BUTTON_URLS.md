# 프론트엔드 로그인 버튼 URL 가이드 (최신 버전)

## 📋 업데이트 내역

**2025-11-25 업데이트:**
- 모든 소셜 로그인 (카카오, 네이버, 구글)이 동일한 패턴으로 통일됨
- `/callback` 엔드포인트가 JSON 응답을 반환하도록 변경됨
- 모든 엔드포인트에서 일관된 응답 구조 사용

---

## ⚠️ 중요: 정확한 URL 확인

프론트엔드에서 로그인 버튼을 클릭할 때 사용할 **정확한 URL**입니다.

### ✅ 확인된 작동 URL

모든 API는 Gateway(`http://localhost:8080`)를 통해 접근해야 합니다.

---

## 1. 카카오 로그인 버튼

**버튼 클릭 시 호출할 URL:**
```
GET http://localhost:8080/api/auth/kakao/auth-url
```

**JavaScript/TypeScript 예제:**
```typescript
const handleKakaoLogin = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/kakao/auth-url', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    
    // data.authUrl에 카카오 로그인 페이지 URL이 담겨있음
    window.open(data.authUrl, 'kakao-login', 'width=500,height=600');
  } catch (error) {
    console.error('카카오 로그인 오류:', error);
    alert('카카오 로그인 요청 실패: ' + error.message);
  }
};
```

**응답 예시:**
```json
{
  "authUrl": "https://kauth.kakao.com/oauth/authorize?client_id=...&redirect_uri=..."
}
```

**콜백 처리:**
카카오 로그인 완료 후, OAuth 제공자가 `http://localhost:8080/api/auth/kakao/callback?code=...`로 리다이렉트합니다.
콜백 엔드포인트는 JSON을 반환하므로, 프론트엔드에서 직접 처리하기 어렵습니다.

**권장 방법:**
1. 로그인 버튼 클릭 → `/auth-url` 호출 → 새 창에서 인증 URL 열기
2. 사용자가 로그인 완료 후, 콜백 URL로 리다이렉트됨
3. 콜백 페이지에서 JSON 응답을 받아 처리하거나, 프론트엔드로 메시지 전달

---

## 2. 네이버 로그인 버튼

**버튼 클릭 시 호출할 URL:**
```
GET http://localhost:8080/api/auth/naver/auth-url
```

**JavaScript/TypeScript 예제:**
```typescript
const handleNaverLogin = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/naver/auth-url', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    
    // data.authUrl에 네이버 로그인 페이지 URL이 담겨있음
    window.open(data.authUrl, 'naver-login', 'width=500,height=600');
  } catch (error) {
    console.error('네이버 로그인 오류:', error);
    alert('네이버 로그인 요청 실패: ' + error.message);
  }
};
```

**응답 예시:**
```json
{
  "authUrl": "https://nid.naver.com/oauth2.0/authorize?client_id=...&redirect_uri=..."
}
```

**콜백 처리:**
네이버 로그인 완료 후, OAuth 제공자가 `http://localhost:8080/api/auth/naver/callback?code=...`로 리다이렉트합니다.
콜백 엔드포인트는 JSON을 반환하므로, 프론트엔드에서 직접 처리하기 어렵습니다.

---

## 3. 구글 로그인 버튼

**버튼 클릭 시 호출할 URL:**
```
GET http://localhost:8080/api/auth/google/auth-url
```

**JavaScript/TypeScript 예제:**
```typescript
const handleGoogleLogin = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/google/auth-url', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    
    // data.authUrl에 구글 로그인 페이지 URL이 담겨있음
    window.open(data.authUrl, 'google-login', 'width=500,height=600');
  } catch (error) {
    console.error('구글 로그인 오류:', error);
    alert('구글 로그인 요청 실패: ' + error.message);
  }
};
```

**응답 예시:**
```json
{
  "authUrl": "https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=..."
}
```

**콜백 처리:**
구글 로그인 완료 후, OAuth 제공자가 `http://localhost:8080/api/auth/google/callback?code=...`로 리다이렉트합니다.
콜백 엔드포인트는 JSON을 반환하므로, 프론트엔드에서 직접 처리하기 어렵습니다.

---

## 전체 예제 코드 (React/TypeScript)

```typescript
import React, { useState } from 'react';

const API_BASE_URL = 'http://localhost:8080';

const LoginPage: React.FC = () => {
  const [loading, setLoading] = useState<string | null>(null);

  // 카카오 로그인
  const handleKakaoLogin = async () => {
    setLoading('kakao');
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/kakao/auth-url`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const { authUrl } = await response.json();
      window.open(authUrl, 'kakao-login', 'width=500,height=600');
    } catch (error) {
      console.error('카카오 로그인 오류:', error);
      alert('카카오 로그인 요청 실패: ' + error.message);
    } finally {
      setLoading(null);
    }
  };

  // 네이버 로그인
  const handleNaverLogin = async () => {
    setLoading('naver');
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/naver/auth-url`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const { authUrl } = await response.json();
      window.open(authUrl, 'naver-login', 'width=500,height=600');
    } catch (error) {
      console.error('네이버 로그인 오류:', error);
      alert('네이버 로그인 요청 실패: ' + error.message);
    } finally {
      setLoading(null);
    }
  };

  // 구글 로그인
  const handleGoogleLogin = async () => {
    setLoading('google');
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/google/auth-url`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const { authUrl } = await response.json();
      window.open(authUrl, 'google-login', 'width=500,height=600');
    } catch (error) {
      console.error('구글 로그인 오류:', error);
      alert('구글 로그인 요청 실패: ' + error.message);
    } finally {
      setLoading(null);
    }
  };

  return (
    <div className="login-page">
      <h1>로그인</h1>
      
      <button 
        onClick={handleKakaoLogin} 
        disabled={loading === 'kakao'}
      >
        {loading === 'kakao' ? '로딩 중...' : '카카오 로그인'}
      </button>
      
      <button 
        onClick={handleNaverLogin} 
        disabled={loading === 'naver'}
      >
        {loading === 'naver' ? '로딩 중...' : '네이버 로그인'}
      </button>
      
      <button 
        onClick={handleGoogleLogin} 
        disabled={loading === 'google'}
      >
        {loading === 'google' ? '로딩 중...' : '구글 로그인'}
      </button>
    </div>
  );
};

export default LoginPage;
```

---

## 📡 API 엔드포인트 상세 정보

### 공통 응답 구조

모든 소셜 로그인 API는 동일한 패턴을 따릅니다:

#### 1. 인증 URL 생성 (`/auth-url`)
- **메서드**: `GET`
- **경로**: `/api/auth/{provider}/auth-url`
- **응답**:
```json
{
  "authUrl": "https://..."
}
```

#### 2. 로그인 (`/login`)
- **메서드**: `POST`
- **경로**: `/api/auth/{provider}/login`
- **요청 본문**:
```json
{
  "code": "인증_코드",
  "redirectUri": "http://localhost:8080/api/auth/{provider}/callback"
}
```
- **응답**:
```json
{
  "success": true,
  "message": "{provider} 로그인 성공",
  "accessToken": "access-token-...",
  "refreshToken": "refresh-token-...",
  "userData": {
    "code": "인증_코드",
    "redirectUri": "http://localhost:8080/api/auth/{provider}/callback"
  }
}
```

#### 3. 콜백 (`/callback`)
- **메서드**: `GET`
- **경로**: `/api/auth/{provider}/callback?code=...`
- **응답**:
```json
{
  "success": true,
  "message": "{provider} 로그인 성공",
  "accessToken": "access-token-...",
  "refreshToken": "refresh-token-...",
  "userData": {
    "code": "인증_코드",
    "redirectUri": "http://localhost:8080/api/auth/{provider}/callback"
  }
}
```

---

## 🔍 문제 해결 가이드

### 404 Not Found 오류가 발생하는 경우

1. **URL 확인**: 반드시 `http://localhost:8080/api/auth/{provider}/auth-url` 형식을 사용하세요.
   - ✅ 올바른 예: `http://localhost:8080/api/auth/kakao/auth-url`
   - ❌ 잘못된 예: `http://localhost:8081/api/auth/kakao/auth-url` (직접 authservice 접근)
   - ❌ 잘못된 예: `http://localhost:8080/auth/kakao/auth-url` (`/api` 누락)

2. **Gateway 확인**: Gateway가 실행 중인지 확인하세요.
   ```bash
   docker compose ps gateway
   ```

3. **CORS 확인**: 브라우저 콘솔에서 CORS 오류가 있는지 확인하세요.

### CORS 오류가 발생하는 경우

Gateway에서 CORS가 이미 설정되어 있습니다. 만약 문제가 있다면:
- 브라우저 개발자 도구의 Network 탭에서 실제 요청 헤더를 확인하세요.
- `Origin: http://localhost:3000` 헤더가 포함되어 있는지 확인하세요.

### 네트워크 오류가 발생하는 경우

1. **Docker 컨테이너 확인**:
   ```bash
   docker compose ps
   ```
   모든 서비스가 `Up` 상태인지 확인하세요.

2. **로그 확인**:
   ```bash
   docker compose logs gateway --tail=50
   docker compose logs authservice --tail=50
   ```

### 콜백 처리 문제

현재 콜백 엔드포인트는 JSON을 반환합니다. 프론트엔드에서 콜백을 처리하려면:

1. **방법 1: 콜백 페이지에서 처리**
   - 콜백 URL이 리다이렉트되면, 해당 페이지에서 JSON 응답을 받아 처리
   - 필요시 프론트엔드로 메시지 전달

2. **방법 2: 폴링 방식**
   - 로그인 창을 연 후, 주기적으로 로그인 상태를 확인
   - 백엔드에서 세션이나 토큰을 확인하여 로그인 완료 여부 판단

3. **방법 3: WebSocket 또는 Server-Sent Events**
   - 실시간으로 로그인 상태를 전달받는 방식

---

## 📝 요약

| 제공자 | 엔드포인트 | HTTP 메서드 | 설명 |
|--------|-----------|------------|------|
| 카카오 | `/api/auth/kakao/auth-url` | GET | 카카오 로그인 URL 생성 |
| 네이버 | `/api/auth/naver/auth-url` | GET | 네이버 로그인 URL 생성 |
| 구글 | `/api/auth/google/auth-url` | GET | 구글 로그인 URL 생성 |

**모든 요청은 Gateway(`http://localhost:8080`)를 통해 접근해야 합니다.**

---

## 🔄 변경 사항 (2025-11-25)

### 주요 변경사항
1. **통일된 API 패턴**: 모든 소셜 로그인 (카카오, 네이버, 구글)이 동일한 패턴으로 동작
2. **JSON 응답**: `/callback` 엔드포인트가 HTML 대신 JSON을 반환
3. **일관된 응답 구조**: 모든 엔드포인트에서 동일한 응답 구조 사용

### 영향받는 부분
- 콜백 처리 로직이 변경되었으므로, 프론트엔드에서 콜백을 처리하는 방식을 업데이트해야 할 수 있습니다.
- 모든 소셜 로그인이 동일한 패턴을 따르므로, 코드 재사용이 더 쉬워졌습니다.
