<script>
import { onMount, createEventDispatcher } from 'svelte';
import app from '$lib/app';
import icon from '$lib/icon';

export let title = "";
export let width = 800;
export let height = 1000;
export let maxWidth = window.innerWidth - 100;
export let maxHeight = window.innerHeight - 200;
export let showFooter = true;
export let showCloseButton = true;
export let zIndex = 300; //z-index

const dispatch = createEventDispatcher();
const uuid = utils.uuid();

const popup = {
    isDragStart: false //팝업 드래그 움직임 여부
    , dom : {
        target: null//bind:this={popup.target}
        , header: null
        , body: null
        , footer: null
        , corver: null//bind:this={popup.corver}
    }
    , width: width
    , height: height
    , pos:{
        x: 0
        , y: 0
        , startX: 0
        , startY: 0
    }
    , init: ()=>{
        //console.log(data);
        const dom = popup.dom;
        popup.width = width.toString().number();//Math.min(width,window.innerWidth-40)??dom.target.clientWidth//ev.clientX
        //TODO
        popup.height = height.toString().number()??dom.target.offsetHeight;//data.height;//Math.min(data.height,window.innerHeight-40)??dom.target.clientHeight//ev.clientY
        popup.bodyWidth = Math.min(maxWidth, popup.width);//Math.min(width,window.innerWidth-40)??dom.target.clientWidth//ev.clientX
        popup.bodyHeight = popup.height - 50 - (showFooter?70:0) - 0
        popup.bodyHeight = Math.min(maxHeight, popup.bodyHeight);//Math.min(data.height,window.innerHeight-40)??dom.target.clientHeight//ev.clientY
        popup.addHeight = popup.width > popup.bodyWidth  ? 17 : 0; //가로스크롤 영향
        popup.height += popup.addHeight; 
    
        if(window.innerWidth >  dom.target.clientWidth) popup.pos.x = (window.innerWidth - dom.target.clientWidth)/2;
        else popup.pos.x = (dom.target.clientWidth - window.innerWidth)/2;    //popup.pos.x = 0;    
        if(window.innerHeight > popup.height) popup.pos.y = (window.innerHeight - popup.height)/2;
        else popup.pos.y = (popup.height - window.innerHeight + (200/3))/2;
        //console.log(popup);
    }
    , dragStart:(ev)=>{
        //console.log("PopupDragStart");
        ev.preventDefault();
        const dom = popup.dom;
        popup.isDragStart = true;
        popup.pos.startX = ev.clientX - dom.target.offsetLeft;
        popup.pos.startY = ev.clientY - dom.target.offsetTop;
        //popup.monitor(ev);
    }
    , dragIng: (ev)=>{
        //ev.preventDefault();
        ev.stopPropagation();
        if(popup.isDragStart==true){            
            //console.log("PopupDragIng");
            popup.pos.x = ev.clientX - popup.pos.startX; //popup.target.offsetLeft
            popup.pos.y = ev.clientY- popup.pos.startY; //popup.target.offsetTop
            //this = this;
        }
        //popup.monitor(ev);
    }
    , dragEnd:(ev)=>{
        ev.preventDefault();
        popup.isDragStart = false;
        // popup.pos.x = popup.target.offsetLeft - ev.clientX;
        // popup.pos.y = ev.clientY - popup.target.offsetTop;
        //popup.monitor(ev);
    }
    , close: (ev)=>{
        ev.preventDefault();
        ev.stopPropagation();
        dispatch("close");
        popup.init();
    }
    , monitor: (ev)=>{
        console.log(
            // popup,
            {...popup.pos
                , width:popup.width
                , height:popup.height
            }
        );
    }
}

onMount(()=>{
    popup.init();
    //console.log(popup);
    //console.log("data", data);
    //window.popup = popup;
});
</script>

<div id="popup-{uuid}" class="popup popup-container"
    style:z-index={zIndex}
>
    <div class="wrapper">
        <div class="content" bind:this={popup.dom.target}
            style:left={popup.pos.x+"px"}
            style:top={popup.pos.y+"px"}
            style:width={popup.width+"px"}            
            style:max-width={maxWidth+"px"}            
            style:border={popup.isDragStart?"":"solid 1px #666"}
            style:background-color={popup.isDragStart?"":"white"}
        >
       <!--  <div class="content" bind:this={popup.dom.target}
            style:left={popup.pos.x+"px"}
            style:top={popup.pos.y+"px"}
            style:width={popup.width+"px"}
            style:height={popup.height+"px"}
            style:max-width={maxWidth+"px"}
            style:max-height={maxHeight+"px"}
            style:border={popup.isDragStart?"":"solid 1px #666"}
            style:background-color={popup.isDragStart?"":"white"}
        > -->
            <div class="head" bind:this={popup.dom.head}
                style:width={popup.bodyWidth+"px"}
                style:visibility={popup.isDragStart?"hidden":""}
            >
                <div class="headBody">
                    <span class="title"
                        on:mousedown={popup.dragStart}
                        on:keyup
                        role="button" tabindex=0
                    >{@html title?icon.pin()+'&nbsp'+title:icon.loading()}</span>
                    {#if showCloseButton==true}
                        <span class="btnClose" 
                            on:click={popup.close}
                            on:keyup
                            role="button" tabindex=0
                        >{@html icon.close()}</span>
                    {/if}
                </div>
            </div>
            <!-- <div class="body" bind:this={popup.dom.body}
                style:height={popup.bodyHeight}
                style:min-width={false?"":popup.width+"px"}
                style:visibility={popup.isDragStart?"hidden":""}
            > -->
            <div class="body" bind:this={popup.dom.body}
                style:max-height={popup.bodyHeight+"px"}
                style:visibility={popup.isDragStart?"hidden":""}
            >
                <slot name="body"/>
            </div>

            {#if showFooter==true}
                <div class="footer" bind:this={popup.dom.footer}
                    style:visibility={popup.isDragStart?"hidden":""}
                >
                    <div class="footerBody" style={`
                        width:${popup.bodyWidth}px;
                    `}>
                        <slot name="footer"/>
                    </div>
                </div>
            {/if}

            <div class="cover" bind:this={popup.dom.cover}
                style:display={popup.isDragStart?"block":"none"}
                style:background-color={popup.isDragStart?"rgba(255 255 255/50%)":"white"}
            />
        </div>
    </div>
</div>

<svelte:window
    on:mouseup={popup.dragEnd}
    on:mousemove={popup.dragIng}
/>

<style lang="scss">

.popup-container {
    position:fixed;
    top:0;
    left:0;
    width:100vw;
    height:100vh;
    background-color:rgba(0 0 0/30%);
    //overflow-y:auto;
    .wrapper{
        border:solid 0px orange;
        display:flex;
        flex-direction:column;
        justify-content:center;
        align-items:center;
        height:100vh;
        .content{
            position: absolute;
            box-shadow:1px 2px 7px rgba(0 0 0/40%);
            border-radius:0.5rem;
            overflow-y: hidden;
            .head{
                height:50px;
                margin:0;
                border-bottom:solid 0px white;
                //position:fixed;
                cursor:grab;
                .headBody{
                    position:fixed;
                    width:inherit;
                    border-radius:0.5rem 0.5rem 0 0;
                    background-color:#efefef;
                }
                &:hover{
                    //background-color:#ddd;
                }
                &:active{
                    background-color:#ccc;
                    cursor:grabbing;
                }
                .title{
                    display:flex;
                    flex-direction:row;
                    justify-content:left;
                    align-items:center;
                    border:solid 0px red;
                    padding: 1rem 0 0.5rem 1rem;
                    font-weight:bold;
                    font-size:1.1rem;
                }
                .btnClose{
                    border:solid 0px red;
                    display:block;
                    position:absolute;
                    padding:10px 14px 0 0px;
                    top:0px;
                    right:0px;
                    float:right;
                    color:rgba(0 0 0/30%);
                    cursor:pointer;
                    &:hover{
                        color:rgba(0 0 0/60%);
                    }
                }
            }
            .body{
                //position:relative;
                //top:50px;
                border:solid 0px blue;
                width: inherit;//fit-content;//100%;
                overflow-x:hidden;
                overflow-y:auto;
            }            
            .footer{
                height:70px;
                .footerBody{
                    display:flex;
                    flex-direction:row;
                    align-items:center;
                    justify-content:center;
                    text-align:center;
                    vertical-align:middle;
                    height:70px;
                    position:fixed;
                    border-radius:0 0 0.5rem 0.5rem ;
                    background-color:#efefef;
                }
            }
            .cover{
                border:solid 0px yellow;
                position:absolute;
                top:0;
                left:0;
                width:100%;
                height:100%;
                cursor:grabbing;
                display:none;
            }
        }
    }
}
</style>