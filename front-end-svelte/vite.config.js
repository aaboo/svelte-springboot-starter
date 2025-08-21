import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

const port_backend = 8080; //★★★Front-end 서비스 포트(npm run dev에서만 사용됨)
const port_frontend_dev = 8081; //★★★Back-end WAS 포트 (npm run dev에서 back-end와 통신할 때 사용
								// /src/lib/app.js의 PATH와 함께 설정해야 합니다.)
const port_preview = 8082; //npm run preview 에서 사용(SpringBoot에서 볼거기 때문에 거의 사용하지 않는다)

export default defineConfig({
	plugins: [sveltekit()]
	, server: {
		port: port_frontend_dev 
		, strictPort: false //true: 다른 서비스가 포트를 사용중일 때 exit, false: 임의의 포트로 자동 지정
		, proxy:{
			//npm run dev 로 실행항 결우 Back-end 서비스 접속을 위해 사용됨.("/back-end" 주소로 오면 모두 8080 포트로 우회한다.)
			//8080 포트는 SpringBoot Server의 http 포트이다. 다를 경우 수정 필요!
			//rewrite는 요청주소를 "/back-end/home" 에서 "/home" 으로 변경해주는 역할을 한다.
			//npm run build 후 SpringBoot Server 구동시 본 설정은 아무 영향을 주지 않는다.
			'/back-end': {
				target: `http://localhost:${port_backend}/` 
				, changeOrigin: true
				, rewrite: (path) => path.replace(/^\/back-end/,'')
			}
		}
	}
	, preview: {
		port: port_preview
	}
	//빌드
	, build: {
		chunkSizeWarningLimit: 1600 //빌드 Warning 발생 해결
	}
});	
