<script>
const ID = "batchResult";

import { onMount } from 'svelte';
import { gapLeftStore, menuStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';
import icon from '$lib/icon';
import PopupJobDetail from '../jobProc/PopupJobDetail.svelte';
import PopupProcDetail from '../jobProc/PopupProcDetail.svelte';

//조회 - Frame.svelte.searchFromChildSearch(ev)
export function search(param){
    NV.action.search(param);
}

onMount(()=>{
    NV.init();
    //window.NV = NV;
});

//좌측 메뉴 넓이 조정 결과
$:{
    $gapLeftStore; onResize();
}

const NV = {
    init: function(){
        this.grid1.init();
        this.grid2.init();
    }
    , data:{
        sltJobgroup: "Job"
        , sltProc: {}
        , display:{
            popupJobDetail:false
            , popupProcDetail:false
        }
    }
    , dom: {}
    , param:{}
}

NV.action = {
    //조회
    search: function(param){
        //console.log(ID, param, JSON.stringify(param), JSON.stringify(param)=="{}");
        if(!param || JSON.stringify(param)=="{}") return;
        app.modal.open();//모달
        app.fetch("/batchResult/selectBatchResult", {...param})
        .then(async (response)=>{
            let today = (new Date()).print("yyyy-mm-dd");
            //console.log(response);

            //그리드 렌더링
            response.data.jobList.map((item)=>{                
                NV.grid1.addBgClass(item);
            });
            response.data.procList.map((item)=>{                
                NV.grid2.addBgClass(item);
            });
            NV.grid1.orgData = [...response.data.jobList];
            NV.grid2.orgData = [...response.data.procList];
            await NV.grid1.target.resetData(NV.grid1.orgData);
            await NV.grid2.target.resetData(NV.grid2.orgData);
            
            app.modal.close();
        })
        .catch((e)=>{
            //console.log(e);
            app.modal.close();
        }); 
    }
}

NV.grid1 = {
    target: null
    , gapTop:127 + 20
    , orgData: []
    , init: function(){
        const ds = this;
        const option = {
            el: NV.grid1.target
            , scrollX: false
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore
            , bodyHeight: window.innerHeight - this.gapTop//'fitToParent'
            , rowHeaders: ['rowNum']
            , columns: [
                {header:'그룹코드', name:'jobgroup', width:70, align:"center"}
                , {header:'그룹명', name:'jobgroupnm', width:150, align:"center", filter:"select"}
                , {header:'배치코드', name:'jobcode', width:100, align:"center"}
                , {header:'배치업무명', name:'jobname', align:"left"}
                , {header:'실행배치', name:'jobfile', width:350, align:"left"}
                , {header:'-', name:'job', width:50, align:"center"
                    , formatter: (item)=>{
                        //console.log(item);
                        let result = "";
                        if(item.row.jobstatus=="R"){
                            result = icon.working(1);
                        }else if(item.row.jobstatus=="E"){
                            result = icon.warn(1.1);
                        }else if(item.row.jobstatus=="N"){
                            result = icon.notAllowed(1);
                        }else if(item.row.jobstatus=="S"){
                            result = `<span style="color:magenta">${icon.skip(1.4)}</span>`;
                        }else if(item.row.jobstatus=="C"){
                            if(item.row.jobchk=="O"){
                                result = `<span style="color:seagreen">${icon.check(1)}</span>`;
                            }else{
                                result = `<span style="color:blue">${icon.message()}</span>`;
                            }
                        }else{
                            return item.value;
                        }
                        return result
                    }
                }
                , {
                    header:'실행결과'
                    , name:'jobstatusname', align:'center', width:100
                    //, renderer: CustomRenderer
                    , filter:"select"                    
                }
            ]
        }
        this.target = tuiGrid.make(option);
        this.bindEvent();
    }    
    , bindEvent: function(){
        const grid = this.target;
        grid.on("click", (ev)=>{
            if(ev.rowKey==undefined) return;
            const rowKey = ev.rowKey;
            const rowData = grid.getRow(rowKey);
            NV.data.sltJob = rowData;
            NV.data.display.popupJobDetail = true;
        });
    }
    , addBgClass: (item)=>{
        item["_attributes"] = {className:{row:["job", item.jobstatus]}}
    }
}

NV.grid2 = {
    target: null
    , gapTop:127 + 20
    , orgData: []
    , init: function(){
        const ds = this;
        const option = {
            el: NV.grid2.target
            , scrollX: true
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore
            , bodyHeight: window.innerHeight - this.gapTop//'fitToParent'
            , rowHeaders: ['rowNum']
            , columns: [
                {header:'프로세스코드', name:'proccodeorg', width:120, align:"center"}
                , {header:'프로세스명', name:'procname', align:"left"}
                , {header:'실행배치', name:'jobfile', width:200, align:"left"}
                , {header:'실행프로세스', name:'procfile', width:300, align:"left"}
                , {header:'-', name:'job', width:50, align:"center"
                    , formatter: (item)=>{
                        //console.log(item);
                        if(item.row.procstatus=="C"){//정상
                            if(item.row.errflag=="X"){//스킵처리
                                return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span>`;
                            }else if(item.row.errflag=="E"){//오류스킵
                                return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span>`;
                            }else if(item.row.errflag=="Q"){//대기중스킵
                                return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span>`;
                            }else if(item.row.errflag=="R"){//처리중스킵
                                return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span>`;
                            }else if(item.row.errflag=="N"){//자료없음
                                return `<span style="color:seagreen;margin-right:4px;">${icon.warn(1.1)}</span>`;
                            }else if(item.row.errflag=="H"){//수작업완료
                                return `<span style="color:seagreen;margin-right:4px;">${icon.check(1)}</span>`;
                            }else{
                                return `<span style="color:seagreen;margin-right:4px;">${icon.check(1)}</span>`;
                            }
                        }else if(item.row.procstatus=="X"){// -
                            return "";
                        }else if(item.row.procstatus=="H"){//수작업
                            return "";
                        }else if(item.row.procstatus=="Q"){//대기중
                            return "";
                        }else if(item.row.procstatus=="R"){//처리중
                            return `<span style="color:rgb(0 0 140);margin-right:4px;">${icon.working()}</span>`;
                        }else if(item.row.procstatus=="E"){//처리오류
                            return `<span style="margin-right:4px;">${icon.warn(1.1)}</span>`;
                        }else if(item.row.procstatus=="S"){//작업스킵
                            return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span>`;
                        }else if(item.row.procstatus=="N"){//미실행
                            return `<span style="margin-right:4px;">${icon.notAllowed(0.8)}</span>`;                    
                        }else{
                            return "";
                        }
                    }
                }
                , {
                    header:'실행결과'
                    , name:'procstatusnm', align:'center', width:100
                    , filter:"select"                    
                }
                , {header:'처리건수', name:'resultcnt', align:'right', width:100
                    , formatter:(item)=>{                 
                        return (item.value??"0").money();
                    }
                }
                , {header:'시작시간', name:'starttime', align:'center', width:100}
                , {header:'종료시간', name:'endtime', align:'center', width:100}
                , {header:'작업자ID', name:'chkuser', align:'center', width:100}
                , {header:'비고', name:'procmemo', align:'center', width:200}
            ]
        }
        this.target = tuiGrid.make(option);
        this.bindEvent();
    }    
    , bindEvent: function(){
        const grid = this.target;
        grid.on("click", (ev)=>{
            if(ev.rowKey==undefined) return;
            const rowKey = ev.rowKey;
            const rowData = grid.getRow(rowKey);
            NV.data.sltProc = rowData;
            NV.data.display.popupProcDetail = true;
        });
    }
    , addBgClass: (item)=>{
        if(item.procstatus=="C"){
            if(item.errflag=="X"){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(item.errflag=="E"){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(item.errflag=="Q"){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(item.errflag=="R"){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(item.errflag=="N"){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(item.errflag=="H"){
                //item["_attributes"] = {className:{row:["skip"]}}
            }else{
                //item["_attributes"] = {className:{row:["complete"]}}
            }
        }else if(item.procstatus=="X"){                    
            item["_attributes"]= {className:{row:["waiting"]}}
        }else if(item.procstatus=="H"){
            item["_attributes"] = {className:{row:["waiting"]}}
        }else if(item.procstatus=="Q"){
            item["_attributes"] = {className:{row:["waiting"]}}
        }else if(item.procstatus=="R"){
            item["_attributes"] = {className:{row:["working"]}}
        }else if(item.procstatus=="E"){
            item["_attributes"] = {className:{row:["error"]}}
        }else if(item.procstatus=="S"){
            item["_attributes"] = {className:{row:["skip"]}}
        }else if(item.procstatus=="N"){
            item["_attributes"] = {className:{row:["notToday"]}}
        }
    }
}

//탭메뉴
NV.tab = {
    target: null
    , list: [
        {jobgroup:"Job", jobgroupName:"배치 처리결과", dom:null}
        , {jobgroup:"Proc", jobgroupName:"프로세스 처리결과", dom:null}
    ]
    , obj:{}
    , init: ()=>{
        const ds = NV.tab;
        NV.tab.list.map((item, index)=>{
            NV.tab.obj[item.jobgroup] = item;
        });
    }
    , action: {
        select: async (_jobgroup="")=>{
            //console.log(_jobgroup);
            const ds = NV.tab;
            NV.data.sltJobgroup = _jobgroup;
        }
    }
}

let resizeTimer = null;
function onResize(ev){
    ev?.preventDefault();
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(()=>{
        //console.log(window.innerWidth, window.innerHeight);
        NV.grid1.target.setWidth(Math.max(app.css.contentMinWidth, window.innerWidth-$gapLeftStore));
        NV.grid1.target.setBodyHeight(window.innerHeight - NV.grid1.gapTop);
        NV.grid2.target.setWidth(Math.max(app.css.contentMinWidth, window.innerWidth-$gapLeftStore));
        NV.grid2.target.setBodyHeight(window.innerHeight - NV.grid2.gapTop);
    }, 200);
}    
</script>

<!-- 중간선 이벤트용 -->
<svelte:window on:resize={onResize}/>


<batchResult>
    <!-- Tab 메뉴 영역 -->
    <ul bind:this={NV.tab.target}                
        on:selectstart={(ev)=>{
            ev.preventDefault();
            ev.stopPropagation();
        }}
        on:mousemove={(ev)=>{
            ev.preventDefault();
            ev.stopPropagation();
        }} 
    >
        {#each NV.tab.list as item, index (item.jobgroup) }
            <li class:on={NV.data.sltJobgroup==item.jobgroup}>
                <div bind:this={item.dom}
                    on:click={()=>{NV.tab.action.select(item.jobgroup);}}
                    on:keyup
                    role="button" tabindex={index}
                >
                    {item.jobgroupName}
                </div>
            </li>
        {/each}
    </ul>
    <div id="topDescription">
        <div id="topDescriptionLeft">
            <span id="topDescriptionLeftListCount">
                목록개수: {NV.data.sltJobgroup=="Job" ? NV.grid1.orgData.length : NV.grid2.orgData.length}
            </span>
        </div>
        <div id="leftSideDesriptionCenter"></div>
        <div id="leftSideDesriptionRight"></div>
    </div>
    <!-- Grid -->
    <div class="grid-topline-hide">
        <div bind:this={NV.grid1.target} style:display={NV.data.sltJobgroup=="Job"?"":"none"}></div>
        <div bind:this={NV.grid2.target} style:display={NV.data.sltJobgroup=="Proc"?"":"none"}></div>
    </div>
    <!-- 상세팝업 -->
    {#if NV.data.display.popupJobDetail==true}
        <PopupJobDetail on:close={()=>{NV.data.display.popupJobDetail=false}}            
            job={NV.data.sltJob}
            from={"BatchResult"}
            height={800}
        />
    {/if}
    {#if NV.data.display.popupProcDetail==true}
        <PopupProcDetail on:close={()=>{NV.data.display.popupProcDetail=false}}            
            proc={NV.data.sltProc}
            from={"BatchResult"}
            height={800}
        />
    {/if}
</batchResult>

<style lang="scss">
batchResult {
    display:block;
    position:relative;
    top:-1px;

    //탭매뉴
    ul {
        z-index:2;
        position:relative;
        display:flex;  
        flex-direction:row;
        align-items:flex-end;
        margin:0;
        width:100%;//calc(100% - 2px);
        height:2rem;
        padding: 0.2rem 0 0 0;
        list-style: none;
        background-color: #bbb;
        overflow:hidden;
        font-size:1rem;
        white-space:nowrap;
        /* 왼쪽 여백 */
        &::before {
            content:"";
            min-width:20px;
            border-bottom:solid 1px rgb(0 0 0/50%);
        }
        /* 오른쪽 여백 */
        &::after {
            content:"";
            width:100%;//calc(100% - 612px);
            border-bottom:solid 1px rgb(0 0 0/30%);
        }
        /* 기본 탭 */
        li{
            display:inline-flex;
            justify-content: center;
            align-items:center;
            height:1.4rem;
            //min-width:100px;
            border: solid 1px rgb(0 0 0/15%);
            border-left:solid 1px rgb(0 0 0/15%);
            border-right:0;
            border-bottom:solid 1px rgb(0 0 0/50%);
            padding: 0.2rem 1rem;
            background-color:#dddddd;
            text-align:center;
            cursor:pointer;
            color:rgba(0 0 0/60%);
            font-size:0.8rem;

            /* 마우스가 올려 졌을 때, 커서 모양 변경 */
            &:hover{
                color:black;
            }

            /* 활성탭 */
            &.on {
                border: solid 1px rgb(0 0 0/50%);
                border-bottom:solid 1px rgb(0 0 0/6%);
                background: linear-gradient(360deg, #efefef, #fff);
                font-weight:bold;
                color:black;
                font-size:0.8rem;
            }
        }

    }
    //테이블 정보
    #topDescription{
        border:solid 0px red;
        display:flex;
        justify-content:space-between;
        //top:2px;
        width:100%;
        height:1.5rem;
        line-height:1.5rem;
        font-size:0.7rem;
        border-bottom: solid 1px rgb(0 0 0/30%);
        #topDescriptionLeft{
            display:inline-flex;
            padding-left:0.7rem;
            #topDescriptionLeftListCount{
                width:90px;
                font-family:auto;
            }
        }
    }
    //그리드
    div.grid-topline-hide{
        z-index:1;
        position:relative;
    }
}
//app.scss 그리드 색상 적용
</style>