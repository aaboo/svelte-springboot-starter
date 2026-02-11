<script>    
import HeadTop from '/src/comp/HeadTop.svelte';
import Menu from '/src/comp/Menu.svelte';

import app from '$lib/app';
import icon from '$lib/icon';
import { userStore, menuStore, gapLeftStore, menuNameHideWidthStore } from '$lib/stores';

let menuStoreItem = $menuStore;
let openedMenu = {};

//dispatch 관련
let idFromMenu = '';

//조회
function searchFromChild(ev){
    //페이지 ID를 key로 사용하는 이유: 모든 페이지의 파라미터가 전달되는 것을 방지하기 위함.
    if(ev.detail[$menuStore.currentMenuCID]){
        openedMenu[$menuStore.currentMenuCID].search(ev.detail[$menuStore.currentMenuCID]);
    }
}

//메뉴 선택
function idFromMenuFunc(ev){
    idFromMenu = ev.detail.id;
}

//좌측 메뉴 넓이 조절
let gap = {
    status: "none"
    , minX: 50
    , orgX : 180
    , movX: 180
    , mouseenter: function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        if(gap.status != "mousemove"){
            gap.status = "mouseenter";
            document.body.style.cursor="grab";
        }
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
        gap.movX = gap.movX;
        document.body.style.cursor="grabbing";
        document.body.onselectstart = function(){return false;};
    }
    , mouseup: function(ev){
        ev.preventDefault();
        ev.stopPropagation();
        gap.status = "mouseup";
        gap.orgX = gap.movX;
        gapLeftStore.update(gap.movX); //다른 레이아웃에 영향을 줌
        document.body.style.cursor="";
        document.body.onselectstart = null;
    }
    , mousemove: function(ev){
        //ev.preventDefault();
        ev.stopPropagation();
        if(gap.status=="mousedown" || gap.status=="mousemove"){
            gap.status = "mousemove"
            let movX = (ev.clientX??ev.touches[0].clientX);
            if(movX < gap.minX) movX = gap.minX;
            gap.movX = movX+4;
            document.body.style.cursor="grabbing";
        }
    }
}

//조회영역 보이기
let searchAreaVisible = true;
function searchToggle(){
    return searchAreaVisible = !searchAreaVisible;
}

$:if($gapLeftStore < $menuNameHideWidthStore){
    searchAreaVisible = false;
}else{
    searchAreaVisible = true;
}
</script>

<!-- 간격조정선 이벤트용 -->
<svelte:window 
    on:mousemove={gap.mousemove}
    on:mouseup={gap.mouseup}
    on:touchmove={gap.mousemove}
    on:touchend={gap.mouseup}
/>

{#if $userStore!=null }
<div id="frame">
    <header>
        <HeadTop/>
    </header>
    <!-- <content
        style:grid-template-columns={`${gap.orgX}px D)`}
    > -->
    <content>
        <div id="menu"
            style:width={(gap.orgX-1)+"px"}
            style:height={"calc(100vh - 48px)"}
        >
            <Menu on:selectMenu={idFromMenuFunc}/>
            <div id="search">
                {#if $gapLeftStore < $menuNameHideWidthStore }
                    <span id="searchIcon" title="조회하기"
                        on:click={searchToggle}
                        on:keyup
                        role="button" tabindex=0
                    >{@html icon.search(1.7)}</span>
                {/if}
                <div id="searchWrapper"                    
                    style={$gapLeftStore < $menuNameHideWidthStore?`
                            position: absolute;
                            top:${document.querySelector("#search").offsetTop+5}px;
                            left: 5px;
                            border: solid 1px gray;
                            padding: 1rem 1.5rem;
                            background-color: white;
                            z-index: 200;
                    `:""}
                    style:display={searchAreaVisible?"":"none"}
                >
                    {#if $gapLeftStore < $menuNameHideWidthStore }
                        <div id="btnCloseSearch"
                            on:click={searchToggle}
                            on:keyup
                            role="button" tabindex=0
                        >{@html icon.close(1)}</div>
                    {/if}
                    {#each $menuStore.list as component, index (component.cid)}
                        {#if component.search }
                            <div id={component.cid+"_search"} 
                                style:display={component.cid==$menuStore.currentMenuCID?"block":"none"}>
                                <svelte:component this={component.search}
                                    on:search={searchFromChild}
                                />
                            </div>
                        {/if}
                    {/each}
                    </div>
            </div>
        </div>
        <!-- 간격조정선 -->
        <div id="gap"
            on:mouseenter={gap.mouseenter}
            on:mouseleave={gap.mouseleave}
            on:mousedown={gap.mousedown}
            on:touchstart={gap.mousedown}
            style:left={(gap.movX-3)+"px"}
            style:width={gap.gapWidth+"px"}
            role="button" tabindex=0
        />
        <div id="content" style="
            left:{gap.orgX}px;
            width:calc(100vw - {gap.orgX}px);
            height:calc(100vh - {48}px);
            min-width:{app.css.contentMinWidth}px;
        ">
            {#each $menuStore.list as component, index (component.cid)}
                <div id={component.cid}
                    style:display={component.cid==$menuStore.currentMenuCID?"block":"none"}
                >
                    <svelte:component this={component.component} bind:this={openedMenu[component.cid]}/>
                </div>
            {/each}
        </div>
    </content>
</div>
{/if}

<style lang="scss">
header {
}
content {
    display:block;
    
    //grid-template-rows: 1fr;
    //height:calc(100vh - 48px);
    div#menu{
        //border:solid 1px red;
        display:block;
        position:absolute;
        vertical-align:top;
        border-right:solid 1px rgba(0 0 0/50%);
        background-color:white;//#252526;
        // overflow-x:hidden;//datePicker가 안보인다.
        // overflow-y:auto;
        div#search{
            position:flex;
            width:100%;
            border-top:solid 1px #ccc;
            //margin:0em 0.5em;
            #searchWrapper{
                position:relative;
                #btnCloseSearch{
                    position:absolute;
                    top:8px;
                    right:8px;
                    color:gray;
                    cursor:pointer;
                }             
            }
            span#searchIcon{
                display:flex;
                flex-direction:column;
                align-items:center;     
                margin:1rem;
                cursor:pointer;
            }
        }        
    }
    div#gap{
        border:solid 0px lime;
        position:absolute;
        top:47px;
        width:5px;
        height:calc(100% - 47px);
        z-index:50;
        &:active{
            background-color:rgba(0 0 0/30%);
        }
    } 
    div#content{
        display:block;
        position:absolute;
        border-left:solid 0px lightgray;
        background-color:#efefef;//#1e1e1e;
        min-height:760px;
        overflow-x:hidden;
        overflow-y:hidden;
    }
}

/* 에니메이션 : mainGapHandler */
@-webkit-keyframes mainGapHandler {
	from { width:10px; }
	to { width:20px; }
}
@-moz-keyframes mainGapHandler {
	from { width:10px; }
	to { width:20px; }
}
@keyframes mainGapHandler {
	from { width:10px; }
	to { width:20px; }
}
</style>