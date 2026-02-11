<script>
/**********
 * HeadTop.svelte 에서 불려짐
 **********/
import { onMount, createEventDispatcher } from 'svelte';
import app from '$lib/app';
import { userStore } from '$lib/stores';
import Popup from './Popup.svelte';

const dispatch = createEventDispatcher();

const NV = {
    init:()=>{}
    , data:{
        maxRetryCount:5 //로그인 재시도 제한횟수
        , retryCount:0
        , retryMessage: ""
    }
    , dom:{
        password:null
    }
    , action: {
        loginRetry:()=>{
            const param = {
                userid: $userStore.userid
                , password:NV.dom.password.value
            }
            app.fetch("/auth/login", param).then((response)=>{
                if(response.code=="OK"){
                    dispatch("close");
                }else {
                    NV.data.retryMessage = response.message;
                    NV.data.retryCount = NV.data.retryCount + 1;
                    if(NV.data.retryCount==NV.data.maxRetryCount){
                        location.href="/login";
                    }
                }
            });
        }
        , logoff:()=>{
            app.logoff();
            NV.dom.password.focus();
        }
    }
}

onMount(()=>{
    NV.action.logoff();
});
</script>

<Popup
    title="로그인"
    width=300
    height=500
    showCloseButton=false
    zIndex=9999999999
>
    <div slot="body">
        <div>시간이 경과하였습니다. 다시 로그인해 주세요</div>
        <input type="password" placeholder="패스워드"
            bind:this={NV.dom.password}
            on:keyup={(ev)=>{
                if(ev.keyCode=="13") NV.action.loginRetry();
            }}
        >
        {#if NV.data.retryCount>0}            
            <div>
                {NV.data.retryMessage}
                <div>남은 로그인 횟수: {NV.data.maxRetryCount - NV.data.retryCount} 회</div>
            </div>
        {/if}
    </div>
    <div slot="footer">
        <button on:click={NV.action.loginRetry}>로그인</button>
    </div>
</Popup>

<style lang="scss">
div[slot=body]{
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    padding:1rem;
    div{
        margin:0.5rem;
        font-size:0.7rem;
        color:gray;
        text-align:center;
    }
    input[type=password]{
        text-align:center;
        height:1.6rem;
        width:80%;
        border-radius:0.5rem;
        background-color:#fafafa;
        box-shadow:inset rgb(0 0 0 / 10%) 1px 1px 5px;
    }
}
</style>