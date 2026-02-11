<script>
const ID = "filetran_search";

import { onMount, createEventDispatcher } from 'svelte';
import DatePicker from 'tui-date-picker';
import 'tui-date-picker/dist/tui-date-picker.css';
import SelectBox from '@toast-ui/select-box';
import '@toast-ui/select-box/dist/toastui-select-box.min.css';
import app from '$lib/app';
import icon from '$lib/icon';

const dispatch = createEventDispatcher();

onMount(()=>{
    //console.log(uuid); 
    NV.init();
    //window.NV = NV;
})

const NV = {
    dom:{
        workdate: null
        , svrname: null
        , worktype: null
    }
    , data:{
        svrnameList:[]
    }
    , init: async function(){
        await app.fetch("/filetran/init").then((response)=>{            
            if(response.code=="OK"){
                NV.data.svrnameList = [...response.data.svrnameList];
                NV.bindEvent();
                NV.action.dategubun(NV.dom.workdate.value.replace(/[^\d]/g,''));
                NV.action.search();
            }else{
                alert(response.message??"파일송수신 화면 초기화에 실패하였습니다.");
            }
        });
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
        //기관
        NV.svrname = new SelectBox(NV.dom.svrname, {
            placeholder:"기관"
            , data:[{ 
                label:"기관"
                , data: [{label:"전체", value:"", selected:true}, ...NV.data?.svrnameList]??[] 
            }]
            , theme:{
                "dropdown.maxHeight": "300px"
                , "dropdown.width": "200px"
            }
        });
        NV.dom.svrname.addEventListener("click", function(ev){
            ev.preventDefault();
            document.body.click();
            NV.svrname.open();
        });
        //송수신
        NV.worktype = new SelectBox(NV.dom.worktype, {
            placeholder:"송수신"
            , data:[{ 
                data: [
                    {label:"전체", value:"", selected:true}
                    , {label:"수신", value:"get"}
                    , {label:"송신", value:"put"}
                ] 
            }]
            , theme:{
                "dropdown.width": "100px"
            }
        });
        NV.dom.worktype.addEventListener("click", function(ev){
            ev.preventDefault();
            document.body.click();
            NV.worktype.open();
        });
    }
    , action: {
        //파라미터
        getParam: ()=>{
            return {
                workdate: NV.dom.workdate.value.replace(/[^\d]/g,'')
                , svrname: NV.svrname?.getSelectedItem()?.value??""
                , worktype: NV.worktype?.getSelectedItem()?.value??""
            }
        }
        //조회
        , search: ()=>{
            const param = NV.action.getParam();
            //console.log(param);
            if(param.workdate==""){alert('작업월을 입력해 주세요'); return;}
            app.ss.setSearch(param);
            dispatch('search', { filetran: {...param} }); //조회 발생
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
    
<div id="jobCtrl_search" class="search">
    <span class="title">{@html icon.search()}&nbsp;메뉴6 조회</span>
    <div class="label">
        <span class="labelTitle">작업일(<span class="dategubun" bind:this={NV.dom.dategubun}></span>)</span>
        <div id="" class="tui-datepicker-input tui-datetime-input tui-has-focus" style="width:130px">
            <input type="text" bind:this={NV.dom.workdate} arial-label="Date-Time"/>
            <span class="tui-ico-date"></span>
        </div>
        <div bind:this={NV.dom.workdatePopup} class="tui-date-picker-container rightMiddle" style="margin-top:-1px;"></div>
    </div>
    <div class="label">
        <span class="labelTitle">기관</span>
        <div id="svrname" bind:this={NV.dom.svrname} style="width:130px"></div>
    </div>     
    <div class="label">
        <span class="labelTitle">송수신</span>
        <div id="worktype" bind:this={NV.dom.worktype} style="width:130px"></div>
    </div> <!---->
    <div class="buttons">
        <button id="btnSearch" on:click={NV.action.search}>{@html icon.search()} 조회</button>
    </div>
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
        div.label{            
            margin-bottom:0.6rem;
            span.labelTitle{
                display:block;
                font-size:0.75rem;
                .dategubun{
                    //font-size:0.8rem;
                    font-weight:500;
                    color:royalblue;
                }
            }
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