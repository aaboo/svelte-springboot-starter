<script>
import { onMount } from 'svelte';

import icon from '$lib/icon';
import app from '$lib/app';

const APP_ISSAVEID = app.name+"_isSaveID";
const APP_SAVEDID = app.name+"_savedID";
const NV = {
    dom:{}
}

let param = {
    userid:(app.getCookie(APP_ISSAVEID)=="true"?app.getCookie(APP_SAVEDID):"")
    , password:""
    , isSaveID: (app.getCookie(APP_ISSAVEID)=="true")
}
async function login(){
    //console.log(param);
    await app.fetch("/auth/login", Object.assign({}, param))
    .then((res)=>{
        //console.log(res);
        if(res){
            if(res.code=="OK"){
                location.href="/";
            }else{
                alert(res.message);
                if(res.redirect) location.href = res.redirect;
            }
        }
    })
    .catch((e)=>{
        console.log(e);
    });

}

//세션스토리지 초기 작성
function resetSessionStorage(){    
    sessionStorage.setItem(app.name, JSON.stringify(app.ss.structure));
}

function checkVanSaveID(ev){
    ev.preventDefault();
    app.setCookie(APP_ISSAVEID, param.isSaveID, 30);
}

onMount(()=>{        
    resetSessionStorage();
    app.logout();
    //console.log(NV.dom);
    setTimeout(()=>{
        if(NV.dom.userid.value=="") NV.dom.userid.focus();
        else NV.dom.password.focus();        
    }, 500);
});
</script>
    
<content>
    <div id="wrapper">
        <div id="wrapperInner">
            <div id="serviceDescription">
                <span id="title">sss</span>
                <span id="detail">Svelte & Springboot Starter in one container</span>
            </div>
            <form>
                <label>
                    <input type="checkbox" bind:checked={param.isSaveID} on:change={checkVanSaveID}>
                    <span>아이디 저장</span>
                </label>
                <input type="text" bind:this={NV.dom.userid} bind:value={param.userid} maxlength="10" placeholder="아이디">
                <input type="password" bind:this={NV.dom.password} bind:value={param.password} placeholder="패스워드">
                <button on:click={login}>{@html icon.login(2)}&nbsp;로그인</button>
            </form>
            <div id="media"></div>
        </div>
    </div>
    <div id="myCompanyLogo"></div>
</content>
    
<style lang="scss">
    @property --angle{
        syntax: "<angle>";
        initial-value: 0deg;
        inherits: false;
    }
    %flex-center{
        display:flex;
        justify-content:center;
        align-items:center;
    }
    %flex-center-v{
        display:flex;
        justify-content:center;
        align-items:center;
        flex-direction:column;//세로방향
    }
    content {
        z-index:10;
        position:absolute;
        top:0;
        display:block;
        width:100%;
        height:100%;

        //background:linear-gradient(321deg, #d542c2, #b8d542, #234f8c);
        background:linear-gradient(0deg, #d542c2, #42c1d5, #1b436a);
        background-size:600% 135%;
        // -webkit-animation: AnimationName 59s ease infinite;
        // -moz-animation: AnimationName 59s ease infinite;
        // animation: AnimationName 59s ease infinite;

        div#wrapper {
            //background-color:rgba(255,0,255,0.1);
            background:linear-gradient(0deg, #d542c2, #42c1d5, #1b436a);
            background-size:100% 180%;
            background-position-y: 30%;

            border-radius:1em;
            position:absolute;
            left: calc(50vw - 159px);
            top: calc(50vh - 240px);
            box-shadow: rgba(0,0,157,0.24) 2px 2px 17px;
            div#wrapperInner {
                position:relative;
                @extend %flex-center-v;
                border-radius:1em;
                width:300px;
                margin:1px;
                padding:2em 1em;
                //box-shadow:inset rgba(255,255,255,0.8) 2px 2px 7px;
                box-shadow: rgba(255,255,255,0.4) 0px 0px 7px;
                //box-shadow: rgba(0,0,255,0.2) 0px 0px 7px;
                
                //라인 에니메이션
                &::after, &::before{
                    content:"";
                    position: absolute;
                    height:100%;
                    width:100%;
                    //background-image:conic-gradient(from var(--angle), red, blue, white, green, yellow, red);
                    background-image:conic-gradient(from var(--angle), transparent 80%, white, transparent);
                    top:50%;
                    left:50%;
                    translate: -50% -50%;
                    z-index:-1;
                    padding:0.5px;
                    //border:solid 3px linear-gradient(0deg, #d542c2, #42c1d5, #1b436a);                    
                    border-radius: 1em;
                    animation: 12s spin linear infinite;
                }
                &::before{
                    filter:blur(1rem);
                    opacity: 0.5;
                }
                @keyframes spin{
                    from{
                        --angle:0deg;
                    }
                    to{
                        --angle:360deg;
                    }
                }

                div#serviceDescription{
                    @extend %flex-center-v;
                    color:#fff;
                    span#title{
                        font-size:8rem;
                        font-weight:100;
                        letter-spacing:-0.7rem;
                        line-height:80%;
                        text-shadow:rgba(0,0,0,0.5) 0px 0px 5px;
                    }
                    span#detail{
                        margin-top:2rem;
                        font-size:0.8rem;
                    }
                }
                form {
                    @extend %flex-center-v;
                    margin:2rem 0 1.5rem 0;
                    *{ margin-top:0.5rem; }
                    label {
                        width:100%;
                        line-height:18px;
                        padding-right:1rem;
                        text-align:right;
                        font-size:1rem;
                        color:#fff;
                        text-shadow: rgba(0,0,0,1) 0 0 3px;
                        span {
                            position:relative;
                            top:-1px;
                            font-size:0.9rem;
                        }
                    }
                    input[type=text], input[type=password] {
                        border-radius:2rem;
                        width:240px;
                        height:2rem;
                        padding-left:1rem;
                        font-size:1rem;
                        font-weight:500;
                        border:0;
                        color:#000;
                        background-color:rgba(255,255,255,0.8);
                        box-shadow:inset rgba(0,0,0,0.2) 0 0 7px;
                    }
                    button{
                        @extend %flex-center;
                        margin-top:1rem;
                        height:2.7em;
                        width:260px;
                        border-radius:2rem;
                        box-shadow:inset rgba(0,0,0,0.2) 0 0 5px;
                        
                        background:linear-gradient(321deg, #234f8c, #b8d542, #d542c2);
                        background-size:600% 600%;
                        -webkit-animation: AnimationName 20s ease infinite;
                        -moz-animation: AnimationName 20s ease infinite;
                        animation: AnimationName 20s ease infinite;
                        &:hover {
                            background: var(--link-hover);
                        }

                        &:active {
                            background: var(--link-active);
                        }
                    }

                    :is(button, button:hover, button:active):disabled {
                        background: var(--link);
                        filter: grayscale(1);
                        opacity: 0.4;
                    }
                }
            }
        }

        div#myCompanyLogo{
            position:fixed;
            top:15px;
            right:15px;
            width:44px;
            height:44px;
            background:url("/favicon.png") no-repeat;
            background-size:contain;
        }
    }

    @-webkit-keyframes AnimationName{
        0%{background-position:0% 26%}
        50%{background-position:100% 75%}
        100%{background-position:0% 26%}
    }
    @-moz-keyframes AnimationName{
        0%{background-position:0% 26%}
        50%{background-position:100% 75%}
        100%{background-position:0% 26%}
    }
    @keyframes AnimationName{
        0%{background-position:0% 26%}
        50%{background-position:100% 75%}
        100%{background-position:0% 26%}
    }
</style>