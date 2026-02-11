<script>
import { onMount } from 'svelte';

import app from '$lib/app';
import ses from '$lib/ses';
import icon from '$lib/icon';
import { userStore, loginTimerStore, menuStore } from '$lib/stores';
import LoginRetry from './LoginRetry.svelte';


async function logout(){
    userStore.reset();
    const loginResponse = app.logout();
    location.href="/login";
    //console.log(loginResponse);
}

async function resetTimer(){
    app.fetch("/auth/reset").then(()=>{
        ses.resetTimer();
        app.modal.close();
    });
}

let today;
let todayTimer;
function setToday(){
    const arrDate = ["일","월","화","수","목","금","토"];
    function chkToday(){
        const todayOrg = new Date();//new Date(2024, 0, 21, 0, 0, 0)
        const todayYmd = todayOrg.print("yyyy.mm.dd");
        const hhmiss = todayOrg.print("hh:mi:ss");
        const todayDate = arrDate[todayOrg.getDay()];
        today = `${todayYmd}(${todayDate}) ${hhmiss}`;
        todayTimer = setTimeout(chkToday, 1000);
    }
    chkToday();
}


onMount(()=>{
    resetTimer();//로그인 타이머
    setToday();//오늘 타이머
    return (()=>{
        clearInterval(ses.timer);
        clearInterval(todayTimer);
    });
});
</script>



<logininfo>
    <div id="logo">
        <img id="logoImg" src="/img/favicon.png" alt="aaboo">
        <span id="logoName">SSS</span>
        <span id="currentMenuName">: {$menuStore.currentMenu.name}</span>
    </div>
    <div id="today" title="오늘">{today}</div>
    <div id="logininfo">
        {$userStore.userid}
        &nbsp;
        <button class="tiny" on:click={resetTimer} title="남은시간">
            {@html icon.clock(0.9)}&nbsp;{$loginTimerStore}
        </button>
        &nbsp;
        <button class="tiny" on:click={logout} title="로그아웃">
            {@html icon.logout(1)}&nbsp;로그아웃
        </button>
    </div>
    {#if $loginTimerStore=="0"}
        <LoginRetry on:close={()=>{ses.resetTimer();}}/>
    {/if}
</logininfo>



<style lang="scss">
logininfo {
    display:flex;
    justify-content:space-between;
    align-items:center;
    background-color:#0658a7;
    overflow:hidden;
    height:47px;
    div#logo{
        display:flex;
        justify-content:center;
        align-items:center;
        padding:0 1rem;
        font-size:1.8em;
        color:var(--bg-1);
        img#logoImg{
            //border:solid 1px green;
            width:1.5rem;
        }
        #logoName{ margin:0 0.2rem 0 0.5rem; }
        #currentMenuName{
            position:relative;
            top:0.3rem;
            font-size:0.9rem;
        }
    }
    div#today{
        font-size:1rem;
        color:rgb(255 255 255/50%);
    }
    div#logininfo{
        display:flex;
        justify-content:center;
        align-items:center;
        padding:0 1rem;
        color:white;
        text-shadow:rgba(0,0,0,0.5) 0px 1px 2px;
        font-size:0.8rem;
        button {
            display:flex;
            justify-content:center;
            align-items:center;
            height:25px;
            padding:0.3rem 0.5rem;
            box-shadow: rgba(0 0 0/20%) 0px 0px 2px;            
        }
    }
}
</style>