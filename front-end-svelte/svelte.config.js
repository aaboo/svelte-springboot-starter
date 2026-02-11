import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/kit/vite';
import path from "path";

/** @type {import('@sveltejs/kit').Config} */
const config = {
	preprocess: vitePreprocess(),
	kit: {
		adapter: adapter({
			pages: 'webapp' //빌드 경로
			, assets: 'webapp' //빌드 경로
			, fallback: 'index.html' //진입점 파일
			, precompress: false
			, strict: true
		}),
		prerender: { entries: [] }
	}
};

export default config;