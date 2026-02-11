<script>
const ID = "filetran";

import { onMount } from 'svelte';
import { gapLeftStore, menuStore, loginTimerStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';

//조회 - Frame.svelte.searchFromChild(ev)
export function search(param){    
    NV.param = param;
    NV.action.search(param);
}

onMount(()=>{
    //window.NV = NV;
    NV.init();
    return ()=> NV.socket.close();
});

//좌측 메뉴 넓이 조정 결과
$:{
    $gapLeftStore; onResize();
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
        this.grid.init();
    }
    , data: {
        showOnlyE: false //에러만 보이기
        , hideW: false //대기중 숨기기
        , hideC: false //완료 숨기기
        , hideE: false //에러 숨기기
    }
    , param:{}
}
NV.socket = {
    target: null
    , connect: function(){
        const ds = this;
        if(ds.target==null){
            console.log("socket connect - filetran", NV.param.workdate, (new Date()).print("yyyymmdd"));
            //서버 소켓 연결
            ds.target = new WebSocket(app.WEBSOCKET+'/filetran');
            ds.target.onmessage = (msg)=>{
                //console.log("from server: ", msg.data);
                ds.action.messageRun(msg.data);
            }
            //window.sk = ds.socket;
        }
    }
    , close: function(){
        try{
            console.log("socket close - filetran");
            this.target.close();
            this.target=null;
        }catch(e){}
    }
    , action: {
        //현재 진행중인 JOB, PROC 정리
        messageRun: function(_running){
            //console.log(_running);
            const running = JSON.parse(_running);
            NV.action.filter(running);
        }
    }
}
NV.action = {
    //조회        
    search: function(param){
        //console.log(ID, param, JSON.stringify(param), JSON.stringify(param)=="{}");
        if(!param || JSON.stringify(param)=="{}") return;
        app.modal.open();//모달
        app.fetch("/filetran/selectFiletran", {...param})
        .then(async (response)=>{            
            await NV.action.filter(response.data);
            app.modal.close();
        })
        .catch((e)=>{
            //console.log(e);
            app.modal.close();
        }); 
    }
    //파일 재전송
    , sendFiletranRetry: function(param){
        const gubun = param.anylinkparam.substr(-1)=="1"?"송신":"수신"
        if(!confirm(`${gubun}을 다시 시도합니다. [${param.anylinkparam}]`)) return;
        app.modal.open();//모달
        app.fetch("/filetran/sendFiletranRetry", {...param})
        .then(async (response)=>{
            console.log(response);
            await NV.action.filter(response.data);
        })
        .catch((e)=>{
            console.log(e);
        })
        .finally(()=>{
            app.modal.close();
        });
    }
    //필터링
    , filter: async function(data){
        if(data){
            //console.log(data);
            data.map((item, index)=>{
                NV.grid.addBgClass(item);
            });
            NV.grid.orgData = [...data];
            NV.grid.filterData = [...NV.grid.orgData];
        }
        //console.log("NV.data", NV.data);
        const filterData = [...NV.grid.orgData].filter((item, idx)=>{
            //console.log(item, idx);
            if(NV.data.showOnlyE == true && item.status!="error") return false;
            else {
                if(NV.data.hideW == true && item.status=="waiting") return false;
                if(NV.data.hideC == true && item.status=="complete") return false;
                if(NV.data.hideE == true && item.status=="error") return false;
                return true;
            }
        })

        NV.grid.scrollTop = NV.grid.target.store.viewport.scrollTop;//세로 스크롤 위치 기억
        //console.log(filterData);
        NV.grid.filterData = filterData.map((item, idx)=>{ item._attributes.rowNum = idx+1; })
        await NV.grid.target.resetData(filterData);
        NV.grid.target.store.viewport.scrollTop = NV.grid.scrollTop; //세로 스크롤 위치 복원
        
    }
}

NV.grid = {
    target: null
    , orgData: []
    , filterData: []    
    , defaultContextMenu: [//기본 contextMenu
        {name:'copy', label:'복사', action:"copy"}
        , {name:'copyColumns', label:'열 복사', action:"copyColumns"}
        , {name:'copyRows', label:'행 복사', action:"copyRows"}
        , {name:'export', label:'내보내기', subMenu:[
            {name:'textExport', label:"Text로 내보내기", action:"textExport"}
            , {name:'csvExport', label:"CSV로 내보내기", action:"csvExport"}
            , {name:'excelExport', label:"엑셀로 내보내기", action:"excelExport"}
        ]}
    ]
    , init: function(){
        const ds = this;        
        const option = {
            el: NV.grid.target
            , scrollX: true
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore
            , bodyHeight: window.innerHeight - 90//'fitToParent'
            , rowHeaders: ['rowNum']
            , columns: [
                {header:'기관', name:'svrname', width:120, align:"center", filter:"select"}
                , {header:'송수신', name:'worktype', width:70, align:"center", filter:"select"}
                , {header:'파일tPay코드', name:'filecode', width:150, align:"left"}
                , {header:'파일업무명', name:'batchname', width:250, align:"left"}
                , {header:'파일코드', name:'filepre', width:80, align:"center"}
                , {header:'작업주기', name:'typekind', width:70, align:"center", formatter:(item)=>{                    
                    return `<span title='${item.row.typekindname}'>${item.value}</span>`;
                }}
                , {header:'파일경로', name:'workpath', width:230, align:"left"}
                , {header:'작업일자', name:'workdate', width:90, align:'center'}
                , {header:'마감시간', name:'cuttime', width:80, align:'center', sortable:true}
                , {header:'시작시간', name:'stime', width:80, align:'center', sortable:true}
                , {header:'완료시간', name:'etime', width:80, align:'center', sortable:true}
                , {header:'작업파일명', name:'filename', width:160, align:'left'}
                , {header:'파일크기', name:'worksize', width:100, align:'right'
                    , formatter:(item) => (item.value??"0").money()
                }
                , {header:'작업결과', name:'result', width:230, align:"left", filter:"select"}
                , {header:''}
            ]
            , contextMenu:({rowKey, columnName})=>{
                const item = NV.grid.target.getRow(rowKey);
                //console.log(rowKey, columnName, item);
                if(item.anylinkparam!=""){
                    
                    const gubun = item.anylinkparam.substr(-1)=="1"?"송신":"수신"
                    return [ds.defaultContextMenu, [{
                            name:item.anylinkparam
                            , label: `<b><span style='color:red'>재${gubun}</span></b> [${item.filecode} : ${item.anylinkparam}]`
                            , action:()=>{
                                const param = {
                                    anylinkparam: item.anylinkparam
                                    , tokn: item.tokn
                                };
                                console.log(param);
                                NV.action.sendFiletranRetry(param)
                            }
                        }]
                    ]
                }else{
                    return [ds.defaultContextMenu]
                }
            }
        }

        ds.target = tuiGrid.make(option);
        ds.target.on('mouseover', (ev)=>{
            const {rowKey, columnName, targetType} = ev;
            if(targetType=='cell'){
                const item = ds.target.getRow(rowKey);
                const cellElement = ds.target.getElement(rowKey, columnName);
                //console.log(item, cellElement);
                if(cellElement){
                    cellElement.setAttribute("title", item.anylinkparam);
                }
            }

        })
    }
    //조회결과 색상변경
    , addBgClass: (item)=>{ 
        if(item.status == "waiting") item["_attributes"] = {className:{row:["waiting"]}}
        else if(item.status == "working") item["_attributes"] = {className:{row:["working"]}}
        else if(item.status == "error") item["_attributes"] = {className:{row:["error"]}}
        else if(item.status == "complete") item["_attributes"] = {className:{row:["complete"]}}

    }
}

let resizeTimer = null;
function onResize(ev){
    ev?.preventDefault();
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(()=>{
        //console.log(window.innerWidth, window.innerHeight);
        NV.grid.target.setWidth(Math.max(app.css.contentMinWidth, window.innerWidth-$gapLeftStore));
        NV.grid.target.setBodyHeight(window.innerHeight - 113);
    }, 200);
}
</script>
    
    
    
<!-- 중간선 이벤트용 -->
<svelte:window on:resize={onResize}/>

<filetran>
    <!-- <h1>파일송수신</h1> -->
    <div id="topDescription">
        <div id="topDescriptionLeft">
            <span id="topDescriptionLeftListCount">목록개수: {NV.grid.filterData.length}</span>
            <label title="에러만 보이기" style="cursor:pointer">
                <input type="checkbox" checked={NV.data.showOnlyE} on:click={()=>{NV.data.showOnlyE=!NV.data.showOnlyE; NV.action.filter();}}>
                에러만 보이기
            </label>
            &nbsp;
            /
            &nbsp;
            <label title="대기중 숨기기" style="cursor:pointer">
                <input type="checkbox" checked={NV.data.hideW} on:click={()=>{NV.data.hideW=!NV.data.hideW; NV.action.filter();}}>
                대기중 숨기기
            </label>
            &nbsp;
            <label title="완료 숨기기" style="cursor:pointer">
                <input type="checkbox" checked={NV.data.hideC} on:click={()=>{NV.data.hideC=!NV.data.hideC; NV.action.filter();}}>
                완료 숨기기
            </label>
            &nbsp;
            <label title="에러 숨기기" style="cursor:pointer">
                <input type="checkbox" checked={NV.data.hideE} on:click={()=>{NV.data.hideE=!NV.data.hideE; NV.action.filter();}}>
                에러 숨기기
            </label>
        </div>
        <div id="leftSideDesriptionCenter"></div>
        <div id="leftSideDesriptionRight"></div>
    </div>
    <div id="grid" bind:this={NV.grid.target}></div>
</filetran>



<style lang="scss">
filetran {
    display:block;
    position:relative;
    top:-1px;
    #topDescription{
        border:solid 0px red;
        display:flex;
        justify-content:space-between;
        //top:2px;
        width:100%;
        height:1.5rem;
        line-height:1.5rem;
        font-size:0.7rem;
        #topDescriptionLeft{
            display:inline-flex;
            padding-left:0.7rem;
            label{
                display:flex;
                font-family:auto;
            }
            #topDescriptionLeftListCount{
                width:90px;
                font-family:auto;
            }
        }
    }
}
</style>