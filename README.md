#시작하기 전에

1.  이 프로젝트는 svelte4버전으로 만들어 졌습니다. svelte5 아닙니다.
2.  Svelte와 SvelteKit 을 써보셨나요? 모르시면 어느정도 스터디하셔야 합니다.
3.  Svelte는 React, Vue보다 쉽다고 무시하진 마세요. 저는 머리가 나빠서 익숙해 지는데 1달 정도 걸렸어요.
4.  SvelteKit은 Next, Nuxt보다 쉽다고 하지만 Svelte보다 두 배는 어렵습니다. 역시 익숙해지는데 2달 정도 걸렸구요.
5.  처음 프로젝트를 실행할 때 1, 2번까지 완료되야 합니다.
6.  감이 오면 3번 git 연동하고 본격적인 개발작업 시작하세요.

## 개발환경

(폐쇄망에서는 NEXUS 환경이 반드시 필요합니다.)

- node.js 16.14.0 / npm 8.3.1
- openjdk-21.0.1+12
- apache-tomcat-10.1.18
- SpringBoot 4.21.0
- Oracle/MySql
- thymeleaf
- Sveltekit 1.25.0 / Vite 4.4.9

# 1. SpringbootSvelteStart > [신규] 프로젝트 변경하기

##### 이클립스 Package Exlplorer 에서 프로젝트 복사하기

- springboot-svelte-starter 복사 붙여넣기 > 변경(f2) > [신규] 프로젝트ID

##### 소스코드에서 [신규] 프로젝트ID 변경

- STS나 이클립스에서 프로젝트를 추가한다.
  import > Exist Maven Project >
- Package Explorer에서 com.aaboo.svelteSpringbootStarter.\*\* 패키지명을 모두 바꿔준다.
- Ctrl+h
  contains text: "com.aaboo.svelteSpringbootStarter" 로 모두 찾아서 [신규] 프로젝트ID 로 변경
- README.md 도 수정
- /pom.xml 수정 후 [프로젝트 우클릭] > maven > update project
- ./front-end-svelte/src/lib/app.js 에서 this.name = **"svelteSpringbootStarter"**; 와 port 정보도 수정필요

##### 이클립스 [Servers] 신규서버 생성

- Servers > new > Server > Apache > Tomcat v10.1 Server
  > Server name : [신규]프로젝트ID > 오른쪽 칸으로 이동

##### [svelteSpringbootStarter 서버 설정파일 창] 상세설정 변경

- Timeouts > start 999초로 변경
- Moudule탭 > edit > path > /ROOT 를 / 로 변경 저장(ctrl+s)
- Open launch configuration > arguments탭 > VM arguments에 아래 문구 추가(Application.java 참조)

```
  -Dspring.profiles.active=local
```

# 2. sveltekit 설치 및 환경설정

### svelte가 이미 설치되어 있다면

```
npm install
npm run dev
```

npm WARN deprecated 은 모듈내의 의존성 문제이기 때문에 무시한다.

### sveltekit 설치 참고

(본 소스는 svelte4 기준으로 개발되어 있음. 새로 설치할 경우 svelte5로 개발해야함)

```bash
cd /svelte-springboot-starter
npm create svelte@latest front-end-svlete

- Which Svelte app template? > Skeleton project
- Add type checking with TypeScript? > Yes, using JavaScript With JSDoc comments
- Select additional options
  > Add ESLint for code linting
  > Add Prettier for code formatting

cd front-end-svelte
npm install //기본패키지 설치
npm i -D @sveltejs/adapter-static //배포용 모듈 추가
```

## 2-1. 소스위치 가이드

아래와 같이 구성되었는지 먼저 확인합니다.

##### BACKEND 구성

```
/springboot-svelte-starter - 프로젝트 소스
	/front-end-svelte - Front-end 소스개발 폴더
	/src
		/main
			/java - Back-end 영역 소스
			/resources - Back-end 영역 환경설정
			/webapp - Front-end Sveltekit 터미널에서 npm run build 결과 소스
	.gitignore - git에 업로드되지 않을 예외 항목을 적어놓은 파일
	pom.xml - maven 프로젝트 설정 파일
	README.md - 이 파일!!
```

##### FRONT-END 구성

```
/springboot-svelte-starter - 프로젝트 소스
	/front-end-svelte - Front-end sveltekit vite 영역 빌드 전 소스
		/src
			/comp - 화면별 .svelte 컴포넌트 모음
			/layout - AuthCheck > Base > Content > Frame 순으로 덮여져 있음
			/lib - $lib 위치(import app from '$lib/app';)
			/routes
				+layout.svelte - /app.scss 전체화면 적용
				+page.svelte - svelte 첫 진입점
			app.html - 개발용 최초 진입점 > ./routes/layout.svelte > ./routes/+page.svelte 순으로
			app.scss - CSS 전체화면 적용 소스
		/static
		/webapp - 빌드된 소스파일 모음
			index.html - 실제 운영시 접근하는 최초 파일
		svelte.config.js - 빌드경로, 진입점파일 등 svelte/kit 설정
		vite.config.js - proxy port 설정 및 빌드파일 chunkSize 설정
```

## 2-2. 개발환경

##### 개발/배포 방법 요약

- front-end 개발은 /springboot-svelte-starter/front-end-svelte/경로에서 VSCODE를 작업하고
- back-end 개발은 /springboot-svelte-starter 경로에서 Spring Tool Suite(STS) 또는 이클립스로 작업한다.(import Existing Maven Project)
- 개발테스트: back-end는 STS에서 Server구동 + front-end는 VSCODE bash 터미널에서 npm run dev를 실행하여 localhost:8081 접속하여 테스트
- 운영테스트: back-end는 STS에서 Server구동 + front-end는 VSCODE bash 터미널에서 npm run build를 실행하여 localhost:8080 접속하여 테스트
- port 번호: 개발/운영환경에 맞게 바꾸어주면 된다.
  1. 이클립스: Servers > 프로젝트 정보창 Overview > Ports 정보 수정
     - front-end: 8081
     - back-end: 8080
  2. Svelte: /front-end-svelte 정보 수정
     .vite.config.js
     /src/lib/app.js
- 간혹, 개발/운영테스트가 동일하게 작동하지 않을 때가 있다. 외부모듈<TUI-Datepicker> 사용할 경우였는데, HTMLDomEelemnt id, role 등을 설정하여 수정되었음
  npm run dev 실행시 warning 메시지를 잡아주어야 빌드가 깔끔해진다. ARIA role 등을 잘 찾아서 설정해 주도록 해야한다.
  (MDN 참고사이트 : https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Roles/button_role)
- 배포반영은 VSCODE bash 터미널에서 npm run build까지 실행 후 git commit, merge, push 등 완료
  (npm run build하면 /svelte-springboot-starter/src/main/webapp 안의 파일이 모두 새로운 파일로 변경되기 때문에 Git Commit전에는 꼭 잊지말 것)

##### /svelte-springboot-starter/front-end-svelte/svelte.config.js에서 아래 내용처럼 변경

```javascript
import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/kit/vite';

/\*_ @type {import('@sveltejs/kit').Config} _/
const config = {
	preprocess: vitePreprocess(),
	kit: {
	adapter: adapter({
		pages: 'webapp' //빌드 경로: 편의상 webapp으로 한다.
		, assets: 'webapp' //빌드 경로
		, fallback: 'index.html' //진입점 파일
		, precompress: false
		, strict: true
	}),
	prerender: { entries: [] }
	}
};

export default config;
```

##### /front-end-svelte/package.json 변경

```javascript
"scripts": {
	"dev": "vite dev", --> 터미널에서 npm run dev 로 실행: 가상환경에서 웹서비스를 띄워주기. 이후 소스코드의 실시간 변경이 탐지되고 브라우저에 반영된다.
	"build": "vite build && rm -fr ../src/main/webapp && cp -R ./webapp ../src/main/" --> npm run build 로 실행: 빌드 후 폴더를 SpringBoot 소스로 이동 시킨다.
	"preview": "vite preview",
}
```

##### /front-end-svelte/vite.config.js 변경

```javascript
import { sveltekit } from "@sveltejs/kit/vite";
import { defineConfig } from "vite";

const port_backend = 8080; //★★★Front-end 서비스 포트(npm run dev에서만 사용됨)
const port_frontend_dev = 8081; //★★★Back-end WAS 포트 (npm run dev에서 back-end와 통신할 때 사용
// /src/lib/app.js의 PATH와 함께 설정해야 합니다.)
const port_preview = 8082; //npm run preview 에서 사용(SpringBoot에서 볼거기 때문에 거의 사용하지 않는다)

export default defineConfig({
  plugins: [sveltekit()],
  server: {
    port: port_frontend_dev,
    strictPort: false, //true: 다른 서비스가 포트를 사용중일 때 exit, false: 임의의 포트로 자동 지정
    proxy: {
      //npm run dev 로 실행항 결우 Back-end 서비스 접속을 위해 사용됨.("/back-end" 주소로 오면 모두 8080 포트로 우회한다.)
      //8080 포트는 SpringBoot Server의 http 포트이다. 다를 경우 수정 필요!
      //rewrite는 요청주소를 "/back-end/home" 에서 "/home" 으로 변경해주는 역할을 한다.
      //npm run build 후 SpringBoot Server 구동시 본 설정은 아무 영향을 주지 않는다.
      "/back-end": {
        target: `http://localhost:${port_backend}/`,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/back-end/, ""),
      },
    },
  },
  preview: {
    port: port_preview,
  },
  //빌드
  build: {
    chunkSizeWarningLimit: 1600, //빌드 Warning 발생 해결
  },
});
```

#### /front-end-svelte/src/lib/app.js

```javascript
const App = function () {
  this.name = "svelteSpringbootStarter"; //★★★쿠키, 세션스토리지 명으로 사용됨
  this.port_backend = 8080; //★★★Front-end 서비스 포트(npm run dev에서만 사용됨)
  this.port_frontend_dev = 8081; //★★★Back-end WAS 포트 (npm run dev에서 back-end와 통신할 때 사용
  //vite.config.js server.proxy 참조
  //npm run dev에서 back-end와 통신하기 위해 /back-end를 강제로 붙여준다.
  //console.log(location, location.port=="28082");
  if (location.port == this.port_frontend_dev.toString()) {
    //npm run dev 사용
    this.PATH = "/back-end";
    this.WEBSOCKET = `ws://${location.hostname}:${this.port_backend}/socket`;
  } else {
    //npm run build 사용
    this.PATH = "";
    this.WEBSOCKET = `ws://${location.host}/socket`;
  }
};
```

#### 이제 터미널에서 npm run dev 를 실행하면 브라우저에서 확인이 가능할 겁니다.

```
http://localhost:8081
```

#### npm run build 를 실행하면 SpringBoot /svlete-springboot-starter/src/main/webapp이 새로 만들어 집니다.

- /front-end-svelte/package.json 참고
- 다른 프로그램이 폴더를 점유하느라 자주 실패할 수도 있으니, 재시도 몇 번 해보세요.

## 3. Gitlab 연동

- 가정1: gitlab 에 신규프로젝트를 생성하였다.
- 가정2: 로컬에는 거의 완성된 프로젝트가 이미 만들어져 있고 gitlab 프로젝트에 덮어쓰기 하고 싶다.
- 가정3: 원본 소스는 로컬의 main과 works/dev 브랜치에 있다.
- 가정4: 원격(GitLab)에 이미 프로젝트가 생성되어 있다고 본다.

###

**신규 프로젝트 최초 업로드**

github에 http://github.com/aaboo/svelte-springboot-starter.git 저장소를 만든 후 진행한다.

```bash
cd /d/github/svelte-springboot-starter #로컬 프로젝트 폴더로 이동
rm -fr .git #혹시 있을 기존 git 정보를 삭제한다.
git init #git을 초기화 한다.
git checkout master #초기화시 생성된 master 브랜치로 checkout(이동)한다.
git branch -m main #master 브랜치명을 main으로 변경한다.
git remote add origin http://github.com/aaboo/svelte-springboot-starter.git #원격저장소와 연결한다.
git push --force --set-upstream origin main #원격저장소에 소스를 업로드한다. (--set-upstream 옵션은 최초 1회만 사용한다.)

#github과 계정연동이 되어 있지 않았다면
git config list #git 설정 정보를 확인한다.

#git 연결계정을 설정한다.
git config user.name [github 계정이름]
git config user.email [github 계정이메일]

#계정연결을 openssl로 하겠다는 설정을 한다.
git config --global http.sslBackend openssl

#이후 최초 clone, push 등 연결할 경우 user.name과 password를 물어본다.
#username은 [github 계정이름] 을 입력하고
#password는 github 계정의 로그인 패스워드를 사용하는 것이아니라 별도의 openssl key를 생성한 것을 사용해야한다.
#openssl key 생성방법:
	#github 접속 > 우측상단 개인 아이콘 > Settings > Developer settings > Personal access tokens > Tokens (classic) > Generate new token > Generate new token (classic) > Note 에 계정용도에 대헌 설명을 단순히 작성. > Expiration > No expiration > Select scopes > reop 체크 > Generate token 클릭 > 이전화면으로 돌아감 > 새로 생성된 키를 복사하여 사용한다.
	#주의 사항: 생성된 키는 이후 다시 확인할 수 없으므로, 복사하여 보관하는 것을 추천한다.

```

**원격 상태를 유지하면서 추가하는 방법**

- [ ] Step1 : 프로젝트 소스 백업
- [ ] Step2 : 개발자 로컬에서 원격저장소 연결하기
- [ ] Step3 : 백업파일 복원
- [ ] Step4 : Commit & Push

##### Step1 : 프로젝트 소스 백업

    작업폴더로 가서 프로젝트 폴더 통째로 복사본을 만들어준다.
    D:\github\svelte-springboot-starter
    D:\github\svelte-springboot-starter - 복사본

##### Step2 : 개발자 로컬에서 원격저장소 연결하기 (Git Bash로 개발자 로컬에서 진행)

    VSCode나 SourceTree에서 bash 터미널을 열어서 진행
    D:\github\svelte-springboot-starter (works/dev) bash창에서 작업 시작

##### Step2-1 : 원격브랜치 연결을 처음 시도할 경우

git remote -v로 연결된 프로젝트가 없는 경우 "git remote add" 명령으로 연결해 준다.
이미 연결되어 있을 경우는 Step2-2로 진행

```
[work/dev] git checkout main
		-- 현재 브랜치를 main 으로 변경한다.

[main] git remote add origin http://github.com/aaboo/svelte-springboot-starter.git
		-- 소스트리에서 Remotes가 활성화되어 origin 이 활성화 된다.(svelte-springboot-starter.git 으로 새로 연결됨)

[main] git fetch
		-- 최초 패치를 받는다 : 소스트리에서 Remotes의 origin/main 이 활성화 된다.

```

##### Step2-2 : 원격브랜치에 다른 서비스가 이미 연결되어 있을 경우

STARTER 프로젝트를 복사하여 다른 프로젝트를 생성할 경우 기존의 프로젝트로 연결되어 있을 수 있다.
git remote -v로 아래처럼 **다른 프로젝트**에 연결되어 있을 경우 "git remote set-url" 명령으로 변경해 준다.

    git remote -v
    origin  http://github.com/aaboo/svelte-springboot-starter.git (fetch)
    origin  http://github.com/aaboo/svelte-springboot-starter.git (push)

    git remote set-url origin http://github.com/aaboo/svelte-springboot-starter.git

```
[works/dev] git checkout main
		-- 현재 브랜치를 main 으로 변경한다.

[main] git remote set-url origin http://github.com/aaboo/svelte-springboot-starter.git
		-- 소스트리에서 Remotes가 활성화되어 origin 이 활성화 된다.(aaboo.git 에서 svelte-springboot-starter.git 으로 연결됨)

[works/dev] git fetch
		-- 최초 패치를 받는다 : 소스트리에서 Remotes의 origin/main 이 활성화 된다.

```

##### Step3: 백업파일 복원(main 브랜치에서 진행)

백업한 폴더에서 아래 폴더와 파일을 복사해 프로젝트 폴더로 붙여넣기 해준다. (main 브랜치 상태에서 실행)

```
/src
/pom.xml
/README.md
/.gitignore

```

##### Step4: Commit & Push

```

[main] git add .

[main] git commit -m "복사 완료"

[main] git push origin main

```

##### /svelte-springboot-starter/.gitIgnore

(현재 아래와 같이 되어 있으니, 적절하게 수정해 줍니다.)

```
.mvn/
.classpath
.factorypath
.svn/
Gulpfile.js
mvnw
mvnw.cmd
package.json
.project
.settings/
target/

front-end-svelte/.eslintignore
front-end-svelte/.eslintrc.cjs
front-end-svelte/.npmrc
front-end-svelte/.prettierignore
front-end-svelte/.prettierrc
front-end-svelte/.gitignore
front-end-svelte/webapp/**

.cfInfo
.cfPrjInfo
.gitIgnore
```

### 수고하셨습니다.
