# Diary
일기장

## 설치 방법

### DB 초기화

1. /db/init.sql을 실행한다.
2. 만약 다른 곳에서 일기를 옮겨오고자 할 경우 우선 txt 파일로 내보낸 뒤 importFromTXT.js를 실행한다.

### 서버 설치

1. /server/.env에 포트와 DB 정보, 통신에 사용할 비밀 키를 입력한다.
2. npm install 후 node idnex.js로 실행한다.

### 안드로이드 프로젝트 설정

1. cd android/app/src/main
2. res/xml/network_security_config.xml 파일을 열고 도메인 정보를 수정한다.
3. java/.../APIInterface.kt 파일을 열고 도메인 정보와 포트를 입력한다.
4. java/.../APICall.kt 파일을 열고 서버에서 설정한 비밀 키를 입력한다.
