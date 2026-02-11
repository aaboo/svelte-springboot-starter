<script>
const ID = "jobProc_search";

import { onMount, createEventDispatcher } from 'svelte';
import DatePicker from 'tui-date-picker';
import 'tui-date-picker/dist/tui-date-picker.css';
import app from '$lib/app';
import icon from '$lib/icon';

const dispatch = createEventDispatcher();

onMount(()=>{
    //console.log(uuid);
    NV.init();
});

const NV = {
    dom:{}
    , init: function(){
        NV.bindEvent();            
        NV.action.dategubun(NV.dom.workdate.value.replace(/[^\d]/g,''));
        NV.action.search();
    }
    , bindEvent: ()=>{
        //세션스토리지 정보를 가져와 셋팅해 준다.           
        const workdate = app.ss.getSearch()?.workdate;
        //날짜    
        NV.workdate = new DatePicker(NV.dom.workdatePopup, {
            date: !workdate?new Date():new Date(workdate.substr(0,4), workdate.substr(4,2)-1, workdate.substr(6,2))
            , type: 'date'
            , language:"ko"
            , input:{
                element: NV.dom.workdate
                , format: 'yyyy-MM-dd(D)'
            }
        });
        NV.workdate.on("change", ()=>{
            //console.log(NV.dom.workdate.value.replace(/[^\d]/g,''));
            const workdate = NV.dom.workdate.value.replace(/[^\d]/g,'');
            NV.action.dategubun(workdate)
        });
    }
    , action: {
        //조회
        search: ()=>{
            const param = {
                workdate: NV.dom.workdate.value.replace(/[^\d]/g,'')
            }
            if(param.workdate==""){alert('작업일을 입력해 주세요'); return;}
            app.ss.setSearch(param);
            dispatch('search', { jobProc: {...param} }); //조회 발생
        }
        //작업일구분
        , dategubun: (workdate)=>{
            app.fetch("/common/selectDategubun", {workdate: workdate})
            .then((response)=>{
                NV.dom.dategubun.innerHTML = response.data;
            })
            .catch((e)=>{
                console.log(e);
            });
        }
    }
}
</script>

<div id="jobProc_search" class="search">
    <span class="title">{@html icon.search()}&nbsp;메뉴2 조회</span>
    <label>
        <span class="label">작업일(<span class="dategubun" bind:this={NV.dom.dategubun}></span>)</span>
        <div class="tui-datepicker-input tui-datetime-input tui-has-focus" style="width:130px">
            <input type="text" bind:this={NV.dom.workdate} arial-label="Date-Time"/>
            <span class="tui-ico-date"></span>
        </div>
        <div bind:this={NV.dom.workdatePopup} class="tui-date-picker-container rightMiddle" style="margin-top:-1px;"></div>
    </label>
    <div class="buttons">
        <button id="btnSearch" on:click={NV.action.search}>{@html icon.search()} 조회</button>
        <!-- <button id="btnPlay" on:click={NV.action.search}>{@html icon.search()} 시작</button>
        <button id="btnStop" on:click={NV.action.search}>{@html icon.search()} 정지</button> -->
    </div>
</div>
    
<style lang="scss">
div#jobProc_search{
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
        font-size:0.75rem;
        .dategubun{
            //font-size:0.8rem;
            font-weight:500;
            color:royalblue;
        }
    }
    .buttons{
        margin-top:1rem;
        button {
            white-space:nowrap;
        }
    }
}
</style>