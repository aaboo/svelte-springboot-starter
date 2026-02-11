<script>    
    import { menuStore } from '$lib/stores';
    import app from '$lib/app';

    /**********************************************************
    * 메뉴
    * CSR(Client Side Rendering)에서는 동적 import가 불가능하기 때문에 미리 선언할 수 밖에 없었다.
    **********************************************************/
    //배치관리
    import JobCalendar from '/src/comp/jobCalendar/JobCalendar.svelte';
    //배치작업
    import JobProc from '/src/comp/jobProc/JobProc.svelte';
    import JobProc_search from '/src/comp/jobProc/JobProc_search.svelte';
    //배치결과
    import BatchResult from '/src/comp/batchResult/BatchResult.svelte';
    import BatchResult_search from '/src/comp/batchResult/BatchResult_search.svelte';
    //DAEMON
    import Daemon from '/src/comp/daemon/Daemon.svelte';
    import Daemon_search from '/src/comp/daemon/Daemon_search.svelte';
    //매입처리
    import Purchase from '/src/comp/purchase/Purchase.svelte';
    import Purchase_search from '/src/comp/purchase/Purchase_search.svelte';
    //파일송수신
    import Filetran from '/src/comp/filetran/Filetran.svelte';
    import Filetran_search from '/src/comp/filetran/Filetran_search.svelte';

    const defaultMenuID = "jobProc";
    const menuList = [
        {id:"jobCalendar", cid:"jobCalendar", name:"메뉴1", component:JobCalendar}
        , {id:"jobProc", cid:"jobProc", name:"메뉴2", component:JobProc, search:JobProc_search}
        , {id:"batchResult", cid:"batchResult", name:"메뉴3", component:BatchResult, search:BatchResult_search}
        , {id:"daemon", cid:"daemon", name:"메뉴4", component:Daemon, search:Daemon_search}
        , {id:"purchase", cid:"purchase", name:"메뉴5", component:Purchase, search:Purchase_search}
        , {id:"filetran", cid:"filetran", name:"메뉴6", component:Filetran, search:Filetran_search}
    ];
    const menuObj = {};
    menuObjMaker([...menuList]);

    //menuList Array를 Object형으로 변경(key: id)
    function menuObjMaker(list){
        list.map(async (item, idx)=>{            
            menuObj[item.id] = {...item};
            if(item.list) menuObjMaker([...item.list]);
        })
    }

    const SS = JSON.parse(sessionStorage.getItem(app.name));
    const menu = SS.menu;
    const currentMenuID = menu.currentMenuID??defaultMenuID;
    const menuStoreItem = {
        currentMenuID: currentMenuID
        , currentMenuCID: menuObj[currentMenuID].cid
        , currentMenu: menuObj[currentMenuID]
        , list: [...menuList]
        , obj: {...menuObj}
    };
    SS["menu"] = menuStoreItem;
    sessionStorage.setItem(app.name, JSON.stringify(SS));        
    menuStore.update(menuStoreItem);
</script>

<div id="content">

    <slot/>

</div>

<style lang="scss">
    
</style>