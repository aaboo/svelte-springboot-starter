<script>
const ID = "daemon";

import { onMount } from 'svelte';
import { menuStore, gapLeftStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';
import Popup from '../Popup.svelte';
import icon from '$lib/icon';

//조회 - Frame.svelte.searchFromChild(ev)
export function search(param){
    NV.action.selectJob(param);
}

onMount(()=>{
    NV.init();
    //window.NV = NV;
});
    
//좌측 메뉴 넓이 조정 결과
$:{
    //window.resize
    $gapLeftStore; gap.orgX; onResize();
}

const NV = {
    init: function(){
        const ds = this;
        this.tab.init();
        this.grid.init();        
        ds.action.selectJob(app.ss.getSearch()); 
    }
    , dom: {
        jobList:[]
        , procPopupComment: null //PROC 팝업창 상세 팝업 작업자 한 줄 코멘트
    }
    , param:{}
    //현재 상태 자료
    , data:{
        jobList: [] //JOB list: 왼쪽리스트 기준
        , jobProcListObj: {} //전체 JOBCODE별 PROC List
        , jobObj: {} //JOBCODE별 JOB
        , procObj: {} //PROCCODE별 PROC //procList는 불필요하여 jobProcListObj의 것으로만 사용함
        , sltJob:{} //현재 선택된 JOB의 정보
        , sltJobIndex: 0 //현재 선택된 JOB의 왼쪽리스트 기준 index
        , sltJobcode:"" //현재 선택된 JOBCODE
        , sltJobgroup: "" //현재 선택된 탭(Jobgroup)
        , sltProc:{} //현재 선택된 Proccode 상세 정보
        , sltProccode:"" //현재 선택된 Proccode        
        , runningGroupList:[] //현재 진행중인 그룹리스트
        //원본 데이터(DB로 부터 받아온 원본)
        , org:{
            jobList:[]
            , jobProcListObj:{}
        }
        , clear: ()=>{            
            NV.data.jobList = [];
            NV.data.jobProcListObj = {};
            NV.data.jobObj = {};
            NV.data.sltJob = {}; //현재 선택된 proc의 정보
            NV.data.sltJobIndex = 0; //현재 선택된 job의 왼쪽리스트 기준 index
            NV.data.sltJobcode = ""; //현재 선택된 탭
            NV.data.sltJobgroup = "";
            NV.data.sltProc = {};
            NV.data.sltProccode = "";
            NV.data.runningGroupList = [];
            NV.data.org.jobList = [];
            NV.data.org.jobProcListObj = {};
        }
        //데이터 세팅 함수data = {jobList:[], jobProcListObj:{}}
        //DB에서 전체 JOBLIST 조회시 사용된다.
        , set: (data, callback)=>{            
            NV.data.jobList = [...data.jobList];
            NV.data.jobProcListObj = {...data.jobProcListObj};
            NV.data.jobObj = {}
            NV.data.jobList.map((item, index)=>{
                NV.data.jobObj[item.jobcode]=item;
                //NV.data.jobProcListObj[item.jobcode].procList = NV.data.org.jobProcListObj[item.jobcode];
            });
            NV.data.procObj = {};
            for(let jobcode in NV.data.org.jobProcListObj){
                //console.log("jobcode", jobcode, NV.data.org.jobProcListObj[jobcode]);
                NV.data.org.jobProcListObj[jobcode].map((item, index)=>{
                    NV.data.procObj[item.proccode] = item;
                });
            }
        }
        , leftSideWrapperScrollTop: 0 //왼쪽리스트 세로스크롤 현재 위치(NV.dom.leftSideWrapper 이벤트)
        , btnStatus: null //우측화면 실행배치 버튼상태 조정결과
        , isFocusRunningAlways: true //진행중(R)인 jobcode로 계속 포커싱
    }
}

NV.action = {
    //조회
    selectJob: function(param){
        const ds = this;
        //console.log(ID, param, JSON.stringify(param), JSON.stringify(param)=="{}");
        if(!param || JSON.stringify(param)=="{}") return;
        app.modal.open();
        app.fetch("/daemon/selectDaemon", {...param})
        .then(async (response)=>{
            //console.log(response);
            if(!(response.data && response.data.jobList)) return false;
            if(response.data.jobList.length>0){
                //원본데이터 확보
                NV.data.org.jobList = [...response.data.jobList];
                NV.data.org.jobProcListObj = {...response.data.jobProcListObj};
                //데이터 세팅 + 왼쪽리스트 렌더링 완료
                NV.data.set(response.data);
                //전체탭 선택 - NV.data.sltJobcode 생성됨
                NV.tab.action.select();
            }else{                
                NV.data.clear();
                NV.grid.target.resetData([]);
                app.modal.close();   
                alert("자료가 존재하지 않습니다.");
            }
            app.modal.close();
            //console.log(NV.dom.leftSideWrapperBody.querySelectorAll("div.job"));
        })
        .catch((e)=>{
            console.log(e);
            app.modal.close();
        });
    }
    //JOBCODE 선택
    , selectJobDetail: function(_jobcode){
        const jobcode = _jobcode??NV.data.sltJobcode??"";        
        if(jobcode==""){
            NV.grid.target?.resetData([]);
        }else{
            NV.data.sltJobcode = jobcode;
            NV.data.sltJob = NV.data.jobList.find((item, index)=>{
                item.sltJobIndex = index;
                return item.jobcode==jobcode;
            })
            NV.data.sltJobIndex = NV.data.sltJob.sltJobIndex;
            //app.ss.setData({sltJobIndex: NV.data.sltJobIndex});
            if(NV.data.org.jobProcListObj[jobcode]){
                NV.grid.target?.resetData([...NV.data.org.jobProcListObj[jobcode]]);
            }else{
                NV.grid.target?.resetData([]);
            }
        }
        app.modal.close();
    }
    //LeftList 현재 선택된 JOB으로 포커싱
    , focusCurrentJob: function(){
        if(NV.data.sltJobcode==""){
            NV.data.sltJobcode=NV.data.jobList[0].jobcode;
        }   
        
        let testJobList;
            
        /** sltJobIndex 선택 로직
         * 현재 탭내의 NV.data.jobList중에 선택된 jobcode가 없으면
         *   - 첫번째 리스트의 sltJobcode를 지정해 준다.
         */
        if(NV.data.sltJobcode=="") testJobList = [];
        else testJobList = NV.data.jobList.filter((item)=>item.jobcode==NV.data.sltJobcode);
        //console.log(testJobList);        
        if(testJobList.length==0) NV.data.sltJobcode=NV.data.jobList[0].jobcode;

        NV.action.selectJobDetail();

        const scrollTo = {
            left:0
            , top:(NV.dom.jobList[0]?.clientHeight??0)*(NV.data.sltJobIndex) - 100
        };
        //console.log(NV.data.sltJobIndex, scrollTo);
        NV.dom.leftSideWrapper?.scrollTo(scrollTo);
    }
    ///LeftList 최상단으로 포커싱
    , focusTop: function(){
        const scrollTo = {
            left:0
            , top:0
        };
        NV.dom.leftSideWrapper.scrollTo(scrollTo);
    }
    ///LeftList 최하단으로 포커싱
    , focusBottom: function(){
        const scrollTo = {
            left:0
            , top:(NV.dom.jobList[0]?.clientHeight??0)*NV.data.jobList.length
        };
        NV.dom.leftSideWrapper.scrollTo(scrollTo);
    }
    //정상 Error로 표기 전환(C)
    , updateDaemonErrStart: function(ev){
        ev.preventDefault();
        const param = {daemoncode: NV.data.sltJobcode};
        app.fetch("/daemon/updateDaemonErrStart", param).then(async (response)=>{
            console.log(response);
            if(response.data>0){
                alert("정상 Error로 표기 되었습니다.");
                NV.data.sltJob.alertonoff="C";
            }else alert("정상 Error 표기에 실패하였습니다.");
        }).finally(()=>{
            app.modal.close();
        });
    }
    //비정상 Error로 표기 전환(A)
    , updateDaemonErrFinish: function(ev){
        ev.preventDefault();
        const param = {daemoncode: NV.data.sltJobcode};
        app.fetch("/daemon/updateDaemonErrFinish", param).then(async (response)=>{
            console.log(response);
            if(response.data>0){
                alert("비정상 Error로 표기 되었습니다.");
                NV.data.sltJob.alertonoff="A";
            }else alert("비정상 Error 표기에 실패하였습니다.");
        }).finally(()=>{
            app.modal.close();
        });
    }
}
//상세그리드
NV.grid = {
    target: null
    , showMeTheFooter: false
    , init: async function(){
        //console.log("NV.popup.data.bodyHeight", NV.popup.data.bodyHeight - 81);
        const option = {
            el: NV.grid.target
            , scrollX: true
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore - gap.orgX
            , bodyHeight: window.innerHeight - NV.dom.procInfo.clientHeight - 80
            , columns: [
                {header:'no', name:'cnt', align:"center", width:80, align:"center"}
                , {header:'시간', name:'workdate', align:"center", width:200, align:"center"}
                , {header:'상태', name:'daemonstatusname', align:"center", width:80, align:"center"}
                , {header:'건수', name:'checkcnt', align:"center", width:120, align:"right"
                    , formatter:(item)=> (item.value??"0").money()
                }
                , {header:'프로세스 메시지', name:'checkmsg', align:"center", align:"left"}
            ]
        }
        this.target = tuiGrid.make(option);
    }
}
//탭메뉴
NV.tab = {
    target: null
    , list: [
        {jobgroup:"", jobgroupName:"전체", dom:null}
        , {jobgroup:"R", jobgroupName:"실시간 자료처리", dom:null}
        , {jobgroup:"F", jobgroupName:"파일송수신", dom:null}
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
            if(NV.data.org.jobList.length==0) return;
            //왼쪽 리스트 업데이트
            await new Promise((next)=>{
                if(NV.data.sltJobgroup==""){//전체
                    NV.data.jobList = [...NV.data.org.jobList];
                }else{//탭별 리스트 필터                
                    NV.data.jobList = NV.data.org.jobList.filter((item)=>item.jobgroup == NV.data.sltJobgroup);
                }
                next();
            });
            //setTimeout(()=>{
                //if(NV.data.sltJobgroup=="") NV.data.sltJobcode="";
                NV.action.focusCurrentJob();
            //}, 0);
            ds.action.tabRun(); //실행중인 탭 표시
        }
        //실행중인 탭 표시
        , tabRun: ()=>{
            NV.data.runningGroupList = [];
            NV.data.org.jobList.map((item, index)=>{
                if(["RQ","ST","RV"].indexOf(item.jobstatus)>-1){//실행중
                    if(NV.data.runningGroupList.indexOf(item.jobgroup)<0){
                        NV.data.runningGroupList = [...NV.data.runningGroupList, item.jobgroup];
                    }
                }
            });
            if(NV.data.runningGroupList.length>0) NV.data.runningGroupList = [...NV.data.runningGroupList, ""];
            //console.log("runningGroupList", NV.data.runningGroupList);
        }
    }
}

//버튼 상태 변화 값
NV.design = {
    //좌측 리스트 BG 변경
    getBgClass:(item)=>{
        if(item.jobstatus=="ER" && item.alertonoff=="A") return "ERA";
        else if (item.jobstatus=="ER" && item.alertonoff=="C") return "ERC";
        else if (item.errwebchk=="1" && item.alertonoff=="A") return "ERA";
        else if (item.errwebchk=="1" && item.alertonoff=="C") return "ERC";
        else if (item.jobstatus=="00") return "OO";
        else if (item.jobstatus=="RT") return item.jobstatus;
        else if (["RQ","ST","RV"].indexOf(item.jobstatus)>-1) return item.jobstatus;
        else if (["RS","X"].indexOf(item.jobstatus)>-1) return item.jobstatus;
        else if (item.jobstatus=="ER" && item.alertonoff=="X") return "ERX";
        else if (item.errwebchk=="ER" && item.alertonoff=="X") return "ERX";
    }
    //좌측 리스트 아이콘 변경
    , getIcon:(item)=>{
        if(item.jobstatus=="ER" && item.alertonoff=="A") return icon.warn(1.1);
        else if (item.jobstatus=="ER" && item.alertonoff=="C") return icon.warn(1.1);
        else if (item.errwebchk=="1" && item.alertonoff=="A") return icon.warn(1.1);
        else if (item.errwebchk=="1" && item.alertonoff=="C") return icon.warn(1.1);
        else if (item.jobstatus=="00") return `<span style="color:seagreen">${icon.check(1)}</span>`;
        else if (item.jobstatus=="RT") return `<span style="color:seagreen">${icon.check(1)}</span>`;
        else if (["RQ","ST","RV"].indexOf(item.jobstatus)>-1) return icon.working(1);
        else if (["RS","X"].indexOf(item.jobstatus)>-1) return '';
        else if (item.jobstatus=="ER" && item.alertonoff=="X") 
            return `<span style="color:maroon">${icon.check(1)}</span>`
        else if (item.errwebchk=="ER" && item.alertonoff=="X") 
            return `<span style="color:maroon">${icon.check(1)}</span>`
        else return '';
    }
}

//좌우 넓이 조정
const gap = {
    status: "none"
    , dom: null
    , minX: 50
    , orgX: 700
    , movX: 700
    , mouseenter: function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        if(gap.status != "mousemove"){
            gap.status = "mouseenter";
        }
        document.body.style.cursor="grab";
        gap.watch(ev);
    }
    , mouseleave: function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        if(gap.status == "mouseenter"){
            gap.status = "none";
            document.body.style.cursor="";
        }
    }
    , mousedown: function(ev){
        //ev.preventDefault();
        ev.stopPropagation();
        gap.status = "mousedown";
        document.body.style.cursor="grabbing";
        gap.watch();
        document.body.onselectstart = function(){return false;};
    }
    , mouseup: function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        gap.status = "mouseenter";
        gap.orgX = gap.movX
        document.body.style.cursor="";
        gap.watch(ev);
        document.body.onselectstart = null;
    }
    , mousemove: function(ev){
        //ev.preventDefault();//모바일에서
        ev.stopPropagation();
        if(gap.status=="mousedown" || gap.status=="mousemove"){
            gap.status = "mousemove"
            let movX = (ev.clientX??ev.touches[0].clientX) - $gapLeftStore;
            if(movX < gap.minX) movX = gap.minX;
            gap.movX = movX;
            document.body.style.cursor="grabbing";
            gap.watch(ev);
        }
    }
    , watch: function(ev){
        //console.log(gap.status, gap.orgX, gap.movX, ev);
    }
}

//윈도우 리사이즈
let resizeTimer = null;
function onResize(ev){
    ev?.preventDefault();
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(()=>{        
        //console.log(NV.dom.procInfo.clientHeight);        
        //NV.grid.target.refreshLayout();
        NV.grid.target.setWidth(Math.max(app.css.contentMinWidth, window.innerWidth - $gapLeftStore - gap.orgX));
        NV.grid.target.setHeight(window.innerHeight - NV.dom.procInfo.clientHeight - 80);
    }, 300);
}

</script>

<!-- 간격조정선 이벤트용 -->
<svelte:window 
    on:resize={onResize}
    on:mousemove={gap.mousemove}
    on:mouseup={gap.mouseup}
    on:touchmove={gap.mousemove}
    on:touchend={gap.mouseup}
/>

<demon bind:this={NV.dom.root}>
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
            <li class:on={NV.data.sltJobgroup==item.jobgroup} title={item.jobgroup}>
                <div bind:this={item.dom}
                    on:click={()=>{NV.tab.action.select(item.jobgroup);}}
                    on:keyup
                    role="button" tabindex={index}
                >
                    {#if NV.data.runningGroupList.indexOf(item.jobgroup)>-1}
                    <span class="working">{@html icon.working(0.8)}</span>
                    {/if}
                    {item.jobgroupName}
                </div>
            </li>
        {/each}
    </ul>
    
    <div id="content">
        <!-- 왼쪽 영역 -->
        <div id="leftSide" bind:this={NV.dom.lside} style="width:{gap.orgX}px">
            <div id="leftSideDesription">
                <span id="leftDescriptionLeftListCount">목록개수: {NV.data.jobList.length}</span>         
                <div id="leftSideDesriptionRight">
                    <button class="xTiny" title="리스트 상단 이동"
                        on:click={NV.action.focusTop}
                        disabled={NV.data.leftSideWrapperScrollTop==0}                    
                    >{@html icon.toUp(0.8)}</button>
                    <button class="xTiny" title="리스트 하단 이동"
                        disabled={NV.data.leftSideWrapperScrollTop + NV.dom.leftSideWrapper?.offsetHeight >= NV.dom.leftSideWrapper?.scrollHeight}
                        on:click={NV.action.focusBottom}
                    >{@html icon.toDown(0.8)}</button>
                    <button class="xTiny" title="실행 추적"
                        style:opacity={NV.data.isFocusRunningAlways?"1":"0.4"}
                        on:click={()=>{
                            NV.data.isFocusRunningAlways = !NV.data.isFocusRunningAlways
                            NV.action.focusCurrentJob();
                        }}
                    >{@html icon.pin(0.8)}</button>
                </div>
            </div>
            <div id="leftSideWrapper" bind:this={NV.dom.leftSideWrapper} on:scroll={(ev)=>{
                NV.data.leftSideWrapperScrollTop = ev.target.scrollTop;
            }}>
                <div id="leftSideWrapperBody" bind:this={NV.dom.leftSideWrapperBody}>
                    <!--
                    <div class="job C">
                        <span class="status">{@html icon.pin(1)}</span>
                        <span class="jobTitle">C
                            001. JET0001 수작업-배치자동화 데몬 Restart
                            <span class="jobRemain">1/1</span>
                            <span class="time">00:05:36</span>
                        </span>                        
                        <span class="jobWorking"></span>
                    </div>
                    <div class="job C">
                        <span class="status">{@html `<span style="color:seagreen">${icon.check(1)}</span>`}</span>
                        <span class="jobTitle">C
                            001. JET0001 수작업-배치자동화 데몬 Restart
                            <span class="jobRemain">1/1</span>
                            <span class="time">00:05:36</span>
                        </span>    
                        <span class="jobWorking"></span>                
                    </div>
                    <div class="job C">
                        <span class="status">{@html `<span style="color:blue">${icon.message()}</span>`}</span>
                        <span class="jobTitle">C
                            001. JET0001 수작업-배치자동화 데몬 Restart
                            <span class="jobRemain">1/1</span>
                            <span class="time">00:05:36</span>
                        </span>
                        <span class="jobWorking"></span>                
                    </div>
                    <div class="job S">
                        <span class="status">{@html `<span style="color:magenta">${icon.skip(1.4)}</span>`}</span>
                        <span class="jobTitle">S
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF
                            <span class="jobRemain">7/9</span>                            
                            <span class="time">00:05:36</span>
                        </span>
                        <span class="jobWorking"></span>
                    </div>
                    <div class="job X">
                        <span class="status"></span>
                        <span class="jobTitle">X
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF
                            <span class="jobRemain">7/9</span>
                            <span class="time">09:10:31</span>
                        </span>
                        <span class="jobWorking"></span>
                    </div>
                    <div class="job X">
                        <span class="status">{@html `<span style="color:red">${icon.cross(0.9)}</span>`}</span>
                        <span class="jobTitle">X
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF
                            <span class="jobRemain">7/9</span>
                            <span class="time">09:10:31</span>
                        </span>
                        <span class="jobWorking"></span>
                    </div>
                    <div class="job N">
                        <span class="status">{@html icon.notAllowed(1)}</span>
                        <span class="jobTitle">N
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF
                            <span class="jobRemain">7/9</span>
                            <span class="time">09:10:31</span>
                        </span>                
                        <span class="jobWorking"></span>
                    </div>
                    <div class="job E">
                        <span class="status">{@html icon.warn(1.1)}</span>
                        <span class="jobTitle">E
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF167. JPR0180 크론탭
                            <span class="jobRemain">7/9</span>
                            <span class="time">09:10:31</span>
                        </span>                
                        <span class="jobWorking"><span>{@html icon.rightBold(0.9)}</span></span>
                    </div>
                    <div class="job R">
                        <span class="status">{@html icon.working(1)}</span>
                        <span class="jobTitle">R
                            167. JPR0180 크론탭-차액정산 결과파일 DB반영 처리 - DIF
                            <span class="jobRemain">7/9</span>
                            <span class="time">09:10:31</span>
                        </span>                
                        <span class="jobWorking"></span>
                    </div>
                    -->
                    {#each NV.data.jobList as item, index (item.jobcode)}
                        <div class="job {NV.design.getBgClass(item)}"
                            bind:this={NV.dom.jobList[index]}
                            class:sltJob={NV.data.sltJobcode==item.jobcode}
                            on:click={()=>{ NV.action.selectJobDetail(item.jobcode); }}
                            on:keyup
                            role="button" tabindex={index}
                        >
                            <span class="status">                                
                                {@html NV.design.getIcon(item)}
                            </span>
                            <span class="jobTitle"> 
                                <!-- {item.jobstatus} {item.alertonoff} {item.errwebchk} -->
                                {`${item.no}. ${item.jobcode} ${item.jobname}`}                                
                                <span class="time">{item.jobendtime}</span>
                            </span>
                            <span class="jobWorking">
                                {#if NV.data.sltJobcode==item.jobcode}
                                    <span>{@html icon.rightBold(0.9)}</span>
                                {/if}
                            </span>
                        </div>
                    {/each}
                    

                </div>
            </div>
        </div>
        <!-- 간격조정선 -->
        <div id="gap"
            bind:this={gap.dom}
            on:mouseenter={gap.mouseenter}
            on:mouseleave={gap.mouseleave}
            on:mousedown={gap.mousedown}
            on:touchstart={gap.mousedown}
            style="left:{gap.movX}px"
            role="button" tabindex=0
        />
        <!-- 오른쪽 영역 -->        
        <div id="rightSide" bind:this={NV.dom.rside}>
            <div class="labelWrapper" bind:this={NV.dom.procInfo}>
                <table border="1">
                    <colgroup>
                        <col width="100">
                        <col width="200">
                        <col width="110">
                        <col width="200">
                    </colgroup>
                    <tr>
                        <th>경로</th>
                        <td colspan="4">
                            <span class="jobcode">{NV.data.sltJob?.jobcode??'-'}</span>
                            <span>
                                {NV.data.sltJob?.daemonsvr??'-'}({NV.data.sltJob?.daemonsvr_??'-'}):
                                {NV.data.sltJob?.daemonpath??'-'}<strong>{NV.data.sltJob?.daemonfile??'-'}</strong>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <th>서비스명</th>
                        <td colspan="4">
                            <div>{NV.data.sltJob?.daemongroupname??'-'}({NV.data.sltJob?.daemongroup??'-'}) >
                            <div class="jobname">{NV.data.sltJob?.jobname??'-'}</div>
                            {#if NV.data.sltJob?.jobstatus=="ER" || NV.data.sltJob?.errwebchk=="1"}
                                <div class="divButtons">
                                    {#if NV.data.sltJob?.alertonoff=="A"}
                                        <button on:click={NV.action.updateDaemonErrStart} class="green">정상 Error로 표기 전환</button>
                                    {:else if NV.data.sltJob?.alertonoff=="C"}
                                        <button on:click={NV.action.updateDaemonErrFinish} class="maroon">비정상 Error로 표기 전환</button>
                                    {/if}
                                </div>
                            {/if}
                        </td>
                    </tr>
                    <tr>
                        <th>IP:PORT</th>
                        <td>{NV.data.sltJob?.netip??'-'}:{NV.data.sltJob?.netport??'-'}</td>
                        <th>경고기준</th>
                        <td colspan="2">
                            경과시간: {NV.data.sltJob?.alerttime??'-'}분
                            / 잔여건수: {NV.data.sltJob?.alertcnt??'-'}건
                        </td>
                    </tr>
                    <tr>
                        <th>실시간구분</th>
                        <td>{NV.data.sltJob?.jobgroupname??'-'}({NV.data.sltJob?.daemongroup??'-'})</td>
                        <th>마지막 응답시간</th>
                        <td align="center">{NV.data.sltJob?.jobendtime??'-'}</td>
                        <td rowspan="2" class="elapsed">
                            <span>
                                {#if NV.data.sltJob?.elapseddd!='0'}
                                    {(NV.data.sltJob?.elapseddd??'0').money()}일
                                {/if}
                                {#if NV.data.sltJob?.elapsedhh24!='0'}
                                    {(NV.data.sltJob?.elapsedhh24??'0').money()}시간
                                {/if}
                                {(NV.data.sltJob?.elapsedmi??'0').money()}분
                                / {(NV.data.sltJob?.checkcnt??'0').money()}건
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <th>등록</th>
                        <td>{NV.data.sltJob?.created??'-'} / {NV.data.sltJob?.userid??'-'}</td>
                        <th>현재시간</th>
                        <td align="center">{NV.data.sltJob?.realdate??'-'}</td>
                    </tr>
                    <tr>
                        <th>메모</th>
                        <td colspan="4">{NV.data.sltJob?.memo??'-'}</td>
                    </tr>
                </table>
            </div>
            <!-- PROC 그리드 -->
            <div class="gridHeaderBg1"
                bind:this={NV.grid.target}
                style="position:relative;top:-1px"
            />
        </div>
    </div>
</demon>
    
<style lang="scss">
demon {
    display:block;
    position:relative;
    top:-1px;
    //border:solid 1px white;
    width:calc(100% + 1px);
    min-width:650px;
    //height:100vh;
    margin:1px 0;

    div#content{
        display:table;
        table-layout:fixed;
        width:100%;
        height:calc(100vh - 86px);
        border:solid 0px green;
        margin:0;
        padding:1;
        //좌측 리스트
        &>div#leftSide{
            display:table-cell;
            vertical-align:top;
            #leftSideDesription{
                position: relative;
                //top:2px;
                height:1.5rem;
                line-height:1.5rem;                
                padding-left:0.5rem;
                font-size:0.7rem;
                #leftDescriptionLeftListCount{
                    font-family:auto;
                }
                #leftSideDesriptionRight{
                    position:absolute;
                    top:0.2rem;
                    right:0.2rem;
                    height:1.5rem;                    
                }
            }
            #leftSideWrapper{
                border:solid 0px orangered;  
                height:calc(100vh - 108px);
                overflow-x:hidden;
                overflow-y:auto;
                border-top:solid 1px rgb(0 0 0/30%);
                div#leftSideWrapperBody{
                    display:table;
                    table-layout:fixed;
                    width:100%;  
                    font-size:1rem;
                    .job{
                        position:relative;
                        display:table-row;
                        height:40px;
                        cursor:pointer;
                        &.sltJob{
                            background-color:white; 
                        }
                        &.OO, &.RT{ 
                            &:hover{ background-color:rgb(0 0 0/5%) }
                        }
                        // &.X{ 
                        //     background-color:rgb(0 0 0/10%); 
                        //     &:hover{ background-color:rgb(0 0 0/15%) }
                        // }
                        &.RS, &.X{ 
                            background-color:rgb(0 0 0/30%);
                            &:hover{ background-color:rgb(0 0 0/40%) }
                            color:rgb(0 0 0/40%);
                        }
                        &.ERA{ 
                            background-color:rgb(128 0 0/100%);//maroon; 
                            &:hover{ background-color:rgb(90 0 0/100%) }
                            animation: errorAni 1.5s linear infinite; //app.scss 참조
                            -webkit-animation: errorAni 1.5s linear infinite;
                        }
                        &.ERC{ 
                            background-color:rgb(0 128 0/100%);//green; 
                            &:hover{ background-color:rgb(0 90 0/100%) }
                            animation: errorAni 1.5s linear infinite; //app.scss 참조
                            -webkit-animation: errorAni 1.5s linear infinite;
                        }
                        &.ERX{
                            background-color:rgb(250 190 200/100%);//pink; 
                            &:hover{ background-color:rgb(240 160 170/100%) }
                            animation: errorAni 1.5s linear infinite; //app.scss 참조
                            -webkit-animation: errorAni 1.5s linear infinite;
                        }
                        &.RQ, &.ST, &.RV{
                            background-color:rgb(0 0 140/100%);//darkblue;
                            &:hover{ background-color:rgb(0 0 90/100%) }
                            color:cyan;
                        }
                        >span{
                            display:table-cell;
                            border:solid 0px gold;
                            border-bottom:solid 1px rgb(0 0 0/20%);
                            vertical-align:middle;
                            white-space:nowrap;
                            &.status{
                                display:table-cell;
                                width:30px;
                                padding-top:6px;
                                text-align:center;
                            }
                            &.jobTitle{
                                //border:solid 1px green;
                                display:table-cell;
                                overflow:hidden;                            
                                text-overflow:ellipsis;
                                white-space:nowrap;
                                line-height:1rem;
                                span.time{
                                    display:inline-block;
                                    float:right;
                                    position:relative;
                                    font-size:1rem;
                                    font-weight:600;
                                    top:0.1rem;
                                    // line-height:1.2rem;   
                                    // border:solid 1px red;                                
                                }
                            }
                            &.jobWorking{
                                display:table-cell;
                                width:13px;
                                text-align:right;
                                padding:4px 5px 0 5px;
                                span{
                                    position:relative;
                                    top:1px;
                                    animation: selectedJobTranIcon 1s linear infinite;
                                }
                            }
                        }
                        &:hover{
                            background-color:rgb(0 0 0/10%);
                        }
                    }
                }
            }
        }
        //우측 상세 및 PROC 리스트
        &>div#rightSide{
            display:table-cell;
            vertical-align:top;
            width:inherit;
            border:solid 0px blue;
            overflow-y:auto;
            //overflow-x:auto;
            // white-space:nowrap;
            padding:0;
            div.labelWrapper{
                //border-bottom:solid 1px #ccc;
                table{
                    width:100%;
                    border:solid 1px #ccc;
                    border-top:0;
                    //border-left:0;
                    //border-right:0;
                    border-collapse:collapse;
                    font-size:0.9rem;
                    line-height:1.5rem;
                    th{ 
                        background-color:#EFEFEF;
                        font-weight:normal;
                    }
                    td{
                        background-color:white;
                        padding: 0.3rem 0.5rem;
                        &.elapsed{
                            font-weight:bold;
                            text-align:center;
                        }
                    }
                    span.jobcode{
                        float:right;
                        font-weight:bold;
                        font-size:0.9rem;
                        color:var(--link);
                    }
                    div.jobname{                        
                        font-size:1.2rem;
                        font-weight:600;
                    }
                    div.divButtons{
                        margin:0.5rem 0;
                    }
                    button.green{
                        background-color:green;
                        &:hover{ background-color:rgb(0 90 0/100%) }
                        &:active{ background-color:green }
                    }
                    button.maroon{
                        background-color:maroon;
                        &:hover{ background-color:rgb(90 0 0/100%) }
                        &:active{ background-color:maroon }
                    }
                }
            }
        }
    }
    //leftSide, rightSide 간격조정선
    div#gap{
        border-left: solid 1px rgb(0 0 0/20%);
        position:absolute;
        top:33px;
        width:10px;
        height:100vh;
        z-index:50;
        &:active{
            border-left: solid 4px rgb(0 0 0/50%);
        }
    }

    //탭매뉴
    ul {
        display:flex;  
        flex-direction:row;
        align-items:flex-end;
        margin:0;
        width:calc(100% + 2px);
        height:30px;
        padding: 0.25rem 0 0 0;
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
            border: solid 1px rgb(0 0 0/15%);
            border-left:solid 1px rgb(0 0 0/15%);
            border-right:0;
            border-bottom:solid 1px rgb(0 0 0/50%);
            padding: 0.2rem 1.2rem;
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

            span.working{
                color:blue;
                line-height:0.5rem;
                margin-right:0.2rem;
            }
        }

    }
}


</style>