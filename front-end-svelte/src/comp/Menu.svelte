<script>
import { onMount, createEventDispatcher } from 'svelte';
import { menuStore, gapLeftStore, menuNameHideWidthStore } from '$lib/stores';
import app from '$lib/app';
import icon from '$lib/icon';

const dispatch = createEventDispatcher();

let menu = $menuStore;//Base.svelte 참조
let menuList = $menuStore.list;//Base.svelte 참조
let currentMenuID = $menuStore.currentMenuID;
const menuNameHideWidth = 125;

//메뉴 선택
async function selectMenu(id){
    const authCheck = await app.authCheck();
    //console.log(authCheck);
    if(authCheck.data){
        //console.log($menuStore);
        const SS = JSON.parse(sessionStorage.getItem(app.name));
        const SSmenu = SS.menu;
        
        currentMenuID = id??SSmenu.currentMenuID;
        menu.currentMenuID = currentMenuID;
        menu.currentMenu = menu.obj[currentMenuID];
        menu.currentMenuCID = menu.obj[currentMenuID].cid;
        menuStore.update(menu);
        
        SS["menu"]["currentMenuID"] = menu.currentMenuID;
        SS["menu"]["currentMenu"] = menu.currentMenu;
        SS["menu"]["currentMenuCID"] = menu.currentMenuCID;
        sessionStorage.setItem(app.name, JSON.stringify(SS));
        dispatch("selectMenu", { id: id });
        await app.sessionReset();
        app.hitMenu("/"+currentMenuID);
    }else{
        location.href="/login";
    }
}

$:{
    menu = $menuStore;//Base.svelte 참조
    menuList = $menuStore.list;//Base.svelte 참조
    currentMenuID = $menuStore.currentMenuID;
}

//전쟁을 시작해 볼까
onMount(()=>{
    selectMenu();
});
</script>

{#if menuList}
<nav>
    <menu>
        <!-- <div>menu</div> -->
        <ul>
        <!-- {$menuNameHideWidthStore+","+$gapLeftStore} -->
        {#each menuList as menu, i}
            <li class:selected={menu.id==currentMenuID}
                title={`${menu.id}: ${menu.name}`}
            >
                <span 
                    style:justify-content={$gapLeftStore < $menuNameHideWidthStore?"center":""}
                    style:padding={$gapLeftStore < $menuNameHideWidthStore?"0.6rem 0 0 0":"0.6rem 0 0 1rem"}
                    on:click={()=>{selectMenu(menu.id)}}
                    on:keyup
                    role="button" tabindex={i}    
                >
                <span id="menuIcon">{@html icon[menu.id](1.5)}</span>
                {#if $gapLeftStore > $menuNameHideWidthStore}
                    <span id="menuName">{menu.name}</span>
                {/if}
                </span>
            </li>
            <!-- {#if menu.list}
                {#each menu.list as submenu, j}
                    <li sub on:click|stopPropagation|preventDefault={()=>{selectMenu(submenu.id)}}
                        class:selected={submenu.id==currentMenuID}
                    >
                        - {submenu.name}
                    </li>
                {/each}
            {/if} -->
        {/each}
        </ul>
    </menu>
</nav>
{/if}

<style lang="scss">
    nav {
        display:grid;
        height:calc(100vh - 250px);
        max-height:300px;
        margin:0;
        padding:0;
        border:solid 0px blue;
        overflow-y:auto;
        overflow-x:hidden;
        font-size:1rem;
        menu {
            margin-top:1.1rem;
            padding:0;

            // div{                
            //     font-size:0.5rem;
            //     text-align:center;
            //     color:#ccc;
            // }
            ul{
                list-style:none;
                margin:0;
                padding:0px;
                li {
                    span {
                        display:flex;
                        height:1.9rem;
                    }

                    //padding:0.6rem 0 0 1rem;
                    cursor:pointer;
                    color:black;
                    
                    border:solid 0px green;
                    overflow:hidden;
                    white-space:nowrap;
                    text-overflow:ellipsis;                    
                    
                    &:hover {
                        background-color:#efefef;
                    }
                    &:active {
                        background-color:#ccc;
                    }
                    &.selected {
                        font-weight:bold;
                        background-color:#dfdfdf; //aliceblue;
                        &:hover{
                            color:black;
                            background-color:#cfcfcf;
                        }
                        &:active {
                            background-color:#ccc;
                        }
                    }
                    // &[sub]{
                    //     padding:0.1rem 0 0.1rem 1.5rem;
                    //     color:#666;
                    // }
                    span#menuName{ margin-left:0.5rem; }
                }
            }
        }
    }
</style>