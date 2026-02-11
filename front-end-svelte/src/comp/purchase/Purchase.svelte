<script>
const ID = "purchase";

import { onMount } from 'svelte';
import { gapLeftStore, menuStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';

//조회 - Frame.svelte.searchFromChild(ev)
export function search(param){
    NV.action.search(param);
}

onMount(()=>{
    NV.init();
});

const NV = {
    init: function(){
        this.grid.init();
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
        app.fetch("/purchase/selectPurchase", {...param})
        .then(async (response)=>{
            let today = (new Date()).print("yyyy-mm-dd");
            //console.log(response);
            NV.grid.orgData = [...response.data];
            await NV.grid.target.resetData(NV.grid.orgData);
            app.modal.close();
        })
        .catch((e)=>{
            //console.log(e);
            app.modal.close();
        }); 
    }
}

NV.grid = {
    target: null
    , orgData: []
    , init: function(){
        const option = {
            el: NV.grid.target
            , scrollX: true
            , scrollY: true
            , width: window.innerWidth - $gapLeftStore
            , bodyHeight: window.innerHeight - 90//'fitToParent'
            , rowHeaders: ['rowNum']
            , columns: [
                {header:'매입구분', name:'ptype', width:100, align:"center", filter:"select"}
                , {header:'카드사', name:'acquier', width:100, align:"center"}
                , {header:'파일구분', name:'filetype', width:200, align:"left"}
                , {header:'생성경로', name:'filepath', width:350, align:"left"}
                , {header:'VOS경로', name:'vospath', width:350, align:"left"}
                , {header:'요청결과', name:'resulttype', width:100, align:"center"}
                , {header:'파일크기', name:'filesize', width:120, align:'right'
                    , formatter:(item) => (item.value??"0").money()
                }
                , {header:'라인수', name:'linecount', width:100, align:'right'
                    , formatter:(item) => (item.value??"0").money()
                }
                , {header:'가맹점수', name:'merchantcount', width:100, align:'right'
                    , formatter:(item) => (item.value??"0").money()
                }
                , {header:'파일생성일시', name:'makedate', width:200, align:"center"}
                , {header:'VOS파일명', name:'vosfilename', width:200, align:"center"}
                , {header:'VOS체크결과', name:'voscheck', width:120, align:"center"}
                , {header:'VOS체크일시', name:'vossendtime', width:120, align:"center"}
            ]
        }
        this.target = tuiGrid.make(option);        
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


//좌측 메뉴 넓이 조정 결과
$:{
    $gapLeftStore; onResize();
}
</script>



<!-- 중간선 이벤트용 -->
<svelte:window on:resize={onResize}/>

<purchase>
    <!-- <h1>배치관리</h1> -->
    <div id="topDescription">
        <div id="topDescriptionLeft">
            <span id="topDescriptionLeftListCount">목록개수: {NV.grid.orgData.length}</span>
        </div>
        <div id="leftSideDesriptionCenter"></div>
        <div id="leftSideDesriptionRight"></div>
    </div>
    <div bind:this={NV.grid.target}></div>
</purchase>



<style lang="scss">
purchase {
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
            #topDescriptionLeftListCount{
                width:90px;
                font-family:auto;
            }
        }
    }
}
</style>