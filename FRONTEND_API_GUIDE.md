# 프론트엔드 API 연결 가이드 (최신 버전)

## 📋 업데이트 내역

**2025-11-25 업데이트:**
- 모든 소셜 로그인 (카카오, 네이버, 구글)이 동일한 패턴으로 통일됨
- `/callback` 엔드포인트가 JSON 응답을 반환하도록 변경됨
- 모든 엔드포인트에서 일관된 응답 구조 사용

---

## 📌 기본 정보

- **API Gateway URL**: `http://localhost:8080`
- **프론트엔드 URL**: `http://localhost:3000`
- **CORS**: 이미 설정 완료 (localhost:3000 허용)
- **서브 라우터**: 모든 인증 API는 `/api/auth/**` 경로로 통일

---

## 🔐 소셜 로그인 API

모든 소셜 로그인 API는 **`/api/auth/{provider}/**`** 형식으로 통일되었습니다.

### 공통 API 패턴

모든 소셜 로그인 (카카오, 네이버, 구글)이 동일한 패턴을 따릅니다:

1. **인증 URL 생성**: `GET /api/auth/{provider}/auth-url`
2. **로그인 처리**: `POST /api/auth/{provider}/login`
3. **콜백 처리**: `GET /api/auth/{provider}/callback?code=...`

---

### 1. 카카오 로그인

#### 1-1. 인증 URL 가져오기
```javascript
// GET 요청
const response = await fetch('http://localhost:8080/api/auth/kakao/auth-url');
const data = await response.json();
// data.authUrl에 카카오 로그인 URL이 담겨있습니다
```

**요청 URL**: `GET http://localhost:8080/api/auth/kakao/auth-url`

**응답 예시**:
```json
{
  "authUrl": "https://kauth.kakao.com/oauth/authorize?client_id=...&redirect_uri=..."
}
```

#### 1-2. 로그인 버튼 클릭 시 동작
```javascript
const handleKakaoLogin = async () => {
  try {
    // 1. 인증 URL 가져오기
    const response = await fetch('http://localhost:8080/api/auth/kakao/auth-url', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const { authUrl } = await response.json();
    
    // 2. 새 창으로 카카오 로그인 페이지 열기
    window.open(
      authUrl,
      'kakao-login',
      'width=500,height=600,scrollbars=yes,resizable=yes'
    );
  } catch (error) {
    console.error('카카오 로그인 오류:', error);
    alert('카카오 로그인 요청 실패: ' + error.message);
  }
};
```

**콜백 URL**: `http://localhost:8080/api/auth/kakao/callback`
- 백엔드에서 JSON 응답을 반환합니다.
- 콜백은 OAuth 제공자가 자동으로 리다이렉트하므로, 프론트엔드에서 직접 처리하기 어렵습니다.

---

### 2. 네이버 로그인

#### 2-1. 인증 URL 가져오기
```javascript
// GET 요청
const response = await fetch('http://localhost:8080/api/auth/naver/auth-url');
const data = await response.json();
```

**요청 URL**: `GET http://localhost:8080/api/auth/naver/auth-url`

**응답 예시**:
```json
{
  "authUrl": "https://nid.naver.com/oauth2.0/authorize?client_id=...&redirect_uri=..."
}
```

#### 2-2. 로그인 버튼 클릭 시 동작
```javascript
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
    
    const { authUrl } = await response.json();
    
    window.open(
      authUrl,
      'naver-login',
      'width=500,height=600,scrollbars=yes,resizable=yes'
    );
  } catch (error) {
    console.error('네이버 로그인 오류:', error);
    alert('네이버 로그인 요청 실패: ' + error.message);
  }
};
```

**콜백 URL**: `http://localhost:8080/api/auth/naver/callback`
- 백엔드에서 JSON 응답을 반환합니다.

---

### 3. 구글 로그인

#### 3-1. 인증 URL 가져오기
```javascript
// GET 요청
const response = await fetch('http://localhost:8080/api/auth/google/auth-url');
const data = await response.json();
```

**요청 URL**: `GET http://localhost:8080/api/auth/google/auth-url`

**응답 예시**:
```json
{
  "authUrl": "https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=..."
}
```

#### 3-2. 로그인 버튼 클릭 시 동작
```javascript
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
    
    const { authUrl } = await response.json();
    
    window.open(
      authUrl,
      'google-login',
      'width=500,height=600,scrollbars=yes,resizable=yes'
    );
  } catch (error) {
    console.error('구글 로그인 오류:', error);
    alert('구글 로그인 요청 실패: ' + error.message);
  }
};
```

**콜백 URL**: `http://localhost:8080/api/auth/google/callback`
- 백엔드에서 JSON 응답을 반환합니다.

---

## 📋 API 엔드포인트 요약

| 소셜 로그인 | 인증 URL 엔드포인트 | 콜백 URL | 메서드 | 응답 형식 |
|------------|-------------------|---------|--------|----------|
| **카카오** | `GET /api/auth/kakao/auth-url` | `/api/auth/kakao/callback` | GET | JSON |
| **네이버** | `GET /api/auth/naver/auth-url` | `/api/auth/naver/callback` | GET | JSON |
| **구글** | `GET /api/auth/google/auth-url` | `/api/auth/google/callback` | GET | JSON |

---

## 📡 API 응답 구조

### 인증 URL 응답 (`/auth-url`)
```json
{
  "authUrl": "https://..."
}
```

### 로그인 응답 (`/login`)
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

### 콜백 응답 (`/callback`)
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

## 🔄 로그인 플로우

```
1. 사용자가 로그인 버튼 클릭
   ↓
2. 프론트엔드: GET /api/auth/{provider}/auth-url 호출
   ↓
3. 백엔드: 소셜 로그인 URL 반환 (JSON)
   ↓
4. 프론트엔드: 새 창으로 소셜 로그인 페이지 열기
   ↓
5. 사용자가 소셜 로그인 완료
   ↓
6. 소셜 서비스가 백엔드 콜백 URL로 리다이렉트
   ↓
7. 백엔드: 인증 처리 후 JSON 응답 반환
   ↓
8. 프론트엔드: 콜백 처리 (현재는 JSON 응답이므로 직접 처리 어려움)
```

**참고**: 현재 콜백은 JSON을 반환하므로, 프론트엔드에서 직접 처리하기 어렵습니다. 
콜백 처리 방법은 아래 "콜백 처리 방법" 섹션을 참고하세요.

---

## 💡 React 예시 코드

```jsx
import { useState } from 'react';

const SocialLogin = () => {
  const [isLoading, setIsLoading] = useState<string | null>(null);
  const API_BASE_URL = 'http://localhost:8080';

  const handleSocialLogin = async (provider: 'kakao' | 'naver' | 'google') => {
    setIsLoading(provider);
    
    try {
      // 모든 provider가 동일한 경로 형식 사용
      const response = await fetch(`${API_BASE_URL}/api/auth/${provider}/auth-url`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const { authUrl } = await response.json();
      
      // 새 창으로 로그인 페이지 열기
      window.open(
        authUrl,
        `${provider}-login`,
        'width=500,height=600,scrollbars=yes,resizable=yes'
      );
    } catch (error) {
      console.error(`${provider} 로그인 오류:`, error);
      alert(`${provider} 로그인 요청 실패: ${error.message}`);
    } finally {
      setIsLoading(null);
    }
  };

  return (
    <div>
      <button 
        onClick={() => handleSocialLogin('kakao')}
        disabled={isLoading === 'kakao'}
      >
        {isLoading === 'kakao' ? '로딩 중...' : '카카오 로그인'}
      </button>
      
      <button 
        onClick={() => handleSocialLogin('naver')}
        disabled={isLoading === 'naver'}
      >
        {isLoading === 'naver' ? '로딩 중...' : '네이버 로그인'}
      </button>
      
      <button 
        onClick={() => handleSocialLogin('google')}
        disabled={isLoading === 'google'}
      >
        {isLoading === 'google' ? '로딩 중...' : '구글 로그인'}
      </button>
    </div>
  );
};

export default SocialLogin;
```

---

## 🔄 콜백 처리 방법

현재 백엔드의 `/callback` 엔드포인트는 JSON을 반환합니다. 프론트엔드에서 콜백을 처리하는 방법:

### 방법 1: 폴링 방식 (권장)
로그인 창을 연 후, 주기적으로 로그인 상태를 확인합니다.

```javascript
const handleSocialLogin = async (provider) => {
  // 인증 URL 가져오기
  const response = await fetch(`${API_BASE_URL}/api/auth/${provider}/auth-url`);
  const { authUrl } = await response.json();
  
  // 새 창 열기
  const popup = window.open(authUrl, `${provider}-login`, 'width=500,height=600');
  
  // 폴링으로 로그인 상태 확인
  const checkInterval = setInterval(async () => {
    try {
      // 로그인 상태 확인 API 호출 (백엔드에 구현 필요)
      const statusResponse = await fetch(`${API_BASE_URL}/api/auth/${provider}/status`);
      const status = await statusResponse.json();
      
      if (status.loggedIn) {
        clearInterval(checkInterval);
        popup.close();
        // 로그인 성공 처리
        localStorage.setItem('accessToken', status.accessToken);
        localStorage.setItem('refreshToken', status.refreshToken);
      }
    } catch (error) {
      console.error('상태 확인 오류:', error);
    }
  }, 1000); // 1초마다 확인
  
  // 창이 닫히면 폴링 중지
  const closeCheckInterval = setInterval(() => {
    if (popup.closed) {
      clearInterval(checkInterval);
      clearInterval(closeCheckInterval);
    }
  }, 500);
};
```

### 방법 2: 백엔드에서 세션/쿠키 사용
백엔드에서 로그인 성공 시 세션이나 쿠키를 설정하고, 프론트엔드에서 이를 확인합니다.

### 방법 3: WebSocket 또는 Server-Sent Events
실시간으로 로그인 상태를 전달받는 방식입니다.

---

## 🎯 프론트엔드에서 사용할 URL 목록

### 카카오 로그인
- **인증 URL 요청**: `GET http://localhost:8080/api/auth/kakao/auth-url`
- **콜백 URL** (카카오 개발자 콘솔에 등록): `http://localhost:8080/api/auth/kakao/callback`
- **프론트엔드 콜백 경로**: `http://localhost:3000/auth/kakao`

### 네이버 로그인
- **인증 URL 요청**: `GET http://localhost:8080/api/auth/naver/auth-url`
- **콜백 URL** (네이버 개발자 센터에 등록): `http://localhost:8080/api/auth/naver/callback`
- **프론트엔드 콜백 경로**: `http://localhost:3000/auth/naver`

### 구글 로그인
- **인증 URL 요청**: `GET http://localhost:8080/api/auth/google/auth-url`
- **콜백 URL** (구글 클라우드 콘솔에 등록): `http://localhost:8080/api/auth/google/callback`
- **프론트엔드 콜백 경로**: `http://localhost:3000/auth/google`

---

## ⚠️ 주의사항

1. **CORS 설정**: 이미 백엔드에서 `localhost:3000`을 허용하도록 설정되어 있습니다.

2. **토큰 저장**: 
   - 현재는 `localStorage`에 저장하는 예시를 보여드렸지만, 실제 프로덕션에서는 보안을 고려하여 `httpOnly` 쿠키 사용을 권장합니다.

3. **에러 처리**: 
   - 네트워크 오류, 인증 실패 등 모든 경우에 대한 에러 처리를 구현하세요.

4. **서브 라우터 구조**: 
   - 모든 인증 API는 `/api/auth/**` 경로로 통일되었습니다.
   - Gateway에서 `/api/auth/**` 요청을 받아 `authservice`로 라우팅합니다.

5. **콜백 처리**: 
   - 현재 콜백은 JSON을 반환하므로, 프론트엔드에서 직접 처리하기 어렵습니다.
   - 위의 "콜백 처리 방법" 섹션을 참고하여 구현하세요.

---

## 🔄 변경 사항 (2025-11-25)

### 주요 변경사항
1. **통일된 API 패턴**: 모든 소셜 로그인 (카카오, 네이버, 구글)이 동일한 패턴으로 동작
2. **JSON 응답**: `/callback` 엔드포인트가 HTML 대신 JSON을 반환
3. **일관된 응답 구조**: 모든 엔드포인트에서 동일한 응답 구조 사용

### 영향받는 부분
- 콜백 처리 로직이 변경되었으므로, 프론트엔드에서 콜백을 처리하는 방식을 업데이트해야 할 수 있습니다.
- 모든 소셜 로그인이 동일한 패턴을 따르므로, 코드 재사용이 더 쉬워졌습니다.

---

## 📞 문의

문제가 발생하거나 추가 기능이 필요한 경우 백엔드 팀에 문의해주세요.

**Swagger UI**: `http://localhost:8080/swagger-ui.html`
