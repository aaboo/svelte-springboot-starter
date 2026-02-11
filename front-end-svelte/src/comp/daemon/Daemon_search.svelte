<script>
const ID = "daemon_search";

import { onMount, createEventDispatcher } from 'svelte';
import DatePicker from 'tui-date-picker';
import 'tui-date-picker/dist/tui-date-picker.css';
import app from '$lib/app';
import icon from '$lib/icon';

const dispatch = createEventDispatcher();
//const uuid = crypto.randomUUID().split("-")[0];

onMount(()=>{
    //console.log(uuid);
    NV.init();
})

const NV = {
    dom:{}
    , data:{
        historyCnt: 20
    }
    , init: function(){
        NV.action.search();           
    }
    , action: {
        //조회
        search: ()=>{
            const param = {
                historyCnt: NV.data.historyCnt.toString()
            }
            app.ss.setSearch(param);
            dispatch('search', { daemon: {...param} }); //조회 발생
        }
    }
}
</script>

<div id="jobCtrl_search" class="search">
    <span class="title">{@html icon.search()}&nbsp;메뉴4 조회</span>
    <label>
        <span class="label">히스토리 조회수({NV.data.historyCnt})</span>
        <input type="range" min="1" max="40" bind:value={NV.data.historyCnt}>
    </label>
    <div class="buttons">
        <button id="btnSearch" on:click={NV.action.search}>{@html icon.search()} 조회</button>
        <!-- <button id="btnPlay" on:click={NV.action.search}>{@html icon.search()} 시작</button>
        <button id="btnStop" on:click={NV.action.search}>{@html icon.search()} 정지</button> -->
    </div>
    <!-- <select bind:this={NV.dom.slt}></select> -->
</div>
    
<style lang="scss">
    div#jobCtrl_search{
        display:flex;
        justify-content:center;
        align-items:center;
        flex-direction:column;
        padding:1rem;
        span.title{
            display:flex;
            font-size:0.9rem;
            margin-bottom:1em;
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;        
        }
        .label{
            display:block;
            text-align:center;
            font-size:0.75rem;
        }
        .buttons{
            margin-top:1rem;
            button {
                white-space:nowrap;
                //font-size:0.8rem;
            }
        }
    }
</style>