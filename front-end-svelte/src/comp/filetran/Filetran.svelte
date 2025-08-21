<script>
const ID = "filetran";

import { onMount } from 'svelte';
import { gapLeftStore, menuStore } from '$lib/stores';
import 'tui-grid/dist/tui-grid.css';
import app from '$lib/app';
import tuiGrid from '$lib/tuiGrid';
import Popup from '../Popup.svelte';

export let paramFromSearch = "";

onMount(()=>{
    NV.init();
    //window.NV = NV;
});

//좌측 메뉴 넓이 조정 결과
$:{
    $gapLeftStore; onResize();
}

//조회
$:if($menuStore.currentMenuCID==ID){
    NV.action.search({...paramFromSearch[ID]});
    paramFromSearch[ID] = ""; //조회 후 초기화
}

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
        app.fetch("/filetran/selectFiletran", {...param})
        .then(async (response)=>{
            let today = (new Date()).print("yyyy-mm-dd");
            //console.log(response);
            response.data.map((item, index)=>{
                NV.grid.addBgClass(item);
            });
            await NV.grid.target.resetData([...response.data]);
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
    , init: function(){
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
                , {header:'파일경로', name:'workpath', width:230, align:"left"}
                , {header:'작업일자', name:'workdate', width:90, align:'center'}
                , {header:'시작시간', name:'stime', width:80, align:'center', sortable:true}
                , {header:'완료시간', name:'etime', width:80, align:'center', sortable:true}
                , {header:'작업파일명', name:'filename', width:160, align:'left'}
                , {header:'파일크기', name:'worksize', width:100, align:'right'
                    , formatter:(item) => (item.value??"0").money()
                }
                , {header:'작업결과', name:'result', width:230, align:"left", filter:"select"}
                , {header:''}
            ]
        }
        this.target = tuiGrid.make(option);        
    }
    //조회결과 색상변경
    , addBgClass: (item)=>{        
        if(item.resultcode!="" && item.resultcode!="0000"){
            item["_attributes"] = {className:{row:["error"]}}
        }
    }
}

let resizeTimer = null;
function onResize(ev){
    ev?.preventDefault();
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(()=>{
        //console.log(window.innerWidth, window.innerHeight);
        NV.grid.target.setWidth(Math.max(app.css.contentMinWidth, window.innerWidth-$gapLeftStore));
        NV.grid.target.setBodyHeight(window.innerHeight - 90);
    }, 200);
}
</script>
    
    
    
<!-- 중간선 이벤트용 -->
<svelte:window on:resize={onResize}/>

<purchase>
    <!-- <h1>배치관리</h1> -->
    <div bind:this={NV.grid.target}></div>
</purchase>



<style lang="scss">
purchase {
    display:block;
    position:relative;
    top:-1px;
}
</style>