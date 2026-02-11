import { writable, readable } from 'svelte/store';

//사용자정보
function userStoreFunc() {
    const { subscribe, set, update } = writable(null);
    return {
        subscribe,
        update: (data)=> update((n)=> data ),
        reset: ()=> set(null)
    }
}

//메뉴정보
function menuStoreFunc() {
    const { subscribe, set, update } = writable(null);
    return {
        subscribe,
        update: (data)=> update((n)=> data ),
        reset: ()=> set(null)
    }
}

//조회정보
function searchStoreFunc() {
    const { subscribe, set, update } = writable(null);
    return {
        subscribe,
        update: (data)=> update((n)=> data ),
        reset: ()=> set(null)
    }
}

//메뉴 넓이 조정 gap 위치
function gapLeftStoreFunc(){
    const { subscribe, set, update } = writable(180);
    return {
        subscribe,
        update: (data)=> update((n)=> data ),
        reset: ()=> set(null)
    }
}

//로그인 타이머
function loginTimerStoreFunc(){
    const basicTime = "1:00:00";
    const { subscribe, set, update } = writable(basicTime);
    return {
        basicTime: basicTime,
        subscribe,
        update: (data)=> update((n)=> data ),
        reset: ()=> set(basicTime)
    }
}

//화면 메뉴 넓이 조정시 메뉴명 사라지는 넓이 기준
function menuNameHideWidthFunc(){
    const { subscribe } = readable(125);
    return {
        subscribe
    }
}

//writable
export const userStore = userStoreFunc();
export const menuStore = menuStoreFunc();
export const searchStore = searchStoreFunc();
export const gapLeftStore = gapLeftStoreFunc();
export const loginTimerStore = loginTimerStoreFunc();

//readable
export const menuNameHideWidthStore = menuNameHideWidthFunc();