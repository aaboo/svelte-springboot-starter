<!-- 
jobstatus {
X:기본상태
, Q:실행대기
, C:완료
, N:미실행(당일제외)
, H:수작업
, E:에러
, R:진행중
}
procstatus {
C:완료
, X(기본)/H(수작업)/Q(실행대기):대기중
, R:작업중
, E:에러
, S:스킵
, N:미실행(당일제외)
}
errflag {
X: 기본상태
, E: 에러
, Q: 실행대기
, R: 실행중
, N: 미실행
}
-->
<script>
const ID = "jobProc";

import { onMount } from 'svelte';
import { menuStore, gapLeftStore, loginTimerStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';
import icon from '$lib/icon';
import PopupJobDetail from './PopupJobDetail.svelte';
import PopupProcDetail from './PopupProcDetail.svelte';

//조회 - Frame.svelte.searchFromChildSearch(ev)
export function search(param){
    NV.param = param;
    NV.action.selectJob(param);
}

onMount(()=>{
    window.NV = NV;
    NV.init();
    return ()=> NV.socket.close();
});
    
//좌측 메뉴 넓이 조정 결과
$:{
    $gapLeftStore;
    gap.orgX;
    onResize();
}

//로그인 타이머 소켓 적용
$:if($menuStore.currentMenuCID==ID){
    const today = (new Date()).print("yyyymmdd");
    if($loginTimerStore==0) NV.socket.close();
    else {
        if(NV.socket.target!=null &&NV.param?.workdate!=today) NV.socket.close();
        else if(NV.socket.target==null && NV.param?.workdate==today) NV.socket.connect();
    }
}else{
    if(NV.socket.target!=null) NV.socket.close();
}

const NV = {
    init: function(){
        const ds = this;
        this.tab.init();
        this.grid.init();        
        //ds.action.selectJob(app.ss.getSearch()); //$:에서 최초 실행되어짐
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
        , sltJobIndex: (app.ss.getData()?.sltJobIndex??"0") //현재 선택된 JOB의 왼쪽리스트 기준 index
        , sltJobcode:(app.ss.getData()?.sltJobcode??"") //현재 선택된 JOBCODE
        , sltJobgroup: (app.ss.getData()?.sltJobgroup??"") //현재 선택된 탭(Jobgroup)
        , sltProc:{} //현재 선택된 Proccode 상세 정보
        , sltProccode:"" //현재 선택된 Proccode        
        , runningGroupList:[] //현재 진행중인 그룹리스트
        , hideN: (app.ss.getData()?.hideN??false) //true: 미실행(N) 보이기, false: 미실행(N) 숨기기
        , hideC: (app.ss.getData()?.hideC??false) //true: 완료(C) 보이기, false: 완료(C)) 숨기기
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
            NV.data.hideN = false;
            NV.data.hideC = false;
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
        , isFocusRunningAlways: false //진행중인 jobcode로 계속 포커싱
        , isJobFound: false //현재 선택된 jobcode로 포커싱할 때, 포커싱 후 CSS 에니메이션 표현
    }
}
NV.socket = {
    target: null
    , connect: function(){
        const ds = this;
        if(ds.target==null && NV.param.workdate && NV.param.workdate==(new Date()).print("yyyymmdd")){
            //console.log("socket connect - jobProc",  NV.param.workdate, (new Date()).print("yyyymmdd"));
            //서버 소켓 연결
            ds.target = new WebSocket(app.WEBSOCKET+'/jobproc');
            ds.target.onmessage = (msg)=>{
                //console.log("from server: ", msg.data);
                ds.action.messageRun(msg.data);
            }
            //window.sk = ds.socket;
        }
    }
    , close: function(){
        try{
            //console.log("socket close - jobProc");
            this.target.close();
            this.target=null;
        }catch(e){}
    }
    , action: {
        //현재 진행중인 JOB, PROC 정리
        messageRun: function(_running){
            //console.log(_running);
            const data = NV.data;
            const running = JSON.parse(_running);
            //데이터 세팅
            NV.data.org.jobList = [...running.jobList];
            NV.data.org.jobProcListObj = {...running.jobProcListObj};
            NV.data.set({jobList:NV.data.org.jobList, jobProcListObj:NV.data.org.jobProcListObj}); //NV.tab.action.tabRun(); 실행
            //기존 탭 유지
            NV.tab.action.select(NV.data.sltJobgroup); //현재 탭기준 jobList 재설정 //NV.action.moveToLastJob()가 여기 있음
            if(!NV.data.isFocusRunningAlways){
                NV.data.jobProcListObj[NV.data.sltJobcode] = NV.data.org.jobProcListObj[NV.data.sltJobcode];
                NV.action.selectJobDetail();
                if(NV.popup.display.procDetail) NV.action.selectPopupProcDetail();
            }else{
                NV.action.moveToLastJob();
            }
        }
    }
}
NV.action = {
    //조회
    selectJob: function(param){
        const ds = this;
        //console.log(param);
        if(!param || JSON.stringify(param)=="{}") return;
        NV.param = param; //NV.socket.connect에서 사용
        app.modal.open();
        NV.socket.close();
        app.fetch("/jobproc/selectJob", {...param})
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
                //NV.data.isFocusRunningAlways = false;
                NV.tab.action.select();
            }else{                
                NV.data.clear();
                NV.grid.target.resetData([]);
                app.modal.close();   
                alert("자료가 존재하지 않습니다.");
            }
            NV.action.selectJobDetail();
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
        //실행추적이 ON 상태일때 다른 JOB을 선택했을 경우, 자동으로 OFF 해주는 기능
        if(NV.data.isFocusRunningAlways==true && _jobcode!=undefined && _jobcode!=NV.data.sltJobcode){
            NV.data.isFocusRunningAlways = false;
        }
        const jobcode = _jobcode??NV.data.sltJobcode??"";
        //const jobcode = _jobcode??"";
        if(jobcode==""){
            NV.grid.target?.resetData([]);
            NV.data.btnStatus = {...NV.btnCtrl.get()};
        }else{
            NV.data.sltJobcode = jobcode;
            NV.data.sltJob = NV.data.jobObj[NV.data.sltJobcode];
            NV.data.sltJobIndex = NV.data.sltJob.index;
            //app.ss.setData({sltJobIndex: NV.data.sltJobIndex});

            //그리드 렌더링 + 작업중인 포스트 row 찾기
            NV.grid.focusRowKey = 0;
            NV.data.org.jobProcListObj[jobcode].map((item, index)=>{
                //포커스 대상 Row 찾기
                if(
                    NV.grid.isPopupOpen==false //프로세스 팝업이 없을 경우
                    && ["X","N"].indexOf(NV.data.sltJob.jobstatus)<0 //JOB: 대기(X),미실행(N) 제외
                    && ["X","N","Q","H"].indexOf(item.procstatus) < 0 //PROC: 대기(X),미실행(N),실행준비(Q),수작업(H) 제외
                    && !(NV.data.sltJob.jobstatus=="C" && NV.data.sltJob.jobchk=="O") //JOB: 완료(C+jobchk:O) 제외
                ){
                    NV.grid.focusRowKey = index;
                }
                NV.grid.addBgClass(item);
            });
            //그리드 렌더링시 포커스 자동조정
            {
                const orgList = NV.grid.target.getData(); //기존 Grid 자료
                const resList = [...NV.data.org.jobProcListObj[jobcode]]; //신규 DB 자료
                //기존 자료와 다를 경우 새로 resetData
                if(orgList.length!=resList.length){
                    NV.grid.target?.resetData(resList);
                    //자동 포커스
                    if(NV.grid.focusRowKey > 0){
                        try{
                            const rowKeyTest = Math.min(resList.length-1, NV.grid.focusRowKey+3);
                            NV.grid.target?.focus(rowKeyTest, "procname", true);
                            //NV.grid.target?.focus(NV.grid.focusRowKey, "procname", true);
                        }catch(e){}
                    }
                }
                //기존자료와 같을 경우 변경된 row만 setRow
                else{
                    for(let i=0, il=resList.length; i<il; i++){
                        if(JSON.stringify(orgList[i])!=JSON.stringify(resList[i])){
                            NV.grid.target.setRow(i, resList[i]);
                        }
                    }
                }
            }
            //console.log("NV.btnCtrl.get()", NV.btnCtrl.get());
            //버튼 모양 변화
            NV.data.btnStatus = {...NV.btnCtrl.get()};
        }
        NV.action.saveSessionStorageData();//스토리지 저장
        app.modal.close();
    }
    //JOB 상세팝업
    , selectPopupJobDetail: async function(ev, _jobcode){
        const jobcode = _jobcode??NV.data.sltJobcode;
        if(!jobcode) return;
        const pd = NV.popup.data;
        pd.title = `배치업무 상세정보(${jobcode})`;
        pd.showFooter = true;
        //pd.bodyHeight = NV.popup.height - 50 - (pd.showFooter?70:0) - 0;
        //-- 이 단계를 거쳐야 grid가 refresh 됨
        await new Promise((next)=>{
            NV.popup.display.jobDetail = true;
            next();                
        });
        //setTimeout(()=>{},1000);
    }
    //PROC 상세팝업
    , selectPopupProcDetail: async function(_proccode){
        const proccode = _proccode??NV.data.sltProccode;
        if(!proccode) return;
        const pd = NV.popup.data;
        NV.data.sltProc = {...NV.data.procObj[proccode]};
        NV.data.sltProccode = proccode;
        //console.log(NV.data.sltProc);            
        //app.modal.open();//모달
        //console.log(response);
        pd.title = `프로세스 상세정보(${proccode})`;
        pd.showFooter = true;
        pd.bodyHeight = NV.popup.height - 50 - (pd.showFooter?70:0) - 0;
        //-- 이 단계를 거쳐야 grid가 refresh 됨
        await new Promise((next)=>{
            NV.popup.display.procDetail = true;
            next();                
        });
        //setTimeout(()=>{},1000);
    }
    //미실행, 완료 숨기기 필터
    , filterHide: function(_jobList){
        let filteredJobList = _jobList?_jobList:NV.data.org.jobList;
        if(NV.data.hideN==true){
            filteredJobList = filteredJobList.filter((item)=>{
                return !(item.jobstatus=="N");
            })
        }
        if(NV.data.hideC==true){
            filteredJobList = filteredJobList.filter((item)=>{
                return !(item.jobstatus=="C" && item.jobchk=="O");
            })
        }
        //선택용 index 재설정
        filteredJobList.map((item, index)=>{
            item.index = index;
        });
        return filteredJobList;
    }
    //미실행 숨기기
    , hideN: async function(){
        NV.data.hideN = !NV.data.hideN;
        NV.tab.action.select();
        //NV.action.moveToLastJob();
    }
    //완료 숨기기
    , hideC: async function(){
        NV.data.hideC = !NV.data.hideC;
        NV.tab.action.select();
        NV.action.moveToLastJob();
    }
    //스토리지 저장(탭선택 또는 job리스트 선택시 발생)
    , saveSessionStorageData: function(){
        app.ss.setData({
            ...app.ss.getData()
            , ...{
                sltJobgroup: NV.data.sltJobgroup
                , sltJobcode: NV.data.sltJobcode
                , sltJobIndex: NV.data.sltJobIndex
                , hideN: NV.data.hideN
                , hideC: NV.data.hideC
            }
        });
    }
    //포커스할 JOB찾기        
    /* JOB 선택 로직
        실행추적이 OFF상태이고
            화면에 기존 JOB이 보이면 기존 JOB을 반환하고 끝낸다
        
        진행중(Q,R)인 JOB을 찾아서 
            존재하면 그 중에서 가장 마지막에 작업한 JOB을 선택한다
            존지해지 않으면
                대기(Q),에러(E),완료(C)된 것 중 크론탭을 제외한 JOB을 찾아서
                    존재하면 그 중에서 가장 마지막에 완료된 것을 반환하고
                    존재하지 않으면 목록 중에서 첫번째 JOB을 반환한다.
    */
    , getJobToFocus: function(){
        const ds = this;
        let jobFinder=[]
            , focusTarget=null;
        //실행추적이 OFF상태이고
        if(NV.data.isFocusRunningAlways==false){            
            //화면에 기존 JOB이 보이면
            jobFinder = NV.data.jobList.filter(item=>{
                return item.jobcode == NV.data.sltJob?.jobcode;
            })
            //기존 JOB을 반환한다.
            if(jobFinder.length>0) return NV.data.sltJob;
        }
            

        //진행중(Q,R)인 JOB을 찾아서
        jobFinder = NV.data.jobList.filter((item, index)=>{
                return ["Q","R"].indexOf(item.jobstatus)>-1
            });        
        //존재하면
        if(jobFinder.length>0){
            //그 중에서 가장 마지막에 작업한 JOB을 선택한다
            focusTarget = jobFinder.reduce((prev, curr)=>{
                return curr.jobendtime > prev.jobendtime ? curr : prev;
            });            
        }
        //존재하지 않으면
        else{
            //대기(Q), 에러(E), 완료(C)된 것 중 크론탭을 제외한 JOB을 찾아서
            jobFinder = NV.data.jobList.filter((item, index)=>{
                return item.jobchk=="O" || item.jobstatus=="E" || (item.jobstatus=="C" && item.jobname.indexOf("크론탭")<0);
            });
            //존재하면
            if(jobFinder.length>0){
                //그 중에서 가장 마지막에 완료된 것을 반환하고
                focusTarget = jobFinder.reduce((prev, curr)=>{
                    return curr.jobendtime > prev.jobendtime? curr : prev;
                });
            }
            //존재하지 않으면
            else{
                //목록 중에서 첫번째 JOB을 반환한다.
                focusTarget = NV.data.jobList[0];
            }
        }
        return focusTarget;
    }
    //선택된 JOB으로 포커스 이동
    , focusToJob: function(focusTarget=null){
        if(focusTarget!=null){
            const scrollTo = {
                behavior:"smooth"
                , left: 0
                , top: (NV.dom.jobList[0]?.clientHeight??0)*(focusTarget.index)
                    - (NV.dom.leftSideWrapper.clientHeight/2.5)
            };
            //console.log(NV.data.sltJobIndex, NV.data.sltJob, scrollTo);
            NV.dom.leftSideWrapper?.scrollTo(scrollTo);
        }
    }
    //LeftList 현재 선택된 JOB으로 포커싱
    , moveToLastJob: function(){
        const ds = this;               
        
        const focusTarget = ds.getJobToFocus();//포커스할 JOB찾기
        NV.data.sltJob = focusTarget;
        NV.data.sltJobcode = NV.data.sltJob?.jobcode??"";
        NV.data.sltJobIndex = NV.data.sltJob?.index??"";        
        this.saveSessionStorageData();//스토리지 저장
        NV.action.selectJobDetail();//상세화면 업데이트
        ds.focusToJob(NV.data.sltJob);//선택된 JOB으로 포커스 이동
    }
    //LeftList 최상단으로 포커싱
    , focusTop: function(){
        const scrollTo = {
            behavior:"smooth"
            , left:0
            , top:0
        };
        NV.dom.leftSideWrapper.scrollTo(scrollTo);
    }
    //LeftList 최하단으로 포커싱
    , focusBottom: function(){
        const scrollTo = {
            behavior:"smooth"
            , left:0
            , top:(NV.dom.jobList[0]?.clientHeight??0)*NV.data.jobList.length
        };
        NV.dom.leftSideWrapper.scrollTo(scrollTo);
    }   
    //JOB 작업실행
    , btnJobProcQ: function(ev){
        ev.preventDefault();
        const job = NV.data.sltJob;
        //console.log(job, NV.data.jobProcListObj[job.jobcode]);
        const testProcList = NV.data.jobProcListObj[job.jobcode].filter(item=>item.procstatus=="X");
        if(testProcList.length>0){
            if(!confirm("작업을 실행하시겠습니까?")) return false;
            const param = {
                workdate: job.workdate
                , jobcode: job.jobcode
            }
            app.modal.open({message:"작업 실행 중"});
            app.fetch("/jobproc/saveJobProcQ", param).then(async (response)=>{
                //console.log(response);
                if(response.data>0){
                    //NV.action.selectJob({...NV.param});
                    NV.data.sltJob.jobstatus="Q";
                    NV.data.sltJob.jobchk="X";
                }else alert(response.message);
            });
        }else{
            alert("수행할 수 있는 프로세스(X)가 없습니다.");
        }
    }
    //JOB 결과확인
    , btnJobProcResultConfirm: function(ev){
        ev.preventDefault();
        const job = NV.data.sltJob;
        //console.log(job, NV.data.jobProcListObj[job.jobcode]);    
        const procList = [...NV.data.jobProcListObj[job.jobcode]];
        const testProcList = procList.filter(item=>
            ["C","H","N"].indexOf(item.procstatus)>-1
        );
        if(testProcList.length != procList.length){
            alert("프로세스가 아직 완료(C)되지 않았습니다.");
            return;
        }
        if(app.alertFocus(NV.dom.workmemo.value=="", "결과확인 코멘트를 적어주세요", NV.dom.workmemo)) return;
        app.modal.open();
        app.fetch("/jobproc/saveJobProcResultConfirm", job).then(async (response)=>{
            //console.log(response);
            if(response.data>0){
                NV.data.sltJob.jobstatus="C";
                NV.data.sltJob.jobchk="O";
                alert("결과확인 되었습니다.");                
                app.modal.close();
            }else alert(response.message);
        }).catch((e)=>{
            console.log(e);
            app.modal.close();
        });
    }
    //작업스킵
    , btnJobProcSkip: function(ev){
        ev.preventDefault();
        const job = NV.data.sltJob;
        const procList = [...NV.data.jobProcListObj[job.jobcode]];
        let testProcList = []
        testProcList = procList.filter(item=>["E","S","X","Q","R","H"].indexOf(item.procstatus)>-1);
        if(testProcList.length>0){
            if(!confirm("잠깐! 개발팀과 상의하셨나요?"
                +"\n\n미처리된 프로세스가 모두 스킵처리됩니다."
                +"\n프로세스 개별 스킵은 프로세스 상세창을 이용해 주세요."
                +"\n\n스킵처리 하시겠습니까?")
            ) return;
        }else{
            testProcList = procList.filter(item=>["N"].indexOf(item.procstatus)>-1);
            if(procList.length!=testProcList.length){
                alert("스킵처리 대상이 아닙니다.");
                return;
            }
        }
        if(app.alertFocus(NV.dom.workmemo.value=="", "작업스킵 코멘트를 적어주세요", NV.dom.workmemo)) return;
        app.modal.open();
        app.fetch("/jobproc/saveJobProcSkip", job).then(async (response)=>{
            //console.log(response);
            if(response.data>0){
                NV.data.sltJob.jobstatus="S";
                NV.data.sltJob.jobchk="O";                
                alert("스킵처리 되었습니다.");                
                app.modal.close();
            }else alert(response.message);
        }).catch((e)=>{     
            console.log(e);        
            app.modal.close();
        });
    }
}
//탭메뉴
NV.tab = {
    target: null
    , list: [
        {jobgroup:"", jobgroupName:"전체", dom:null}
        , {jobgroup:"PS", jobgroupName:"매입요청", dom:null}
        , {jobgroup:"PR", jobgroupName:"매입결과", dom:null}
        , {jobgroup:"DS", jobgroupName:"DB조회", dom:null}
        , {jobgroup:"DR", jobgroupName:"DB반영", dom:null}
        , {jobgroup:"ET", jobgroupName:"기타배치", dom:null}
    ]
    , obj:{}
    , init: ()=>{
        const ds = NV.tab;
        NV.tab.list.map((item, index)=>{
            NV.tab.obj[item.jobgroup] = item;
        });
    }
    , action: {
        select: async (_jobgroup=NV.data.sltJobgroup)=>{
            //console.log(_jobgroup);
            const ds = NV.tab;
            NV.data.sltJobgroup = _jobgroup;

            if(NV.data.org.jobList.length==0) return;
            //왼쪽 리스트 업데이트
            await new Promise(async (next)=>{
                if(NV.data.sltJobgroup==""){//전체
                    NV.data.jobList = await NV.action.filterHide([...NV.data.org.jobList]);
                }else{//탭별 리스트 필터                
                    NV.data.jobList = await NV.action.filterHide(
                        NV.data.org.jobList.filter((item)=>item.jobgroup == NV.data.sltJobgroup)
                    );
                }
                next();
            });                
            //setTimeout(()=>{
                NV.action.moveToLastJob();
            //}, 0);
            
            NV.action.saveSessionStorageData();//스토리지 data 저장
            ds.action.tabRun(); //실행중인 탭 표시
        }
        //실행중인 탭 표시
        , tabRun: ()=>{
            NV.data.runningGroupList = [];
            NV.data.org.jobList.map((item, index)=>{
                if(item.jobstatus=="R"){//R:실행중
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
//상세그리드
NV.grid = {
    target: null
    , isPopupOpen:false//현재 팝업 오픈 여부 - {true: 팝업열림, false:팝업닫힘}
    , focusRowKey:0//isPopupOpen==false 일 때 자동 focus
    , init: async function(){
        //console.log("NV.popup.data.bodyHeight", NV.popup.data.bodyHeight - 81);
        //const w = window.innerWidth - $gapLeftStore - gap.orgX - 2;
        const option = {
            el: NV.grid.target
            , scrollX: true
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore - gap.orgX
            , bodyHeight: window.innerHeight - NV.dom.procInfo.clientHeight - 80
            , columns: [
                {header:'수신상태', name:'filestatusnm', width:90, align:"center", formatter:(item)=>{
                    if(item.row.filestatus=="A"){//대상아님(-)
                        return item.value;
                    }else if(item.row.filestatus=="B"){//수신대기
                        return item.value;
                    }if(item.row.filestatus=="C"){//수신완료
                        return `<span style="color:seagreen;margin-right:4px;">${icon.check(1)}</span> ${item.value}`;
                    }if(item.row.filestatus=="D"){//중복수신
                        return `<span style="margin-right:4px;">${icon.warn(1.1)}</span> ${item.value}`;
                    }else return item.value;
                }}
                , {header:'NO', name:'no', align:"center", width:40, align:"center"}
                /* , {header:'PROCCODE', name:'proccode', width:100, align:"center"} */
                , {header:'프로세스명', name:'procname', align:"left", formatter:(item)=>{
                    return `<span title="${item.row.proccode}">${item.value}</span>`
                }}
                , {header:'실행결과', name:'procstatusnm', align:"center", width:90, formatter:(item)=>{
                    //console.log(item);
                    if(item.row.procstatus=="C"){//정상
                        if(item.row.errflag=="X"){//스킵처리
                            return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span> ${item.value}`;
                        }else if(item.row.errflag=="E"){//오류스킵
                            return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span> ${item.value}`;
                        }else if(item.row.errflag=="Q"){//대기중스킵
                            return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span> ${item.value}`;
                        }else if(item.row.errflag=="R"){//처리중스킵
                            return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span> ${item.value}`;
                        }else if(item.row.errflag=="N"){//자료없음
                            return `<span style="color:seagreen;margin-right:4px;">${icon.warn(1.1)}</span> ${item.value}`;
                        }else if(item.row.errflag=="H"){//수작업완료
                            return `<span style="color:seagreen;margin-right:4px;">${icon.check(1)}</span> ${item.value}`;
                        }else{
                            return `<span style="color:seagreen;margin-right:4px;">${icon.check(1)}</span> ${item.value}`;
                        }
                    }else if(item.row.procstatus=="X"){// -
                        return item.value;
                    }else if(item.row.procstatus=="H"){//수작업
                        return item.value;
                    }else if(item.row.procstatus=="Q"){//대기중
                        return `<span style="margin-right:4px;">${icon.pin(1)}</span> ${item.value}`;
                    }else if(item.row.procstatus=="R"){//처리중
                        return `<span style="color:rgb(0 0 140);margin-right:4px;">${icon.working()}</span> ${item.value}`;
                    }else if(item.row.procstatus=="E"){//처리오류
                        return `<span style="margin-right:4px;">${icon.warn(1.1)}</span> ${item.value}`;
                    }else if(item.row.procstatus=="S"){//작업스킵
                        return `<span style="color:maroon;margin-right:4px;">${icon.cross(0.8)}</span> ${item.value}`;
                    }else if(item.row.procstatus=="N"){//미실행
                        return `<span style="margin-right:4px;">${icon.notAllowed(0.8)}</span> ${item.value}`;
                    }else{
                        return item.value
                    }
                }}   
                , {header:'오류 조치', name:'notice', width:80, align:"center"}
                , {header:'작업자 메모', name:'procmemo', width:80, align:"center"}                
                , {header:'시작시간', name:'starttime', align:"center", width:75}
                , {header:'종료시간', name:'endtime', align:"center", width:75}
                , {header:'건수', name:'resultcnt', align:"right", width:80, formatter:(item)=>{                    
                    return (item.value??"0").money();
                }}
            ]
        }
        this.target = tuiGrid.make(option);
        this.bindEvent();
    }
    , bindEvent: function(){
        const ds = this;
        const grid = ds.target;
        grid.on("click", (item)=>{
            //console.log(item);
            if(item.rowKey==undefined) return;
            const rowKey = item.rowKey;
            const rowData = grid.getRow(rowKey);
            NV.grid.isPopupOpen = true; //그리드 자동 focus에 필요함
            NV.action.selectPopupProcDetail(rowData.proccode);
        });
    }
    , addBgClass: (item)=>{        
        if(item.procstatus=="C"){
            if(["X","E","Q","R","N"].indexOf(item.errflag)>0){
                item["_attributes"] = {className:{row:["skip"]}}
            }else if(["H"].indexOf(item.errflag)>0){
                //item["_attributes"] = {className:{row:["skip"]}}
            }else{
                //item["_attributes"] = {className:{row:["complete"]}}
            }
        }else if(["X","H","Q"].indexOf(item.procstatus)>0){
            item["_attributes"]= {className:{row:["waiting"]}}
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
//팝업
NV.popup = {
    display: {
        jobDetail:false
        , procDetail:false
    }
    , data: {
        title:""
        , width:800
    }
    , action: {
        //스킵처리(C-기존 procstatus)
        btnProcC: function(ev){
            ev.preventDefault();
            const param = {...NV.data.sltProc};
            //console.log("btnProcC", param.comment, param);
            if(["C","E","N"].indexOf(param.procstatus)<0){
                if(app.alertFocus(!param.comment, "코멘트를 작성해 주세요", NV.dom.procPopupComment)) return;
                app.modal.open();
                app.fetch("/jobproc/updateProcC", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){
                        alert("스킵 처리 되었습니다.");
                        NV.popup.display.procDetail = false;
                    }else alert("스킵처리에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
                alert("스킵대상이 아닙니다.(실행상태[X,Q,R]만 허용)");
            }
        }
        //자료없음(C-N)
        , btnProcN: function(ev){
            ev.preventDefault();
            const param = {...NV.data.sltProc};
            //console.log("btnProcN", NV.data.sltProc);
            if(NV.data.sltProc.procstatus=="E"){
                if(app.alertFocus(!param.comment, "코멘트를 작성해 주세요", NV.dom.procPopupComment)) return;
                app.modal.open();
                app.fetch("/jobproc/updateProcN", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){
                        alert("자료없음 처리 되었습니다.");
                        NV.popup.display.procDetail = false;
                    }else alert("자료없음 처리에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
                alert("자료없음 처리를 할 수 없습니다.(실행상태[E]만 허용)");
            }
        }
        //재실행
        , btnProcQ: function(ev){
            ev.preventDefault();
            const param = {...NV.data.sltProc};
            //console.log("btnProcQ", NV.data.sltProc, param.procstatus=="C" && param.reworkflag=="O");
            if(param.procstatus=="C" && param.reworkflag=='O'){
                if(!confirm("재실행 하시겠습니까?")) return;
                app.modal.open();
                app.fetch("/jobproc/insertProcQ", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){
                        alert("재실행 처리 되었습니다.");
                        NV.popup.display.procDetail = false;
                    }else alert("재실행에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
                alert("재실행 대상이 아닙니다.\n(실행상태[C]+재작업가능여부[O]만 허용)");
            }
        }
    }
}

//버튼 상태 변화 값
NV.btnCtrl = {
    status: {
        //실행배치 영역
        w1:{
            dft: '작업실행'
            , work: `${icon.working(1)}&nbsp;작업 실행 중`
            , done: `${icon.check(1)}&nbsp;작업실행 완료`
        }
        , w2:{
            dft: '결과확인'
            , work: `${icon.working(1)}&nbsp;결과 확인 중`
            , done: `${icon.check(1)}&nbsp;결과확인 완료`
        }
        , w3:{
            dft: '전체 작업스킵'
            , work: `${icon.working(1)}&nbsp;전체 작업 스킵처리 중`
            , done: `${icon.check(1)}&nbsp;전체 작업스킵 완료`
        }
    }
    //버튼 상태 변화 계산
    , get: function(ev){
        const b = NV.btnCtrl.status;
        const result = {
            w1:{text:b.w1.dft, disabled:true}
            , w2:{text:b.w2.dft, disabled:true}
            , w3:{text:b.w3.dft, disabled:true}
        };
        if(NV.data.sltJob?.workdate<=(new Date()).print("yyyymmdd")){//오늘보다 이전이면
            //선행프로세스 확인
            let bfrJobFiltered = [];
            for(let i=0, il=NV.data.sltJobIndex; i<il; i++){ //NV.action.selectJobDetail에서 sltJobIndex 생성
                const item = NV.data.jobList[i];
                if(item?.jobgroup==NV.data.sltJob?.jobgroup){
                    if(item.jobstatus!="N"){
                        bfrJobFiltered = [...bfrJobFiltered, item];
                    }
                }
            }
            //console.log(bfrJobFiltered);
            const bfrJob = bfrJobFiltered[bfrJobFiltered.length-1]??NV.data.sltJob;
            //console.log("bfrJob", NV.data.sltJobgroup, NV.data.sltJobIndex, bfrJob);
            //선행프로세스 결과확인 완료된 경우, 또는 선행프로세스 없을 경우<첫프로세스>[""]
            if(bfrJob.jobchk=="O" || NV.data.sltJobIndex==0 || bfrJob.jobcode==NV.data.sltJobcode){
                if(NV.data.sltJob.jobstatus=="H"){//수작업
                    result["w1"] = {text:b.w1.dft, disabled:true};//작업실행
                    result["w2"] = {text:b.w2.dft, disabled:false};//결과확인
                    result["w3"] = {text:b.w3.dft, disabled:true};//작업스킵
                }else if(NV.data.sltJob.jobstatus=="X"){//대기중
                    result["w1"] = {text:b.w1.dft, disabled:false};
                    result["w2"] = {text:b.w2.dft, disabled:true};
                    result["w3"] = {text:b.w3.dft, disabled:false};
                }else if(["Q","R"].indexOf(NV.data.sltJob.jobstatus)>-1){//요청(Q), 실행중(R)
                    result["w1"] = {text:b.w1.work, disabled:true};
                    result["w2"] = {text:b.w2.dft, disabled:true};
                    result["w3"] = {text:b.w3.dft, disabled:false};
                }else if(NV.data.sltJob.jobstatus=="N"){//미실행
                    result["w1"] = {text:b.w1.dft, disabled:true};
                    result["w2"] = {text:b.w2.dft, disabled:true};
                    result["w3"] = {text:b.w3.dft, disabled:false};
                }else if(NV.data.sltJob.jobstatus=="S"){//작업스킵
                    result["w1"] = {text:b.w1.dft, disabled:false};
                    result["w2"] = {text:b.w2.dft, disabled:true};
                    result["w3"] = {text:b.w3.done, disabled:true};
                }else if(NV.data.sltJob.jobstatus=="E"){//처리오류
                    result["w1"] = {text:b.w1.dft, disabled:true};
                    if(NV.data.sltJob.jobchk=="O"){
                        result["w2"] = {text:b.w2.done, disabled:true};
                    }else{
                        result["w2"] = {text:b.w2.dft, disabled:false};
                    }
                    result["w3"] = {text:b.w3.done, disabled:false};
                }else if(NV.data.sltJob.jobstatus=="C"){//작업완료
                    result["w1"] = {text:b.w1.done, disabled:true};
                    if(NV.data.sltJob.jobchk=="O"){
                        result["w2"] = {text:b.w2.done, disabled:true};
                    }else{
                        result["w2"] = {text:b.w2.dft, disabled:false};
                    }
                    result["w3"] = {text:b.w3.dft, disabled:true};
                }
            }
        }
        return result;
    }
}

//좌우 넓이 조정
const gap = {
    status: "none"
    , dom: null
    , minX: 50
    , orgX: (window.innerWidth - $gapLeftStore) / 2//700
    , movX: (window.innerWidth - $gapLeftStore) / 2//700
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
        //NV.grid.target.setWidth(Math.max(1070+47, window.innerWidth - $gapLeftStore - gap.orgX - 2));
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

<jobProc bind:this={NV.dom.root}>
    <!-- Tab 메뉴 영역 -->
    <ul bind:this={NV.tab.target}                
        on:selectstart|preventDefault|stopPropagation
        on:mousemove|preventDefault|stopPropagation
    >
        {#each NV.tab.list as item, index (item.jobgroup) }
            <li title={item.jobgroup} class:on={NV.data.sltJobgroup==item.jobgroup}>
                <div bind:this={item.dom}
                    class="tabItemTitle"
                    on:click={()=>{NV.tab.action.select(item.jobgroup);}}
                    on:keyup
                    role="button" tabindex={index}
                >
                    {#if NV.data.runningGroupList.indexOf(item.jobgroup)>-1}
                    <span class="working">{@html icon.working(0.8)}</span>
                    {/if}
                    {item.jobgroupName}
                <div>
            </li>
        {/each}
    </ul>
    
    <div id="content">
        <!-- 왼쪽 영역 -->
        <div id="leftSide" bind:this={NV.dom.lside} style="width:{gap.orgX}px">
            <div id="leftSideDesription">            
                <div id="leftSideDesriptionLeft">
                    <span id="leftSideDescipriptionLeftListCount">목록개수: {NV.data.jobList.length}</span>
                    <label title="미실행(N) 숨기기" style="cursor:pointer">
                        <input type="checkbox" id="hideN" checked={NV.data.hideN} on:click={NV.action.hideN}>
                        미실행 숨기기
                    </label>
                    &nbsp;
                    <label title="완료(C) 숨기기" style="cursor:pointer">
                        <input type="checkbox" id="chkHideC" checked={NV.data.hideC} on:click={NV.action.hideC}>
                        완료 숨기기
                    </label>
                </div>
                <div id="leftSideDesriptionCenter"></div>
                <div id="leftSideDesriptionRight">
                    <button class="xTiny" title="리스트 상단 이동"
                        on:click={NV.action.focusTop}
                        disabled={NV.data.leftSideWrapperScrollTop==0}                    
                    >{@html icon.toUp(0.8)}</button>
                    <button class="xTiny" title="리스트 하단 이동"
                        disabled={NV.data.leftSideWrapperScrollTop + NV.dom.leftSideWrapper?.offsetHeight >= NV.dom.leftSideWrapper?.scrollHeight}
                        on:click={NV.action.focusBottom}
                    >{@html icon.toDown(0.8)}</button>
                    <button class="xTiny" title="현재실행 찾기"
                        on:click={()=>{       
                            NV.action.focusToJob(NV.data.sltJob);
                            let timer = null;
                            timer = setTimeout(()=>{
                                NV.data.isJobFound = !NV.data.isJobFound;
                                clearTimeout(timer);
                                timer = setTimeout(()=>{
                                    NV.data.isJobFound = !NV.data.isJobFound;
                                    clearTimeout(timer);
                                }, 1000);
                            }, 1000);
                        }}
                    >{@html icon.pin(0.7)}</button>
                    &nbsp;&nbsp;
                    <button class="xTiny" title="실행추적 {NV.data.isFocusRunningAlways?"ON":"OFF"}"
                        style:color={NV.data.isFocusRunningAlways?"var(--link)":"rgb(0 0 0/30%)"}
                        on:click={()=>{
                            NV.data.isFocusRunningAlways = !NV.data.isFocusRunningAlways
                            NV.action.moveToLastJob();
                        }}
                    >
                        {#if NV.data.isFocusRunningAlways==true}
                            {@html icon.gpsOn(0.9)}
                        {:else}
                            {@html icon.gpsOff(0.9)}
                        {/if}
                    </button>
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
                        <div class="job {item.jobstatus}"                        
                            bind:this={NV.dom.jobList[index]}
                            class:sltJob={NV.data.sltJobcode==item.jobcode}
                            on:click={()=>{ NV.action.selectJobDetail(item.jobcode); }}
                            on:keyup
                            role="button" tabindex="0"
                        >
                            <!-- 결과확인 undefined 오류 수정 -->
                            {#if item.jobcode==undefined}
                                <span class="status">
                                    {@html `<span style="color:gray">${icon.loading(0.8)}</span>`}
                                </span>
                                <span class="jobTitle" class:jobFound={NV.data.sltJobcode==item.jobcode && NV.data.isJobFound}></span>
                                <span class="jobWorking">
                                    <span>{@html icon.rightBold(0.9)}</span>
                                </span>
                            {:else}
                                <span class="status">
                                    {#if item.jobstatus=="Q"} {@html icon.pin(1)}
                                    {:else if item.jobstatus=="R"} {@html icon.working(1)}
                                    {:else if item.jobstatus=="E"} {@html icon.warn(1.1)}
                                    {:else if item.jobstatus=="N"} {@html icon.notAllowed(1)}
                                    {:else if item.jobstatus=="S"} {@html `<span style="color:magenta">${icon.skip(1.4)}</span>`}
                                    {:else if item.jobstatus=="C"}
                                        {#if item.jobchk=="O"} {@html `<span style="color:seagreen">${icon.check(1)}</span>`}
                                        {:else}
                                            {@html `<span style="color:blue">${icon.message()}</span>`}
                                        {/if}
                                    {:else}
                                        <!-- {@html icon.pin(1)} -->
                                    {/if}
                                </span>
                                <span class="jobTitle">
                                    <!-- {item.jobstatus} {item.jobchk} -->
                                    <span class="jotTileBox"
                                        class:jobFound={NV.data.sltJobcode==item.jobcode && NV.data.isJobFound}
                                    >
                                        {item.no}. 
                                        {item.jobcode}
                                        {item.jobname}
                                        <span class="jobRemain">{`${item.cntdone}/${item.cntproc}`}</span>  
                                    </span>                      
                                    <span class="time">{item.jobendtime}</span>
                                </span>
                                <span class="jobWorking">
                                    {#if NV.data.sltJobcode==item.jobcode}
                                        <span>{@html icon.rightBold(0.9)}</span>
                                    {/if}
                                </span>
                            {/if}
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
            role="button" tabindex="0"
        />
        <!-- 오른쪽 영역 -->        
        <div id="rightSide" bind:this={NV.dom.rside}>
            <div class="labelWrapper" bind:this={NV.dom.procInfo}>
                <div class="label">
                    <span class="title">기본정보</span>
                    <span>
                        <div class="label">
                            <span class="title" style="width:30px">경로</span>
                            <span class="content">
                                { !NV.data.sltJob?.jobcode ? '' : `${NV.data.sltJob.jobsvr}(${NV.data.sltJob.jobsvrnm}) > ${NV.data.sltJob.jobfilepath}` }
                                <span class=" jobcode">{NV.data.sltJob?.jobcode??'-'}</span>
                            </span>
                        </div>
                        <div class="label">
                                <div class="label">
                                    <span class="title" style="width:30px">그룹</span>
                                    <span class="content" style="min-width:138.34px">
                                        {!NV.data.sltJob?.jobgroup ? '' : `[${NV.data.sltJob.jobgroup}] ${NV.data.sltJob.jobgroupnm}`}
                                    </span>
                                    <span class="title">기준시간</span>
                                    <span class="content">
                                        {!NV.data.sltJob?.cuttime ? '' : `${NV.data.sltJob.cuttime} ~ ${NV.data.sltJob.lasttime} (약 ${NV.data.sltJob.worktime}분 소요)`}
                                    </span>
                                </div>
                        </div>
                    </span>
                </div>
                <div class="label" style="min-height:96.95px">
                    <span class="title">배치실행</span>
                    <span class="content">
                        <span class="cursorPointer"
                            on:click={NV.action.selectPopupJobDetail}
                            on:keyup
                            role="button" tabindex="0"
                        >
                            <strong>{NV.data.sltJob?.jobfile??""}</strong>&nbsp;
                        </span>
                        {#if NV.data.sltJob}
                        <button on:click={NV.action.btnJobProcQ}
                            disabled={NV.data.btnStatus?.w1.disabled??true}
                            title="작업실행"
                        >
                            {@html NV.data.btnStatus?.w1.text??"작업실행"}
                        </button>
                        <hr>
                        <div class="flex-align-items-center">
                                <input maxlength="200" 
                                    bind:this={NV.dom.workmemo}
                                    bind:value={NV.data.sltJob.workmemo}
                                    title={NV.data.sltJob.workmemo}
                                    disabled={((NV.data.btnStatus?.w2.disabled??true)&&(NV.data.btnStatus?.w3.disabled??true))}
                                    style="margin-right:0;width:110px"/>
                            <button class:xTiny={true}
                                on:click={()=>{NV.data.sltJob.workmemo = "";}}
                                bind:this={NV.dom.resultConfirmCloseBtn}
                                style="position:relative;left:-17px;"
                                style:visibility={((NV.data.btnStatus?.w2.disabled??true)&&(NV.data.btnStatus?.w3.disabled??true))?"hidden":""}
                                disabled={((NV.data.btnStatus?.w2.disabled??true)&&(NV.data.btnStatus?.w3.disabled??true))}
                                title="코멘트 지우기"
                                >{@html icon.close(0.5)}</button>
                            <button class:small={true}
                                on:click={NV.action.btnJobProcResultConfirm}
                                disabled={NV.data.btnStatus?.w2.disabled??true}
                                title="결과확인"
                            >
                                {@html NV.data.btnStatus?.w2.text??"결과확인"}
                            </button>
                            &nbsp;
                            <button class="small"
                                on:click={NV.action.btnJobProcSkip}
                                disabled={NV.data.btnStatus?.w3.disabled??true}
                                title="작업스킵"
                            >
                                {@html NV.data.btnStatus?.w3.text??"전체 작업스킵"}
                            </button>
                        </div>
                        {/if}
                    </span>
                </div>                
                <div class="label">
                    <span class="title">실행시간</span>
                    <span class="content">
                        {!NV.data.sltJob?.jobstarttime ? '' : `${NV.data.sltJob.jobstarttime} ~ ${NV.data.sltJob.jobendtime}`}
                        <!-- {NV.data.sltJob?.jobstarttime??'-'} ~ {NV.data.sltJob?.jobendtime??'-'} -->
                    </span>
                </div>
            </div>
            <!-- PROC 그리드 -->
            <div class="gridHeaderBg1" bind:this={NV.grid.target}/>
            <!-- 상세 팝업 -->
            {#if NV.popup.display.jobDetail==true}
                <PopupJobDetail
                    on:close={()=>{NV.popup.display.jobDetail=false}}
                    job={{...NV.data.sltJob}}
                />
            {/if}
            {#if NV.popup.display.procDetail==true}
                <PopupProcDetail 
                    on:close={()=>{
                        NV.popup.display.procDetail=false;
                        NV.grid.isPopupOpen = false; //그리드 자동 focus에 필요함
                    }}
                    proc={{...NV.data.sltProc}}
                />
            {/if}
        </div>
    </div>
</jobProc>
    
<style lang="scss">
jobProc {
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
                border:solid 0px red;
                display:flex;
                justify-content:space-between;
                //top:2px;
                width:100%;
                height:1.5rem;
                line-height:1.5rem;
                font-size:0.7rem;
                #leftSideDesriptionLeft{
                    display:inline-flex;
                    padding-left:0.7rem;
                    label{
                        display:flex;
                        font-family:auto;
                    }
                    #leftSideDescipriptionLeftListCount{
                        width:90px;
                        font-family:auto;
                    }
                }
                #leftSideDesriptionCenter{
                    display:inline-flex;
                }
                #leftSideDesriptionRight{                    
                    display:inline-flex;
                    align-items:center;
                    padding-right:0.4rem;
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
                    font-size:0.9rem;   
                    // font-weight:300;
                    background-color:white;      
                    .job{
                        position:relative;
                        display:table-row;
                        height:40px;
                        cursor:pointer;
                        &.sltJob{
                            background-color:white; 
                        }
                        &.C{ //완료
                            &:hover{ background-color:rgb(0 0 0/5%) }
                        }
                        &.X{ //실행대기
                            background-color:rgb(0 0 0/10%); 
                            &:hover{ background-color:rgb(0 0 0/15%) }
                        }
                        &.N, &.H{ //보류
                            background-color:rgb(0 0 0/25%);
                            &:hover{ background-color:rgb(0 0 0/30%) }
                            color:rgb(0 0 0/40%);
                            .jobTitle span.jobRemain{
                                background-color:rgb(0 0 0/20%);
                                color:rgb(255 255 255/50%);
                                text-shadow:none;
                            }
                        }
                        &.E{ //에러
                            background-color:rgb(128 0 0/100%);//maroon; 
                            &:hover{ background-color:rgb(90 0 0/100%) }
                            animation: errorAni 1.5s linear infinite; //app.scss 참조
                            -webkit-animation: errorAni 1.5s linear infinite;
                        }
                        &.R{ //진행중
                            background-color:rgb(0 0 140/100%);//darkblue;
                            &:hover{ background-color:rgb(0 0 90/100%) }
                            color:cyan;
                            .jobTitle span.jobRemain{
                                background-color:rgb(0 200 0/80%);
                                color:white;
                                text-shadow:none;
                            }
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
                                    
                                span.jotTileBox{
                                    display:inline-block;
                                    &.jobFound{
                                        animation: jobFound 1s ease-out;
                                    }
                                    span.jobRemain{
                                        background-color:rgb(0 0 0/40%);
                                        border-radius:1rem;
                                        font-size:0.8rem;
                                        padding:2px 7px;
                                        color:white;
                                        text-shadow:0 0 3px rgb(0 0 0/50%);
                                    }
                                }
                                span.time{
                                    display:inline-block;
                                    float:right;
                                    position:relative;
                                    font-size:0.9rem;
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
                .cursorPointer{ 
                    cursor:pointer; 
                    &:hover{ color:var(--link); }
                }
                div.label{
                    display:flex;
                    align-items:stretch;
                    justify-content:left;            
                    flex-wrap:nowrap;            
                    width:100%;   
                    border-top:solid 1px #ccc;
                    font-size:0.9rem;
                    &:first-child{
                        border-top:0;
                    }
                    span{
                        border-left:solid 1px #ccc;
                        &:first-child{
                            border-left:0;
                        }
                        &:last-child{                    
                            flex-grow:1;
                        }
                    }
                    span.title {  
                        flex-shrink:0;
                        display:flex;
                        justify-content:center;
                        align-items:center;              
                        //min-width:40px;
                        padding:0.5rem;
                        background-color:#efefef;//#dbe7f3;
                        //font-size:0.8rem;
                    }
                    span.content {      
                        // display:flex;
                        // flex-direction:row;
                        // justify-content:flex-start;
                        // align-items:center;
                        background-color:white;
                        padding:0.5rem 0.5rem;
                        //width:inherit;
                        overflow:hidden;
                        //white-space:nowrap;
                        text-overflow:ellipsis;
                        //font-size:0.8rem;
                        span.jobcode{
                            float:right;
                            font-weight:bold;
                            font-size:1rem;
                            color:var(--link);
                        }
                    }
                }
            }
        }
    }
    //leftSide, rightSide 간격조정선
    div#gap{
        border-left: solid 1px rgb(0 0 0/50%);
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
            min-width:60px;
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

            span.working{            
                color:blue;
                line-height:0.5rem;
                margin-right:0.15rem;
            }

            div.tabItemTitle{
                display:flex;
                justify-content:center;
                align-items:center;
            }
        }

    }
}


</style>